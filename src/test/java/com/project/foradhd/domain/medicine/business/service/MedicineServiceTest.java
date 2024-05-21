package com.project.foradhd.domain.medicine.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
public class MedicineServiceTest {
    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineService medicineService;

    @Test
    public void testSearchMedicines() {
        // Given
        Medicine medicine = new Medicine();
        Page<Medicine> expectedMedicines = new PageImpl<>(Arrays.asList(medicine));
        Pageable pageable = PageRequest.of(0, 10);
        given(medicineRepository.findByDosageFormAndShapeAndColor(DosageForm.TABLET, Shape.CIRCULAR, Color.WHITE, pageable))
                .willReturn(expectedMedicines);

        // When
        Page<Medicine> actualMedicines = medicineService.searchMedicines(DosageForm.TABLET, Shape.CIRCULAR, Color.WHITE, pageable);

        // Then
        then(medicineRepository).should().findByDosageFormAndShapeAndColor(DosageForm.TABLET, Shape.CIRCULAR, Color.WHITE, pageable);
        assertEquals(expectedMedicines, actualMedicines);
    }
}
