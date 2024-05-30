package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;

import java.util.List;

public interface MedicineBookmarkService {
    void toggleBookmark(String userId, Long medicineId);
    List<MedicineBookmark> getBookmarksByUser(String userId);
}
