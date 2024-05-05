package com.project.foradhd.domain.board.persistence.repository;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralBoardRepository extends JpaRepository<GeneralPost, String>{
    List<GeneralPost> findScrapedByUserId(String userId);
    List<GeneralPost> findByWriterId(String writerId);

}
