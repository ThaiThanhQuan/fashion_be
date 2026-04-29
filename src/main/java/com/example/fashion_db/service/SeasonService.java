package com.example.fashion_db.service;

import com.example.fashion_db.dto.request.SeasonRequest;
import com.example.fashion_db.dto.response.SeasonResponse;
import com.example.fashion_db.entity.Season;
import com.example.fashion_db.exception.AppException;
import com.example.fashion_db.exception.ErrorCode;
import com.example.fashion_db.mapper.SeasonMapper;
import com.example.fashion_db.repository.SeasonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SeasonService {

    SeasonRepository seasonRepository;
    SeasonMapper seasonMapper;

    public SeasonResponse createSeason(SeasonRequest request) {
        if (seasonRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.SEASON_EXISTED);

        return seasonMapper.toSeasonResponse(seasonRepository.save(seasonMapper.toSeason(request)));
    }

    public List<SeasonResponse> getAllSeasons() {
        return seasonRepository.findAll()
                .stream()
                .map(seasonMapper::toSeasonResponse)
                .toList();
    }
    public void deleteSeason(String seasonId) {
        seasonRepository.deleteById(seasonId);
    }
}