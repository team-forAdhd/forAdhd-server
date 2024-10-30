package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.business.service.impl.MedicineServiceImpl;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.TabletType;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.BDDMockito.willDoNothing;

@TestPropertySource(properties = {"service.medicine.url=https://medicine-service-url",
        "service.medicine.key=service-medicine-key"})
@Import(MedicineServiceImpl.class)
@ExtendWith(SpringExtension.class)
class MedicineServiceImplTest {

    @Autowired
    MedicineServiceImpl medicineService;

    @MockBean
    MedicineRepository medicineRepository;

    @MockBean
    MedicineMapper medicineMapper;

    @MockBean
    MedicineSearchHistoryService searchHistoryService;

    @Disabled("medicineService는 mock이 아니므로 stubbing 불가")
    @Test
    void saveMedicine_shouldSaveMedicine() throws IOException {
        //given
        String itemName = "Tylenol";
        String jsonResponse = "{ \"body\": { \"items\": [ { \"itemName\": \"Tylenol\", \"rating\": 4.5 } ] } }";
        MedicineDto dto = MedicineDto.builder()
                .itemName("Tylenol")
                .rating(4.5)
                .build();

        Medicine medicine = Medicine.builder()
                .itemName(dto.getItemName())
                .rating(dto.getRating())
                .build();

        given(medicineService.fetchMedicineInfo(itemName)).willReturn(jsonResponse);
        given(medicineService.parseMedicine(jsonResponse)).willReturn(dto);
        given(medicineMapper.toEntity(dto)).willReturn(medicine);
        given(medicineRepository.save(medicine)).willReturn(medicine);

        //when
        medicineService.saveMedicine(itemName);

        //then
        verify(medicineRepository, times(1)).save(medicine);
    }

    @Test
    void getSortedMedicines_shouldReturnSortedMedicines() {
        //given
        String sortOption = "nameAsc";
        String userId = "user123";

        Medicine medicine1 = Medicine.builder().itemName("Aspirin").build();
        Medicine medicine2 = Medicine.builder().itemName("Tylenol").build();

        List<Medicine> medicines = Arrays.asList(medicine1, medicine2);
        List<MedicineDto> expectedDtos = Arrays.asList(
                MedicineDto.builder().itemName("Aspirin").build(),
                MedicineDto.builder().itemName("Tylenol").build()
        );

        given(medicineRepository.findAllByOrderByItemNameAsc()).willReturn(medicines);
        given(medicineMapper.toDto(medicine1)).willReturn(expectedDtos.get(0));
        given(medicineMapper.toDto(medicine2)).willReturn(expectedDtos.get(1));

        //when
        List<MedicineDto> result = medicineService.getSortedMedicines(sortOption, userId);

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getItemName()).isEqualTo("Aspirin");
        assertThat(result.get(1).getItemName()).isEqualTo("Tylenol");
    }

    @Test
    void searchByFormCodeNameShapeColorAndTabletType_shouldReturnMedicines() {
        //given
        String formCodeName = "Tablet";
        String shape = "Round";
        String color1 = "White";
        TabletType tabletType = TabletType.TABLET;

        Medicine medicine1 = Medicine.builder().itemName("Aspirin").build();
        Medicine medicine2 = Medicine.builder().itemName("Tylenol").build();

        List<Medicine> medicines = Arrays.asList(medicine1, medicine2);
        given(medicineRepository.findAllByFormCodeNameOrDrugShapeOrColorClass1OrTabletType(formCodeName, shape, color1, tabletType))
                .willReturn(medicines);

        //when
        List<Medicine> result = medicineService.searchByFormCodeNameShapeColorAndTabletType(formCodeName, shape, color1, tabletType);

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getItemName()).isEqualTo("Aspirin");
        assertThat(result.get(1).getItemName()).isEqualTo("Tylenol");
    }

    @Test
    void searchByItemName_shouldReturnSearchedMedicines() {
        //given
        String itemName = "Tylenol";
        String userId = "user123";

        Medicine medicine1 = Medicine.builder().itemName("Tylenol").build();
        List<Medicine> medicines = Collections.singletonList(medicine1);

        given(medicineRepository.findByItemNameContaining(itemName)).willReturn(medicines);
        willDoNothing().given(searchHistoryService).saveSearchTerm(userId, itemName);

        //when
        List<Medicine> result = medicineService.searchByItemName(itemName, userId);

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemName()).isEqualTo("Tylenol");
        verify(searchHistoryService, times(1)).saveSearchTerm(userId, itemName);
    }

    @Test
    void getMedicineById_shouldReturnMedicineDto() {
        //given
        Long id = 1L;
        Medicine medicine = Medicine.builder().id(id).itemName("Tylenol").build();
        MedicineDto expectedDto = MedicineDto.builder().itemName("Tylenol").build();

        given(medicineRepository.findById(id)).willReturn(Optional.of(medicine));
        given(medicineMapper.toDto(medicine)).willReturn(expectedDto);

        //when
        MedicineDto result = medicineService.getMedicineById(id);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getItemName()).isEqualTo("Tylenol");
    }

    @Test
    void getRecentSearchTerms_shouldReturnRecentSearchTerms() {
        //given
        String userId = "user123";
        List<String> recentSearchTerms = Arrays.asList("Aspirin", "Tylenol");

        given(searchHistoryService.getRecentSearchTerms(userId)).willReturn(recentSearchTerms);

        //when
        List<String> result = medicineService.getRecentSearchTerms(userId);

        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("Aspirin");
        assertThat(result.get(1)).isEqualTo("Tylenol");
    }
}
