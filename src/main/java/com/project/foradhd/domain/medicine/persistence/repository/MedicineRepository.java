package com.project.foradhd.domain.medicine.persistence.repository;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findAllByOrderByItemNameAsc();
    List<Medicine> findAllByOrderByRatingDesc();
    List<Medicine> findAllByOrderByRatingAsc();
    List<Medicine> findByItemNameContainingOrderByItemNameAsc(String ingredient);
    List<Medicine> findAllByFormCodeNameOrDrugShapeOrColorClass1(String formCodeName, String drugShape, String color1);
    List<Medicine> findByItemNameContaining(String itemName);
    MedicineDto getMedicineById(Long id);
    List<Medicine> findAllByIngredientTypeOrderByItemNameAsc(int ingredientType);
    List<Medicine> findAllByFormCodeNameOrDrugShapeOrColorClass1OrTabletType(
            String formCodeName, String drugShape, String color1, int tabletType
    );
    @Query("SELECT m FROM Medicine m JOIN MedicineBookmark mb ON m.id = mb.medicine.id WHERE mb.user.id = :userId AND mb.deleted = false")
    List<Medicine> findMedicinesByUserFavorites(@Param("userId") String userId);

}
