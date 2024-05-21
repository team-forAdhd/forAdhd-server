package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicineBookmarkRepository extends JpaRepository<MedicineBookmark, Long> {
    // 특정 사용자의 모든 활성화된 북마크를 조회
    Page<MedicineBookmark> findAllByUserIdAndDeletedFalse(String userId, Pageable pageable);

    // 특정 사용자가 특정 약에 대해 북마크를 했는지 확인
    Page<Medicine> existsByUserIdAndMedicineId(String userId, Pageable sortedPageable);
}
