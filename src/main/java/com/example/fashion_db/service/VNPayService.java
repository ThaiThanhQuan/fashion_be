package com.example.fashion_db.service;

import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VNPayService {

    @Value("${vnpay.tmn-code}")
    String tmnCode;

    @Value("${vnpay.hash-secret}")
    String hashSecret;

    @Value("${vnpay.url}")
    String vnpayUrl;

    @Value("${vnpay.return-url}")
    String returnUrl;

    public String createPaymentUrl(String orderId, Long amount) {
        try {
            Map<String, String> params = new TreeMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", tmnCode);
            params.put("vnp_Amount", String.valueOf(amount * 100));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", orderId);
            params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
            params.put("vnp_OrderType", "other");
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", returnUrl);
            params.put("vnp_IpAddr", "127.0.0.1");
            params.put("vnp_CreateDate",
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            params.forEach((key, value) -> {
                try {
                    hashData.append(key).append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                            .append("&");
                    query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII))
                            .append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                            .append("&");
                } catch (Exception e) {
                    log.error("Error encoding param", e);
                }
            });

            String hashDataStr = hashData.toString().replaceAll("&$", "");
            String secureHash = hmacSHA512(hashSecret, hashDataStr);
            query.append("vnp_SecureHash=").append(secureHash);

            return vnpayUrl + "?" + query;
        } catch (Exception e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }

    public boolean validateCallback(Map<String, String> params) {
        String secureHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        Map<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder hashData = new StringBuilder();

        sortedParams.forEach((key, value) -> {
            try {
                hashData.append(key).append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                        .append("&");
            } catch (Exception e) {
                log.error("Error encoding param", e);
            }
        });

        String calculatedHash = hmacSHA512(hashSecret,
                hashData.toString().replaceAll("&$", ""));
        return calculatedHash.equals(secureHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hash) result.append(String.format("%02x", b));
            return result.toString();
        } catch (Exception e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }
}
