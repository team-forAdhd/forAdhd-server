package com.project.foradhd.domain.medicine.business.service;

import java.util.List;

public interface MedicineSearchHistoryService {
    void saveSearchTerm(String userId, String term);
    List<String> getRecentSearchTerms(String userId);
    void deleteSearchTermById(Long id);
}