package com.nitish.sponsorflow.controller;

import com.nitish.sponsorflow.dto.SponsorRequest;
import com.nitish.sponsorflow.dto.SponsorResponse;
import com.nitish.sponsorflow.entity.SponsorStatus;
import com.nitish.sponsorflow.exception.SponsorNotFoundException;
import com.nitish.sponsorflow.service.SponsorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SponsorController.class)
class SponsorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SponsorService sponsorService;

    @Test
    void getSponsorById_shouldReturn404_whenSponsorDoesNotExist()
            throws Exception {

        when(sponsorService.getSponsorResponseById(999999L))
                .thenThrow(
                        new SponsorNotFoundException(
                                "Sponsor not found with id: 999999"
                        )
                );

        mockMvc.perform(
                        get("/api/sponsors/999999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status").value(404)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Sponsor not found with id: 999999")
                );
    }
    @Test
    void getSponsorById_shouldReturn200_whenSponsorExists()
            throws Exception {

        SponsorResponse response = new SponsorResponse();

        response.setId(1L);
        response.setCompanyName("Nike India");
        response.setContactPerson("John Doe");
        response.setDesignation("Marketing Manager");
        response.setEmail("john@nike.com");
        response.setPhone("9876543210");
        response.setIndustry("Sports");
        response.setNotes("Potential sponsor");

        when(sponsorService.getSponsorResponseById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/sponsors/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(1)
                )
                .andExpect(
                        jsonPath("$.companyName")
                                .value("Nike India")
                )
                .andExpect(
                        jsonPath("$.contactPerson")
                                .value("John Doe")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("john@nike.com")
                );
    }
    @Test
    void createSponsor_shouldReturn201_whenRequestIsValid()
            throws Exception {

        SponsorResponse response = new SponsorResponse();

        response.setId(10L);
        response.setCompanyName("Adidas India");
        response.setContactPerson("Rahul Sharma");
        response.setDesignation("Marketing Manager");
        response.setEmail("rahul@adidas.com");
        response.setPhone("9876543210");
        response.setIndustry("Sports");
        response.setNotes("Potential football sponsor");
        response.setStatus(SponsorStatus.LEAD);

        when(sponsorService.createSponsor(
                org.mockito.ArgumentMatchers.any(SponsorRequest.class)
        )).thenReturn(response);

        String requestBody = """
            {
                "companyName": "Adidas India",
                "contactPerson": "Rahul Sharma",
                "designation": "Marketing Manager",
                "email": "rahul@adidas.com",
                "phone": "9876543210",
                "industry": "Sports",
                "notes": "Potential football sponsor",
                "status": "LEAD"
            }
            """;

        mockMvc.perform(
                        post("/api/sponsors")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id").value(10)
                )
                .andExpect(
                        jsonPath("$.companyName")
                                .value("Adidas India")
                )
                .andExpect(
                        jsonPath("$.contactPerson")
                                .value("Rahul Sharma")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("rahul@adidas.com")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("LEAD")
                );
    }
    @Test
    void createSponsor_shouldReturn400_whenRequestIsInvalid()
            throws Exception {

        String requestBody = """
            {
                "companyName": "",
                "contactPerson": "",
                "designation": "Marketing Manager",
                "email": "invalid-email",
                "phone": "9876543210",
                "industry": "Technology",
                "notes": "Validation test"
            }
            """;

        mockMvc.perform(
                        post("/api/sponsors")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Validation failed")
                )
                .andExpect(
                        jsonPath("$.errors.companyName")
                                .value("Company name is required")
                )
                .andExpect(
                        jsonPath("$.errors.contactPerson")
                                .value("Contact Person is required")
                )
                .andExpect(
                        jsonPath("$.errors.email")
                                .value("Invalid email format")
                );
    }
    @Test
    void deleteSponsor_shouldReturn204_whenSponsorExists()
            throws Exception {

        mockMvc.perform(
                        delete("/api/sponsors/1")
                )
                .andExpect(status().isNoContent());
    }
    @Test
    void updateSponsor_shouldReturn200_whenSponsorExists()
            throws Exception {

        SponsorResponse response = new SponsorResponse();

        response.setId(1L);
        response.setCompanyName("Nike India Updated");
        response.setContactPerson("John Doe");
        response.setDesignation("Sponsorship Manager");
        response.setEmail("john@nike.com");
        response.setPhone("9876543210");
        response.setIndustry("Sports");
        response.setNotes("Updated sponsor details");
        response.setStatus(SponsorStatus.CONTACTED);

        when(
                sponsorService.updateSponsor(
                        org.mockito.ArgumentMatchers.eq(1L),
                        org.mockito.ArgumentMatchers.any(
                                com.nitish.sponsorflow.entity.Sponsor.class
                        )
                )
        ).thenReturn(
                new com.nitish.sponsorflow.entity.Sponsor()
        );

        String requestBody = """
            {
                "companyName": "Nike India Updated",
                "contactPerson": "John Doe",
                "designation": "Sponsorship Manager",
                "email": "john@nike.com",
                "phone": "9876543210",
                "industry": "Sports",
                "notes": "Updated sponsor details",
                "status": "CONTACTED"
            }
            """;

        mockMvc.perform(
                        put("/api/sponsors/1")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isOk());
    }
}