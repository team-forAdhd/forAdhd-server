package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineBookmarkService;
import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineBookmarkServiceImpl implements MedicineBookmarkService {
    @Autowired
    private MedicineBookmarkRepository bookmarkRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    @Transactional
    public void toggleBookmark(String userId, Long medicineId) {
        User user = userService.getUser(userId);
        Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);

        if (optionalMedicine.isPresent()) {
            Medicine medicine = optionalMedicine.get();

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
        } else {
            throw new IllegalArgumentException("Invalid medicineId: " + medicineId);
        }
    }

    @Override
    public Page<MedicineBookmark> getBookmarksByUser(String userId, Pageable pageable) {
        return bookmarkRepository.findByUserIdAndDeletedIsFalse(userId, pageable);
    }
}