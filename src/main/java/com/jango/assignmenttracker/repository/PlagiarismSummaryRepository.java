package com.jango.assignmenttracker.repository;

import com.jango.assignmenttracker.model.PlagiarismSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlagiarismSummaryRepository extends JpaRepository<PlagiarismSummary, Long> {
    List<PlagiarismSummary> findByUserId(Long userId);
    List<PlagiarismSummary> findByUserEmail(String userEmail);
}
