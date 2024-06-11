package com.project.foradhd.domain.medicine.web.mapper;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineResponse;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineSearchResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    Medicine toEntity(MedicineDto dto);

    MedicineDto toDto(Medicine entity);

    List<Medicine> toEntityList(List<MedicineDto> dtoList);

    List<MedicineResponse> toDtoList(List<Medicine> entityList);
    MedicineSearchResponse toResponseDto(Medicine entity);
    List<MedicineSearchResponse> toResponseDtoList(List<Medicine> entityList);
}
