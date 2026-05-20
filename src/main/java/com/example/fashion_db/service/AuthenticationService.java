package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.*;
import com.example.fashion_db.dto.response.AuthenticationResponse;
import com.example.fashion_db.dto.response.IntrospectResponse;
import com.example.fashion_db.dto.response.UserResponse;
import com.example.fashion_db.entity.InvalidatedToken;
import com.example.fashion_db.entity.PasswordResetToken;
import com.example.fashion_db.entity.Role;
import com.example.fashion_db.entity.User;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mail.MailService;
import com.example.fashion_db.mapper.UserMapper;
import com.example.fashion_db.repository.InvalidatedTokenRepository;
import com.example.fashion_db.repository.PasswordResetTokenRepository;
import com.example.fashion_db.repository.RoleRepository;
import com.example.fashion_db.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    MailService mailService;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;

    public UserResponse register(UserRegisterRequest request) {
        User user = userMapper.toUser(request);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user, VALID_DURATION);
        var refreshToken = generateToken(user, REFRESH_DURATION);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(authenticated)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            var username = signToken.getJWTClaimsSet().getSubject();
            var user = userRepository.findByUsername(username)
                    .orElse(null);

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().jwtId(jit).expiryTime(expiryTime).user(user).build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        var signJWT = verifyToken(request.getRefreshToken(), true);
        var jti = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        InvalidatedToken invalidatedToken = InvalidatedToken.builder().jwtId(jti).expiryTime(expiryTime).user(user).build();
        invalidatedTokenRepository.save(invalidatedToken);

        var token = generateToken(user, VALID_DURATION);
        var refreshToken = generateToken(user, REFRESH_DURATION);

        return AuthenticationResponse.builder().token(token).refreshToken(refreshToken).authenticated(true).build();
    }

    // Gửi email reset password
    @Transactional
    public void forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Xóa token cũ nếu có
        passwordResetTokenRepository.deleteByUser_Id(user.getId());

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .used(false)
                .expiryTime(LocalDateTime.now().plusMinutes(15))
                .build();
        passwordResetTokenRepository.save(resetToken);

        // Gửi email
        mailService.sendResetPasswordEmail(email, user.getUsername()  ,token);
    }

    // Đặt lại mật khẩu
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_RESET_TOKEN));

        if (resetToken.isUsed())
            throw new AppException(ErrorCode.RESET_TOKEN_USED);

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new AppException(ErrorCode.RESET_TOKEN_EXPIRED);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Đánh dấu token đã dùng
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                            .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                            .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expityTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsByJwtId(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;

    }

    public String generateToken(User user, long duration) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("fashion.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
            });

        return stringJoiner.toString();
    }


}
