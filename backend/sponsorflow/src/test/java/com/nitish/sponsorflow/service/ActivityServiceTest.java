package com.nitish.sponsorflow.service;

import com.nitish.sponsorflow.dto.ActivityRequest;
import com.nitish.sponsorflow.dto.ActivityResponse;
import com.nitish.sponsorflow.entity.Activity;
import com.nitish.sponsorflow.entity.ActivityType;
import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.repository.ActivityRepository;
import com.nitish.sponsorflow.repository.SponsorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private SponsorRepository sponsorRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void createActivity_shouldCreateActivity_whenSponsorExists() {

        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setCompanyName("Nike India");

        ActivityRequest request = new ActivityRequest();

        request.setSponsorId(1L);
        request.setType(ActivityType.CALL);
        request.setDescription(
                "Discussed sponsorship opportunity."
        );
        request.setActivityDate(
                LocalDate.of(2026, 10, 4)
        );
        request.setNextFollowUpDate(
                LocalDate.of(2026, 10, 10)
        );
        when(
                sponsorRepository.findById(1L)
        ).thenReturn(
                java.util.Optional.of(sponsor)
        );
        Activity savedActivity = new Activity();

        savedActivity.setId(1L);
        savedActivity.setSponsor(sponsor);
        savedActivity.setType(ActivityType.CALL);
        savedActivity.setDescription(
                "Discussed sponsorship opportunity."
        );
        savedActivity.setActivityDate(
                LocalDate.of(2026, 10, 4)
        );
        savedActivity.setNextFollowUpDate(
                LocalDate.of(2026, 10, 10)
        );

        when(
                activityRepository.save(
                        org.mockito.ArgumentMatchers.<Activity>any()
                )
        ).thenReturn(
                savedActivity
        );
        ActivityResponse response =
                activityService.createActivity(request);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                1L,
                response.getSponsorId()
        );

        assertEquals(
                "Nike India",
                response.getSponsorCompanyName()
        );

        assertEquals(
                ActivityType.CALL,
                response.getType()
        );

        assertEquals(
                "Discussed sponsorship opportunity.",
                response.getDescription()
        );

        assertEquals(
                LocalDate.of(2026, 10, 4),
                response.getActivityDate()
        );

        assertEquals(
                LocalDate.of(2026, 10, 10),
                response.getNextFollowUpDate()
        );
        org.mockito.Mockito.verify(
                sponsorRepository
        ).findById(1L);

        org.mockito.Mockito.verify(
                activityRepository
        ).save(
                org.mockito.ArgumentMatchers.<Activity>any()
        );
    }
    @Test
    void createActivity_shouldThrowException_whenSponsorDoesNotExist() {

        ActivityRequest request = new ActivityRequest();

        request.setSponsorId(999L);
        request.setType(ActivityType.CALL);
        request.setDescription(
                "Discussed sponsorship opportunity."
        );
        request.setActivityDate(
                LocalDate.of(2026, 10, 4)
        );
        request.setNextFollowUpDate(
                LocalDate.of(2026, 10, 10)
        );

        when(
                sponsorRepository.findById(999L)
        ).thenReturn(
                java.util.Optional.empty()
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                com.nitish.sponsorflow.exception.SponsorNotFoundException.class,
                () -> activityService.createActivity(request)
        );
    }
    @Test
    void getActivitiesBySponsor_shouldReturnActivities() {

        Activity activity = new Activity();

        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setCompanyName("Nike India");

        activity.setId(1L);
        activity.setSponsor(sponsor);
        activity.setType(ActivityType.CALL);
        activity.setDescription(
                "Discussed sponsorship opportunity."
        );
        activity.setActivityDate(
                LocalDate.of(2026, 10, 4)
        );
        activity.setNextFollowUpDate(
                LocalDate.of(2026, 10, 10)
        );

        when(
                activityRepository
                        .findBySponsorIdOrderByActivityDateDesc(1L)
        ).thenReturn(
                java.util.List.of(activity)
        );

        java.util.List<ActivityResponse> responses =
                activityService.getActivitiesBySponsor(1L);

        assertEquals(
                1,
                responses.size()
        );

        assertEquals(
                1L,
                responses.get(0).getId()
        );

        assertEquals(
                1L,
                responses.get(0).getSponsorId()
        );

        assertEquals(
                "Nike India",
                responses.get(0).getSponsorCompanyName()
        );

        assertEquals(
                ActivityType.CALL,
                responses.get(0).getType()
        );

        assertEquals(
                "Discussed sponsorship opportunity.",
                responses.get(0).getDescription()
        );

        assertEquals(
                LocalDate.of(2026, 10, 10),
                responses.get(0).getNextFollowUpDate()
        );
    }
    @Test
    void getActivitiesBySponsor_shouldReturnEmptyList_whenNoActivitiesExist() {

        when(
                activityRepository
                        .findBySponsorIdOrderByActivityDateDesc(1L)
        ).thenReturn(
                java.util.List.of()
        );

        java.util.List<ActivityResponse> responses =
                activityService.getActivitiesBySponsor(1L);

        assertEquals(
                0,
                responses.size()
        );
    }

    @Test
    void getUpcomingFollowUps_shouldReturnFollowUps() {

        Activity activity = new Activity();

        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setCompanyName("Nike India");

        activity.setId(1L);
        activity.setSponsor(sponsor);
        activity.setType(ActivityType.CALL);
        activity.setDescription(
                "Follow up regarding sponsorship proposal."
        );
        activity.setActivityDate(
                LocalDate.of(2026, 10, 4)
        );
        activity.setNextFollowUpDate(
                LocalDate.of(2026, 10, 10)
        );

        when(
                activityRepository
                        .findByNextFollowUpDateIsNotNullOrderByNextFollowUpDateAsc()
        ).thenReturn(
                java.util.List.of(activity)
        );

        java.util.List<ActivityResponse> responses =
                activityService.getUpcomingFollowUps();

        assertEquals(
                1,
                responses.size()
        );

        assertEquals(
                1L,
                responses.get(0).getId()
        );

        assertEquals(
                "Nike India",
                responses.get(0).getSponsorCompanyName()
        );

        assertEquals(
                LocalDate.of(2026, 10, 10),
                responses.get(0).getNextFollowUpDate()
        );
    }
    @Test
    void getUpcomingFollowUps_shouldReturnEmptyList_whenNoFollowUpsExist() {

        when(
                activityRepository
                        .findByNextFollowUpDateIsNotNullOrderByNextFollowUpDateAsc()
        ).thenReturn(
                java.util.List.of()
        );

        java.util.List<ActivityResponse> responses =
                activityService.getUpcomingFollowUps();

        assertEquals(
                0,
                responses.size()
        );
    }
    @Test
    void updateActivity_shouldUpdateActivity_whenActivityExists() {

        Sponsor sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setCompanyName("Nike India");

        Activity existingActivity = new Activity();

        existingActivity.setId(1L);
        existingActivity.setSponsor(sponsor);
        existingActivity.setType(ActivityType.CALL);
        existingActivity.setDescription(
                "Original discussion."
        );
        existingActivity.setActivityDate(
                LocalDate.of(2026, 10, 1)
        );

        ActivityRequest request = new ActivityRequest();

        request.setSponsorId(1L);
        request.setType(ActivityType.MEETING);
        request.setDescription(
                "Updated sponsorship meeting."
        );
        request.setActivityDate(
                LocalDate.of(2026, 10, 5)
        );
        request.setNextFollowUpDate(
                LocalDate.of(2026, 10, 12)
        );

        when(
                activityRepository.findById(1L)
        ).thenReturn(
                java.util.Optional.of(existingActivity)
        );

        when(
                sponsorRepository.findById(1L)
        ).thenReturn(
                java.util.Optional.of(sponsor)
        );

        when(
                activityRepository.save(existingActivity)
        ).thenReturn(
                existingActivity
        );

        ActivityResponse response =
                activityService.updateActivity(
                        1L,
                        request
                );

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                ActivityType.MEETING,
                response.getType()
        );

        assertEquals(
                "Updated sponsorship meeting.",
                response.getDescription()
        );

        assertEquals(
                LocalDate.of(2026, 10, 5),
                response.getActivityDate()
        );

        assertEquals(
                LocalDate.of(2026, 10, 12),
                response.getNextFollowUpDate()
        );

        assertEquals(
                "Nike India",
                response.getSponsorCompanyName()
        );
    }
    @Test
    void updateActivity_shouldThrowException_whenActivityDoesNotExist() {

        ActivityRequest request = new ActivityRequest();

        request.setSponsorId(1L);
        request.setType(ActivityType.MEETING);
        request.setDescription(
                "Updated sponsorship meeting."
        );
        request.setActivityDate(
                LocalDate.of(2026, 10, 5)
        );
        request.setNextFollowUpDate(
                LocalDate.of(2026, 10, 12)
        );

        when(
                activityRepository.findById(999L)
        ).thenReturn(
                java.util.Optional.empty()
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                com.nitish.sponsorflow.exception.ActivityNotFoundException.class,
                () -> activityService.updateActivity(
                        999L,
                        request
                )
        );
    }
    @Test
    void updateActivity_shouldThrowException_whenSponsorDoesNotExist() {

        ActivityRequest request = new ActivityRequest();

        request.setSponsorId(999L);
        request.setType(ActivityType.MEETING);
        request.setDescription(
                "Updated sponsorship meeting."
        );
        request.setActivityDate(
                LocalDate.of(2026, 10, 5)
        );
        request.setNextFollowUpDate(
                LocalDate.of(2026, 10, 12)
        );

        Sponsor sponsor = new Sponsor();
        sponsor.setId(999L);
        sponsor.setCompanyName("Unknown Sponsor");

        Activity existingActivity = new Activity();
        existingActivity.setId(1L);

        when(
                activityRepository.findById(1L)
        ).thenReturn(
                java.util.Optional.of(existingActivity)
        );

        when(
                sponsorRepository.findById(999L)
        ).thenReturn(
                java.util.Optional.empty()
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                com.nitish.sponsorflow.exception.SponsorNotFoundException.class,
                () -> activityService.updateActivity(
                        1L,
                        request
                )
        );
    }
    @Test
    void deleteActivity_shouldDeleteActivity_whenActivityExists() {

        when(
                activityRepository.existsById(1L)
        ).thenReturn(true);

        activityService.deleteActivity(1L);

        org.mockito.Mockito.verify(
                activityRepository
        ).deleteById(1L);
    }
    @Test
    void deleteActivity_shouldThrowException_whenActivityDoesNotExist() {

        when(
                activityRepository.existsById(999L)
        ).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(
                com.nitish.sponsorflow.exception.ActivityNotFoundException.class,
                () -> activityService.deleteActivity(999L)
        );

        org.mockito.Mockito.verify(
                activityRepository,
                org.mockito.Mockito.never()
        ).deleteById(999L);
    }
}