package com.example.fashion_db.job;

import com.example.fashion_db.repository.InvalidatedTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenCleanupJob {

    InvalidatedTokenRepository invalidatedTokenRepository;

    // Chạy lúc 8h sáng mỗi ngày
    @Scheduled(cron = "0 0 8 * * *")
    public void cleanupExpiredTokens() {
        log.info(">>> Bắt đầu dọn dẹp invalidated tokens...");

        // Chỉ xóa token đã hết hạn
        int deleted = invalidatedTokenRepository.deleteByExpiryTimeBefore(new Date());

        log.info(">>> Đã xóa {} tokens hết hạn", deleted);
    }
}