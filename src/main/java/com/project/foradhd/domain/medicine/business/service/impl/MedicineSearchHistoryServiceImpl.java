package com.project.foradhd.domain.medicine.business.service.impl;

import com.project.foradhd.domain.medicine.business.service.MedicineSearchHistoryService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineSearchHistory;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineSearchHistoryRepository;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
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
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearchTerms(String userId) {
        User user = userService.getUser(userId);
        return searchHistoryRepository.findTop10ByUserOrderByCreatedAtDesc(user)
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
