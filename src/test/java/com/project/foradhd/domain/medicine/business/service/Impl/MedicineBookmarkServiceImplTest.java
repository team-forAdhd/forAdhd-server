package com.project.foradhd.domain.medicine.business.service.Impl;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import com.project.foradhd.domain.medicine.persistence.entity.Medicine;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineBookmarkRepository;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicineBookmarkServiceImplTest {

    @Mock
    private MedicineBookmarkRepository bookmarkRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MedicineBookmarkServiceImpl bookmarkService;

    private User user;
    private Medicine medicine;
    private MedicineBookmark bookmark;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user1")
                .build();

        medicine = Medicine.builder()
                .id(1L)
                .build();

        bookmark = MedicineBookmark.builder()
                .user(user)
                .medicine(medicine)
                .deleted(false)
                .build();
    }

    @Test
    void toggleBookmark_ShouldCreateBookmark() {
        // given
        given(userService.getUser("user1")).willReturn(user);
        given(medicineRepository.findById(1L)).willReturn(Optional.of(medicine));
        given(bookmarkRepository.existsByUserIdAndMedicineId("user1", 1L)).willReturn(false);
        given(bookmarkRepository.save(any(MedicineBookmark.class))).willReturn(bookmark);

        // when
        bookmarkService.toggleBookmark("user1", 1L);

        // then
        then(bookmarkRepository).should().save(any(MedicineBookmark.class));
    }

    @Test
    void toggleBookmark_ShouldDeleteBookmark() {
        // given
        given(userService.getUser("user1")).willReturn(user);
        given(medicineRepository.findById(1L)).willReturn(Optional.of(medicine));
        given(bookmarkRepository.existsByUserIdAndMedicineId("user1", 1L)).willReturn(true);
        willDoNothing().given(bookmarkRepository).deleteByUserIdAndMedicineId("user1", 1L);

        // when
        bookmarkService.toggleBookmark("user1", 1L);

        // then
        then(bookmarkRepository).should().deleteByUserIdAndMedicineId("user1", 1L);
    }

    @Test
    void toggleBookmark_ShouldThrowException_WhenUserNotFound() {
        // given
        given(userService.getUser("user1")).willReturn(null);

        // when
        Throwable thrown = catchThrowable(() -> bookmarkService.toggleBookmark("user1", 1L));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    void toggleBookmark_ShouldThrowException_WhenMedicineNotFound() {
        // given
        given(userService.getUser("user1")).willReturn(user);
        given(medicineRepository.findById(1L)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> bookmarkService.toggleBookmark("user1", 1L));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_MEDICINE.getMessage());
    }

    @Test
    void getBookmarksByUser_ShouldReturnPageOfBookmarks() {
        // given
        Pageable pageable = Pageable.unpaged();
        Page<MedicineBookmark> bookmarkPage = new PageImpl<>(Collections.singletonList(bookmark));
        given(bookmarkRepository.findByUserIdAndDeletedIsFalse("user1", pageable)).willReturn(bookmarkPage);

        // when
        Page<MedicineBookmark> result = bookmarkService.getBookmarksByUser("user1", pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getMedicine().getId()).isEqualTo(1L);
    }
}
