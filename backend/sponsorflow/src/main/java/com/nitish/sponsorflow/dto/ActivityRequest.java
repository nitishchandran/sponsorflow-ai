package com.nitish.sponsorflow.dto;

import com.nitish.sponsorflow.entity.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityRequest {

    @NotNull(message = "Sponsor ID is required")
    private Long sponsorId;

    @NotNull(message = "Activity type is required")
    private ActivityType type;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Activity date is required")
    private LocalDate activityDate;

    private LocalDate nextFollowUpDate;
}