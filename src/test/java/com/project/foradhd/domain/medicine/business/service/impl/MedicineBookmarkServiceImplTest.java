package com.project.foradhd.domain.medicine.business.service.impl;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.mockito.ArgumentCaptor;
class MedicineBookmarkServiceImplTest {

    @Mock
    private MedicineBookmarkRepository bookmarkRepository;

    @Mock
    private UserService userService;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineBookmarkServiceImpl bookmarkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleBookmark_shouldAddBookmarkIfNotExists() {
        // given
        String userId = "user123";
        Long medicineId = 1L;

        User user = User.builder().id(userId).build();
        Medicine medicine = Medicine.builder().id(medicineId).build();

        given(userService.getUser(userId)).willReturn(user);
        given(medicineRepository.findById(medicineId)).willReturn(Optional.of(medicine));
        given(bookmarkRepository.existsByUserIdAndMedicineId(userId, medicineId)).willReturn(false);

        // when
        bookmarkService.toggleBookmark(userId, medicineId);

        // then
        ArgumentCaptor<MedicineBookmark> bookmarkCaptor = ArgumentCaptor.forClass(MedicineBookmark.class);
        verify(bookmarkRepository).save(bookmarkCaptor.capture());

        MedicineBookmark savedBookmark = bookmarkCaptor.getValue();
        assertThat(savedBookmark.getUser()).isEqualTo(user);
        assertThat(savedBookmark.getMedicine()).isEqualTo(medicine);
        assertThat(savedBookmark.getDeleted()).isEqualTo(Boolean.FALSE);
    }

    @Test
    void toggleBookmark_shouldRemoveBookmarkIfExists() {
        // given
        String userId = "user123";
        Long medicineId = 1L;

        User user = User.builder().id(userId).build();
        Medicine medicine = Medicine.builder().id(medicineId).build();

        given(userService.getUser(userId)).willReturn(user);
        given(medicineRepository.findById(medicineId)).willReturn(Optional.of(medicine));
        given(bookmarkRepository.existsByUserIdAndMedicineId(userId, medicineId)).willReturn(true);

        // when
        bookmarkService.toggleBookmark(userId, medicineId);

        // then
        verify(bookmarkRepository, times(1)).deleteByUserIdAndMedicineId(userId, medicineId);
    }

    @Test
    void getBookmarksByUser_shouldReturnBookmarksPage() {
        // given
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);

        MedicineBookmark bookmark1 = MedicineBookmark.builder().id(1L).build();
        MedicineBookmark bookmark2 = MedicineBookmark.builder().id(2L).build();
        Page<MedicineBookmark> bookmarkPage = new PageImpl<>(Arrays.asList(bookmark1, bookmark2), pageable, 2);

        given(bookmarkRepository.findByUserIdAndDeletedIsFalse(userId, pageable)).willReturn(bookmarkPage);

        // when
        Page<MedicineBookmark> result = bookmarkService.getBookmarksByUser(userId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(bookmarkRepository, times(1)).findByUserIdAndDeletedIsFalse(userId, pageable);
    }
}
