package com.project.foradhd.domain.medicine.web.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineBookmarkRequest;
import com.project.foradhd.domain.medicine.web.dto.request.MedicineRequest;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineBookmarkResponse;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineFilteringResponse;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    MedicineMapper INSTANCE = Mappers.getMapper(MedicineMapper.class);
    MedicineFilteringResponse toMedicineFilteringResponseDto(JsonNode jsonNode);

    Medicine medicineRequestDtoToMedicine(MedicineRequest dto);
    MedicineResponse medicineToMedicineResponseDto(Medicine medicine);

    MedicineBookmark medicineBookmarkRequestDtoToMedicineBookmark(MedicineBookmarkRequest dto);
    MedicineBookmarkResponse medicineBookmarkToMedicineBookmarkResponseDto(MedicineBookmark medicineBookmark);
}
