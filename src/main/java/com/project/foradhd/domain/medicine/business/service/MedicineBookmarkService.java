package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicineBookmarkService {
    MedicineBookmark saveBookmark(MedicineBookmark bookmark);
    void deleteBookmark(Long id);
    List<MedicineBookmark> findBookmarksByUserId(Long userId, Pageable sortedPageable);

    @Transactional(readOnly = true)
    Page<MedicineBookmark> findBookmarksByUserId(String userId, Pageable pageable);
}
