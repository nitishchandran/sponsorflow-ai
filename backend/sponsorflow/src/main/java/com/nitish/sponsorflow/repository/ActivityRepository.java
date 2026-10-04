package com.nitish.sponsorflow.repository;

import com.nitish.sponsorflow.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findBySponsorIdOrderByActivityDateDesc(Long sponsorId);
    List<Activity> findByNextFollowUpDateIsNotNullOrderByNextFollowUpDateAsc();
}