package com.groyouth.backend.repository;

import com.groyouth.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("""
            SELECT j FROM Job j WHERE
            (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
               AND (:location IS NULL OR LOWER(j.location) = LOWER(:location))
               AND (:exp IS NULL OR j.experienceRequired >= :exp)
            """)
    List<Job> search(@Param("keyword") String keyword,
                     @Param("location") String location,
                     @Param("exp") Integer exp);
}
