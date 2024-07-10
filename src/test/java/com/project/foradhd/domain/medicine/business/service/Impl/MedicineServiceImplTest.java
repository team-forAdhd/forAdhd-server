package com.project.foradhd.domain.medicine.business.service.Impl;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MedicineServiceImplTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineMapper medicineMapper;

    @Spy
    @InjectMocks
    private MedicineServiceImpl medicineService;

    private MedicineDto medicineDto;
    private Medicine medicine;

    @BeforeEach
    void setUp() {
        medicineDto = MedicineDto.builder()
                .itemSeq("123456")
                .itemName("Test Medicine")
                .entpSeq("123")
                .entpName("Test Entp")
                .chart("Chart")
                .itemImage("image.jpg")
                .drugShape("Shape")
                .colorClass1("Color1")
                .colorClass2("Color2")
                .classNo("ClassNo")
                .className("ClassName")
                .formCodeName("FormCodeName")
                .itemEngName("Test Medicine Eng")
                .rating(4.5)
                .isFavorite(false)
                .build();

        medicine = Medicine.builder()
                .itemSeq("123456")
                .itemName("Test Medicine")
                .rating(4.5)
                .build();
    }

    @Test
    void saveMedicine_ShouldSaveMedicine() throws IOException {
        // given
        String itemname = "Test Medicine";
        String json = "{\"body\": {\"items\": [{\"ITEM_SEQ\": \"123456\", \"ITEM_NAME\": \"Test Medicine\", \"RATING\": 4.5}]}}";

        // Mock the internal methods
        doReturn(json).when(medicineService).fetchMedicineInfo(itemname);
        doReturn(medicineDto).when(medicineService).parseMedicine(json);
        given(medicineMapper.toEntity(medicineDto)).willReturn(medicine);
        given(medicineRepository.save(medicine)).willReturn(medicine);

        // when
        medicineService.saveMedicine(itemname);

        // then
        then(medicineRepository).should().save(medicine);
    }

    @Test
    void getSortedMedicines_ShouldReturnSortedMedicinesByNameAsc() {
        // given
        List<Medicine> medicines = Collections.singletonList(medicine);
        given(medicineRepository.findAllByOrderByItemNameAsc()).willReturn(medicines);
        given(medicineMapper.toDto(medicine)).willReturn(medicineDto);

        // when
        List<MedicineDto> result = medicineService.getSortedMedicines("nameAsc");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemName()).isEqualTo("Test Medicine");
    }

    @Test
    void getSortedMedicines_ShouldReturnSortedMedicinesByRatingDesc() {
        // given
        List<Medicine> medicines = Collections.singletonList(medicine);
        given(medicineRepository.findAllByOrderByRatingDesc()).willReturn(medicines);
        given(medicineMapper.toDto(medicine)).willReturn(medicineDto);

        // when
        List<MedicineDto> result = medicineService.getSortedMedicines("ratingDesc");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRating()).isEqualTo(4.5);
    }

    @Test
    void searchByFormCodeNameAndShapeAndColor_ShouldReturnMedicines() {
        // given
        List<Medicine> medicines = Collections.singletonList(medicine);
        given(medicineRepository.findAllByFormCodeNameOrDrugShapeOrColorClass1("formCode", "shape", "color1")).willReturn(medicines);

        // when
        List<Medicine> result = medicineService.searchByFormCodeNameAndShapeAndColor("formCode", "shape", "color1");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemName()).isEqualTo("Test Medicine");
    }

    @Test
    void searchByItemName_ShouldReturnMedicines() {
        // given
        List<Medicine> medicines = Collections.singletonList(medicine);
        given(medicineRepository.findByItemNameContaining("Test")).willReturn(medicines);

        // when
        List<Medicine> result = medicineService.searchByItemName("Test");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemName()).isEqualTo("Test Medicine");
    }
}
