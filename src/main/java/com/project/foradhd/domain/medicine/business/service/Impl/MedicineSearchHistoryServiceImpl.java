package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineSearchHistoryService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineSearchHistory;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineSearchHistoryRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MedicineSearchHistoryServiceImpl implements MedicineSearchHistoryService {

    private final MedicineSearchHistoryRepository searchHistoryRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void saveSearchTerm(String userId, String term) {
        User user = userService.getUser(userId);
        MedicineSearchHistory searchHistory = MedicineSearchHistory.builder()
                .user(user)
                .term(term)
                .searchedAt(LocalDateTime.now())
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearchTerms(String userId) {
        User user = userService.getUser(userId);
        return searchHistoryRepository.findTop10ByUserOrderBySearchedAtDesc(user)
                .stream()
                .map(MedicineSearchHistory::getTerm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSearchTermById(Long id) {
        searchHistoryRepository.deleteById(id);
    }
}
