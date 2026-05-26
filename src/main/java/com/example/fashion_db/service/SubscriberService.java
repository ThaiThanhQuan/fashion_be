package com.example.fashion_db.service;

import com.example.fashion_db.dto.response.CollectionResponse;
import com.example.fashion_db.entity.Collection;
import com.example.fashion_db.entity.Subscriber;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mail.MailService;
import com.example.fashion_db.repository.SubscriberRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;
import com.example.fashion_db.dto.response.SubscriberResponse;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SubscriberService {

    SubscriberRepository subscriberRepository;
    MailService mailService;

    public void subscribe(String email) throws MessagingException, UnsupportedEncodingException {
        if (subscriberRepository.existsByEmail(email))
            throw new AppException(ErrorCode.EMAIL_ALREADY_SUBSCRIBED);

        subscriberRepository.save(Subscriber.builder()
                .email(email)
                .build());

        // Gửi mail xác nhận
        mailService.sendSubscribeConfirmEmail(email);
    }

    public List<SubscriberResponse> getAllSubscribers() {
        return subscriberRepository.findAll().stream()
                .map(subscriber -> SubscriberResponse.builder()
                        .id(subscriber.getId())
                        .email(subscriber.getEmail())
                        .subscribedAt(subscriber.getSubscribedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // Gửi mail cho tất cả subscriber khi có collection mới
    public void notifyNewCollection(CollectionResponse collection) {
        subscriberRepository.findAll()
                .forEach(subscriber ->
                        {
                            try {
                                mailService.sendNewCollectionEmail(
                                        subscriber.getEmail(),
                                        collection.getTitle(),
                                        collection.getSlug(),
                                        collection.getThumbnail()
                                );
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
