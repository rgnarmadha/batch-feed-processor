package com.example.feedprocessor.repository;

import com.example.feedprocessor.entity.FeedConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedConfigRepository extends JpaRepository<FeedConfig, Long> {
    
    Optional<FeedConfig> findByName(String name);
    
    List<FeedConfig> findByEnabledTrue();
    
    @Query("SELECT fc FROM FeedConfig fc LEFT JOIN FETCH fc.columns WHERE fc.enabled = true")
    List<FeedConfig> findEnabledFeedsWithColumns();
    
    @Query("SELECT fc FROM FeedConfig fc LEFT JOIN FETCH fc.columns WHERE fc.name = :name")
    Optional<FeedConfig> findByNameWithColumns(String name);
}
