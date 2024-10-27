package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.PostSearchHistoryService;
import com.project.foradhd.domain.board.persistence.entity.PostSearchHistory;
import com.project.foradhd.domain.board.persistence.repository.PostSearchHistoryRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostSearchHistoryServiceImpl implements PostSearchHistoryService {

    private final PostSearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveSearchTerm(String userId, String term) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        PostSearchHistory searchHistory = PostSearchHistory.builder()
                .user(user)
                .term(term)
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<String> getRecentSearchTerms(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        return searchHistoryRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(PostSearchHistory::getTerm)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSearchTermById(Long id) {
        searchHistoryRepository.deleteById(id);
    }
}