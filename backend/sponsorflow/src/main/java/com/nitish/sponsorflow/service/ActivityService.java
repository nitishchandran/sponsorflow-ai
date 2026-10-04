package com.nitish.sponsorflow.service;

import com.nitish.sponsorflow.dto.ActivityRequest;
import com.nitish.sponsorflow.dto.ActivityResponse;
import com.nitish.sponsorflow.entity.Activity;
import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.exception.ActivityNotFoundException;
import com.nitish.sponsorflow.exception.SponsorNotFoundException;
import com.nitish.sponsorflow.mapper.ActivityMapper;
import com.nitish.sponsorflow.repository.ActivityRepository;
import com.nitish.sponsorflow.repository.SponsorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final SponsorRepository sponsorRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            SponsorRepository sponsorRepository
    ) {
        this.activityRepository = activityRepository;
        this.sponsorRepository = sponsorRepository;
    }

    public ActivityResponse createActivity(
            ActivityRequest request
    ) {
        Sponsor sponsor = sponsorRepository.findById(
                request.getSponsorId()
        ).orElseThrow(() ->
                new SponsorNotFoundException("Sponsor not found")
        );

        Activity activity = ActivityMapper.toEntity(
                request,
                sponsor
        );

        activity.setCreatedAt(
                LocalDateTime.now()
        );

        Activity savedActivity =
                activityRepository.save(activity);

        return ActivityMapper.toResponse(
                savedActivity
        );
    }

    public List<ActivityResponse> getActivitiesBySponsor(
            Long sponsorId
    ) {
        return activityRepository
                .findBySponsorIdOrderByActivityDateDesc(
                        sponsorId
                )
                .stream()
                .map(ActivityMapper::toResponse)
                .toList();
    }

    public List<ActivityResponse> getUpcomingFollowUps() {

        return activityRepository
                .findByNextFollowUpDateIsNotNullOrderByNextFollowUpDateAsc()
                .stream()
                .map(ActivityMapper::toResponse)
                .toList();
    }
    public void deleteActivity(Long activityId) {

        if (!activityRepository.existsById(activityId)) {
            throw new ActivityNotFoundException("Activity not found");
        }

        activityRepository.deleteById(activityId);
    }
    public ActivityResponse updateActivity(
            Long activityId,
            ActivityRequest request
    ) {

        Activity activity = activityRepository.findById(
                activityId
        ).orElseThrow(() ->
                new ActivityNotFoundException("Activity not found")
        );

        Sponsor sponsor = sponsorRepository.findById(
                request.getSponsorId()
        ).orElseThrow(() ->
                new SponsorNotFoundException("Sponsor not found")
        );

        activity.setSponsor(sponsor);
        activity.setType(request.getType());
        activity.setDescription(request.getDescription());
        activity.setActivityDate(request.getActivityDate());
        activity.setNextFollowUpDate(
                request.getNextFollowUpDate()
        );

        Activity updatedActivity =
                activityRepository.save(activity);

        return ActivityMapper.toResponse(
                updatedActivity
        );
    }
}