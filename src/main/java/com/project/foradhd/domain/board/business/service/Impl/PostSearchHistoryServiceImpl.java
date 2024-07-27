package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostSearchHistoryService;
import com.project.foradhd.domain.board.persistence.entity.PostSearchHistory;
import com.project.foradhd.domain.board.persistence.repository.PostSearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchHistoryServiceImpl implements PostSearchHistoryService {

    private final PostSearchHistoryRepository searchHistoryRepository;

    @Override
    @Transactional
    public void saveSearchTerm(String userId, String term) {
        PostSearchHistory searchHistory = PostSearchHistory.builder()
                .userId(userId)
                .term(term)
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearchTerms(String userId) {
        return searchHistoryRepository.findByUserIdOrderBySearchTimeDesc(userId).stream()
                .map(PostSearchHistory::getTerm)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteSearchTermById(Long id) {
        searchHistoryRepository.deleteById(id);
    }
}
