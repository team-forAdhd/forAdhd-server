package com.project.foradhd.domain.medicine.web.controller;

import com.project.foradhd.domain.medicine.business.service.MedicineBookmarkService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineBookmark;
import com.project.foradhd.domain.medicine.web.dto.response.MedicineBookmarkResponse;
import com.project.foradhd.domain.medicine.web.mapper.MedicineMapper;
import com.project.foradhd.global.AuthUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
public class MedicineBookmarkController {
    @Autowired
    private MedicineBookmarkService bookmarkService;

    @Autowired
    private MedicineMapper medicineMapper;

    @GetMapping("/my")
    public ResponseEntity<Page<MedicineBookmarkResponse>> getMyBookmarks(
            @AuthUserId Long userId,
            @RequestParam(defaultValue = "newest") String sort,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Sort.Direction direction = sort.equals("oldest") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "createdAt"));

        Page<MedicineBookmark> bookmarks = (Page<MedicineBookmark>) bookmarkService.findBookmarksByUserId(userId, sortedPageable);
        Page<MedicineBookmarkResponse> bookmarkDtos = bookmarks.map(bookmark -> medicineMapper.medicineBookmarkToMedicineBookmarkResponseDto(bookmark));

        return ResponseEntity.ok(bookmarkDtos);
    }
}
