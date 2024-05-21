package com.project.foradhd.domain.medicine.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.project.foradhd.domain.medicine.business.service.MedicineService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.enums.Color;
import com.project.foradhd.domain.medicine.persistence.enums.DosageForm;
import com.project.foradhd.domain.medicine.persistence.enums.Shape;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

@WebMvcTest(MedicineController.class)
@ExtendWith(SpringExtension.class)
public class MedicineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineService medicineService;

    @Test
    public void testSearchMedicinesEndpoint() throws Exception {
        // Given
        Medicine medicine = new Medicine(); // Set up necessary fields if needed
        PageImpl<Medicine> page = new PageImpl<>(Arrays.asList(medicine));
        given(medicineService.searchMedicines(DosageForm.TABLET, Shape.CIRCULAR, Color.WHITE, PageRequest.of(0, 10)))
                .willReturn(page);

        // When & Then
        mockMvc.perform(get("/api/medicines/search")
                        .param("dosageForm", "TABLET")
                        .param("shape", "CIRCULAR")
                        .param("color", "WHITE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value(medicine.getName())); // Ensure JSON path matches expected results

        then(medicineService).should().searchMedicines(DosageForm.TABLET, Shape.CIRCULAR, Color.WHITE, PageRequest.of(0, 10));
    }
}
