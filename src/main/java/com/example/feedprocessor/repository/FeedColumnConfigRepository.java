package com.example.feedprocessor.repository;

import com.example.feedprocessor.entity.FeedColumnConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedColumnConfigRepository extends JpaRepository<FeedColumnConfig, Long> {
    
    List<FeedColumnConfig> findByFeedConfigId(Long feedConfigId);
    
    List<FeedColumnConfig> findByFeedConfigName(String feedConfigName);
}
