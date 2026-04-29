package com.example.fashion_db.mapper;

import com.example.fashion_db.dto.request.SeasonRequest;
import com.example.fashion_db.dto.response.SeasonResponse;
import com.example.fashion_db.entity.Season;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeasonMapper {
    Season toSeason(SeasonRequest request);
    SeasonResponse toSeasonResponse(Season season);

}