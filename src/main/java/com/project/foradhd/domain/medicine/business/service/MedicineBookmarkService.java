package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicineBookmarkService {
    void toggleBookmark(String userId, Long medicineId);
    Page<MedicineBookmark> getBookmarksByUser(String userId, Pageable pageable);
}
