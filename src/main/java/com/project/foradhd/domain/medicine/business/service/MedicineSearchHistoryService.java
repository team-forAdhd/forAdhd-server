package com.project.foradhd.domain.medicine.business.service;

import com.project.foradhd.domain.medicine.persistence.entity.MedicineSearchHistory;

import java.util.List;

public interface MedicineSearchHistoryService {
    void saveSearchTerm(String userId, String term);
    List<String> getRecentSearchTerms(String userId);
}