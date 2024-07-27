package com.project.foradhd.domain.medicine.business.service.Impl;

import com.project.foradhd.domain.medicine.business.service.MedicineSearchHistoryService;
import com.project.foradhd.domain.medicine.persistence.entity.MedicineSearchHistory;
import com.project.foradhd.domain.medicine.persistence.repository.MedicineSearchHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public void saveSearchTerm(String userId, String term) {
        MedicineSearchHistory searchHistory = MedicineSearchHistory.builder()
                .userId(userId)
                .term(term)
                .searchedAt(LocalDateTime.now())
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearchTerms(String userId) {
        return searchHistoryRepository.findTop10ByUserIdOrderBySearchedAtDesc(userId)
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
