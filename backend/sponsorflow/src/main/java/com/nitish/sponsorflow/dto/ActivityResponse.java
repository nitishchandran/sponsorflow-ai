package com.nitish.sponsorflow.dto;

import com.nitish.sponsorflow.entity.ActivityType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ActivityResponse {

    private Long id;

    private Long sponsorId;

    private String sponsorCompanyName;

    private ActivityType type;

    private String description;

    private LocalDate activityDate;

    private LocalDate nextFollowUpDate;

    private LocalDateTime createdAt;
}