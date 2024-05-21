package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineBookmarkService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicineBookmarkServiceImpl implements MedicineBookmarkService {
    @Autowired
    private MedicineBookmarkRepository bookmarkRepository;

    @Override
    @Transactional
    public MedicineBookmark saveBookmark(MedicineBookmark bookmark) {
        boolean exists = bookmarkRepository.existsByUserIdAndMedicineId(bookmark.getUser().getId(), bookmark.getMedicine().getId());
        if (exists) {
            throw new IllegalStateException("Bookmark already exists for this medicine and user.");
        }
        return bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        MedicineBookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark with id " + bookmarkId + " not found."));
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public List<MedicineBookmark> findBookmarksByUserId(Long userId, Pageable sortedPageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicineBookmark> findBookmarksByUserId(String userId, Pageable pageable) {
        return bookmarkRepository.findAllByUserIdAndDeletedFalse(userId, pageable);
    }
}
