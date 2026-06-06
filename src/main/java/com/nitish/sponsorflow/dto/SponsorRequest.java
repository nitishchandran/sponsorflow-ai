package com.nitish.sponsorflow.dto;

import com.nitish.sponsorflow.entity.SponsorStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SponsorRequest {
    @NotBlank(message="Company name is required")
    private String companyName;

    @NotBlank(message="Contact Person is required")
    private String contactPerson;

    private String designation;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String industry;

    private String notes;

    private SponsorStatus status;
}
