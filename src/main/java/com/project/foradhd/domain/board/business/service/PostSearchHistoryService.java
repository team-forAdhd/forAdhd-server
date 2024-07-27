package com.project.foradhd.domain.board.business.service;

import java.util.List;

public interface PostSearchHistoryService {
    void saveSearchTerm(String userId, String term);
    List<String> getRecentSearchTerms(String userId);
}
