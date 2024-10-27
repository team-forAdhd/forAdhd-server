package com.project.foradhd.domain.medicine.business.service.impl;

import com.project.foradhd.domain.medicine.business.service.MedicineBookmarkService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MedicineBookmarkServiceImpl implements MedicineBookmarkService {

    private final UserService userService;
    private final MedicineBookmarkRepository bookmarkRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public void toggleBookmark(String userId, Long medicineId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEDICINE));

        boolean exists = bookmarkRepository.existsByUserIdAndMedicineId(userId, medicineId);
        if (exists) {
            bookmarkRepository.deleteByUserIdAndMedicineId(userId, medicineId);
        } else {
            MedicineBookmark bookmark = MedicineBookmark.builder()
                    .user(user)
                    .medicine(medicine)
                    .deleted(false)
                    .build();
            bookmarkRepository.save(bookmark);
        }
    }

    @Override
    public Page<MedicineBookmark> getBookmarksByUser(String userId, Pageable pageable) {
        return bookmarkRepository.findByUserIdAndDeletedIsFalse(userId, pageable);
    }
}
