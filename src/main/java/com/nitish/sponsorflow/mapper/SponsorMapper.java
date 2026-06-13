package com.nitish.sponsorflow.mapper;

import com.nitish.sponsorflow.dto.SponsorRequest;
import com.nitish.sponsorflow.dto.SponsorResponse;
import com.nitish.sponsorflow.entity.Sponsor;
import org.springframework.stereotype.Component;

@Component
public class SponsorMapper {

    public SponsorResponse toResponse(
            Sponsor sponsor) {

        SponsorResponse response =
                new SponsorResponse();

        response.setId(sponsor.getId());
        response.setCompanyName(
                sponsor.getCompanyName());
        response.setContactPerson(
                sponsor.getContactPerson());
        response.setDesignation(
                sponsor.getDesignation());
        response.setEmail(
                sponsor.getEmail());
        response.setPhone(
                sponsor.getPhone());
        response.setIndustry(
                sponsor.getIndustry());
        response.setNotes(
                sponsor.getNotes());
        response.setStatus(
                sponsor.getStatus());
        response.setCreatedAt(
                sponsor.getCreatedAt());

        return response;
    }

    public Sponsor toEntity(
            SponsorRequest request) {

        Sponsor sponsor =
                new Sponsor();

        sponsor.setCompanyName(
                request.getCompanyName());
        sponsor.setContactPerson(
                request.getContactPerson());
        sponsor.setDesignation(
                request.getDesignation());
        sponsor.setEmail(
                request.getEmail());
        sponsor.setPhone(
                request.getPhone());
        sponsor.setIndustry(
                request.getIndustry());
        sponsor.setNotes(
                request.getNotes());
        sponsor.setStatus(
                request.getStatus());

        return sponsor;
    }
}