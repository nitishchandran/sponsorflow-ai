package com.nitish.sponsorflow.controller;

import com.nitish.sponsorflow.dto.ActivityRequest;
import com.nitish.sponsorflow.dto.ActivityResponse;
import com.nitish.sponsorflow.entity.ActivityType;
import com.nitish.sponsorflow.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityService activityService;

    @Test
    void createActivity_shouldReturn201_whenRequestIsValid()
            throws Exception {

        ActivityResponse response = new ActivityResponse();

        response.setId(1L);
        response.setSponsorId(1L);
        response.setSponsorCompanyName("Nike India");
        response.setType(ActivityType.CALL);
        response.setDescription(
                "Discussed sponsorship opportunity."
        );
        response.setActivityDate(
                LocalDate.of(2026, 9, 28)
        );
        response.setNextFollowUpDate(
                LocalDate.of(2026, 10, 2)
        );

        when(
                activityService.createActivity(
                        any(ActivityRequest.class)
                )
        ).thenReturn(response);

        String requestBody = """
                {
                    "sponsorId": 1,
                    "type": "CALL",
                    "description": "Discussed sponsorship opportunity.",
                    "activityDate": "2026-09-28",
                    "nextFollowUpDate": "2026-10-02"
                }
                """;

        mockMvc.perform(
                        post("/api/activities")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id").value(1)
                )
                .andExpect(
                        jsonPath("$.sponsorId").value(1)
                )
                .andExpect(
                        jsonPath("$.sponsorCompanyName")
                                .value("Nike India")
                )
                .andExpect(
                        jsonPath("$.type")
                                .value("CALL")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value("Discussed sponsorship opportunity.")
                )
                .andExpect(
                        jsonPath("$.nextFollowUpDate")
                                .value("2026-10-02")
                );
    }
    @Test
    void getActivitiesBySponsor_shouldReturn200_whenActivitiesExist()
            throws Exception {

        ActivityResponse activity1 = new ActivityResponse();

        activity1.setId(1L);
        activity1.setSponsorId(1L);
        activity1.setSponsorCompanyName("Nike India");
        activity1.setType(ActivityType.CALL);
        activity1.setDescription(
                "Discussed sponsorship opportunity."
        );
        activity1.setActivityDate(
                LocalDate.of(2026, 9, 28)
        );
        activity1.setNextFollowUpDate(
                LocalDate.of(2026, 10, 2)
        );

        ActivityResponse activity2 = new ActivityResponse();

        activity2.setId(2L);
        activity2.setSponsorId(1L);
        activity2.setSponsorCompanyName("Nike India");
        activity2.setType(ActivityType.MEETING);
        activity2.setDescription(
                "Discussed sponsorship proposal."
        );
        activity2.setActivityDate(
                LocalDate.of(2026, 9, 29)
        );
        activity2.setNextFollowUpDate(
                LocalDate.of(2026, 10, 5)
        );

        when(
                activityService.getActivitiesBySponsor(1L)
        ).thenReturn(
                java.util.List.of(activity1, activity2)
        );

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/activities/sponsor/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(2)
                )
                .andExpect(
                        jsonPath("$[0].id").value(1)
                )
                .andExpect(
                        jsonPath("$[0].sponsorCompanyName")
                                .value("Nike India")
                )
                .andExpect(
                        jsonPath("$[0].type")
                                .value("CALL")
                )
                .andExpect(
                        jsonPath("$[0].nextFollowUpDate")
                                .value("2026-10-02")
                )
                .andExpect(
                        jsonPath("$[1].id").value(2)
                )
                .andExpect(
                        jsonPath("$[1].type")
                                .value("MEETING")
                );
    }
    @Test
    void getActivitiesBySponsor_shouldReturnEmptyList_whenNoActivitiesExist()
            throws Exception {

        when(
                activityService.getActivitiesBySponsor(1L)
        ).thenReturn(
                java.util.List.of()
        );

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/activities/sponsor/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(0)
                );
    }
    @Test
    void getUpcomingFollowUps_shouldReturn200_whenFollowUpsExist()
            throws Exception {

        ActivityResponse followUp = new ActivityResponse();

        followUp.setId(1L);
        followUp.setSponsorId(1L);
        followUp.setSponsorCompanyName("Nike India");
        followUp.setType(ActivityType.CALL);
        followUp.setDescription(
                "Discussed sponsorship opportunity."
        );
        followUp.setActivityDate(
                LocalDate.of(2026, 9, 28)
        );
        followUp.setNextFollowUpDate(
                LocalDate.of(2026, 10, 2)
        );

        when(
                activityService.getUpcomingFollowUps()
        ).thenReturn(
                java.util.List.of(followUp)
        );

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/activities/follow-ups")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(1)
                )
                .andExpect(
                        jsonPath("$[0].id").value(1)
                )
                .andExpect(
                        jsonPath("$[0].sponsorCompanyName")
                                .value("Nike India")
                )
                .andExpect(
                        jsonPath("$[0].nextFollowUpDate")
                                .value("2026-10-02")
                );
    }
    @Test
    void createActivity_shouldReturn400_whenRequestIsInvalid()
            throws Exception {

        String requestBody = """
            {
                "sponsorId": 1,
                "type": "CALL",
                "description": "",
                "activityDate": null,
                "nextFollowUpDate": "2026-10-02"
            }
            """;

        mockMvc.perform(
                        post("/api/activities")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                                .value("Validation failed")
                )
                .andExpect(
                        jsonPath("$.errors.description")
                                .value("Description is required")
                )
                .andExpect(
                        jsonPath("$.errors.activityDate")
                                .value("Activity date is required")
                );
    }
    @Test
    void updateActivity_shouldReturn200_whenRequestIsValid()
            throws Exception {

        ActivityResponse response = new ActivityResponse();

        response.setId(1L);
        response.setSponsorId(1L);
        response.setSponsorCompanyName("Nike India");
        response.setType(ActivityType.MEETING);
        response.setDescription(
                "Updated sponsorship meeting details."
        );
        response.setActivityDate(
                LocalDate.of(2026, 9, 30)
        );
        response.setNextFollowUpDate(
                LocalDate.of(2026, 10, 7)
        );

        when(
                activityService.updateActivity(
                        org.mockito.ArgumentMatchers.eq(1L),
                        any(ActivityRequest.class)
                )
        ).thenReturn(response);

        String requestBody = """
            {
                "sponsorId": 1,
                "type": "MEETING",
                "description": "Updated sponsorship meeting details.",
                "activityDate": "2026-09-30",
                "nextFollowUpDate": "2026-10-07"
            }
            """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/activities/1")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(1)
                )
                .andExpect(
                        jsonPath("$.type").value("MEETING")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value("Updated sponsorship meeting details.")
                )
                .andExpect(
                        jsonPath("$.nextFollowUpDate")
                                .value("2026-10-07")
                );
    }
    @Test
    void deleteActivity_shouldReturn204_whenActivityExists()
            throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/activities/1")
                )
                .andExpect(status().isNoContent());

        org.mockito.Mockito.verify(
                activityService
        ).deleteActivity(1L);
    }
    @Test
    void deleteActivity_shouldReturn404_whenActivityDoesNotExist()
            throws Exception {

        org.mockito.Mockito.doThrow(
                new com.nitish.sponsorflow.exception.ActivityNotFoundException(
                        "Activity not found"
                )
        ).when(activityService).deleteActivity(999L);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/activities/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status").value(404)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Activity not found")
                );
    }
    @Test
    void createActivity_shouldReturn404_whenSponsorDoesNotExist()
            throws Exception {

        org.mockito.Mockito.when(
                activityService.createActivity(
                        any(ActivityRequest.class)
                )
        ).thenThrow(
                new com.nitish.sponsorflow.exception.SponsorNotFoundException(
                        "Sponsor not found"
                )
        );

        String requestBody = """
            {
                "sponsorId": 999,
                "type": "CALL",
                "description": "Discussed sponsorship opportunity.",
                "activityDate": "2026-10-04",
                "nextFollowUpDate": "2026-10-10"
            }
            """;

        mockMvc.perform(
                        post("/api/activities")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status").value(404)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Sponsor not found")
                );
    }
}