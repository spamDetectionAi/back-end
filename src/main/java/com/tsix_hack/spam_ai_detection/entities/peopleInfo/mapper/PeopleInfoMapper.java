package com.tsix_hack.spam_ai_detection.entities.peopleInfo.mapper;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PeopleInfoMapper {
    PeopleInfoMapper INSTANCE = Mappers.getMapper(PeopleInfoMapper.class);

    PeopleInfoDTO toDTO(PeopleInfo peopleInfo);
    PeopleInfo toEntity(PeopleInfoDTO peopleInfoDTO);
}
