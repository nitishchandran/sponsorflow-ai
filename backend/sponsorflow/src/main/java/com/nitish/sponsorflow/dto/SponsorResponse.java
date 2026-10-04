package com.nitish.sponsorflow.dto;

import com.nitish.sponsorflow.entity.SponsorStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SponsorResponse {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String designation;
    private String email;
    private String phone;
    private String industry;
    private String notes;
    private SponsorStatus status;
    private LocalDateTime createdAt;
}
