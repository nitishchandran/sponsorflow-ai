package com.nitish.sponsorflow.mapper;

import com.nitish.sponsorflow.dto.ActivityRequest;
import com.nitish.sponsorflow.dto.ActivityResponse;
import com.nitish.sponsorflow.entity.Activity;
import com.nitish.sponsorflow.entity.Sponsor;

public class ActivityMapper {

    public static Activity toEntity(
            ActivityRequest request,
            Sponsor sponsor
    ) {
        Activity activity = new Activity();

        activity.setSponsor(sponsor);
        activity.setType(request.getType());
        activity.setDescription(request.getDescription());
        activity.setActivityDate(request.getActivityDate());
        activity.setNextFollowUpDate(
                request.getNextFollowUpDate()
        );

        return activity;
    }

    public static ActivityResponse toResponse(
            Activity activity
    ) {
        ActivityResponse response = new ActivityResponse();

        response.setId(activity.getId());
        response.setSponsorId(activity.getSponsor().getId());
        response.setSponsorCompanyName(
                activity.getSponsor().getCompanyName()
        );
        response.setType(activity.getType());
        response.setDescription(activity.getDescription());
        response.setActivityDate(activity.getActivityDate());
        response.setNextFollowUpDate(
                activity.getNextFollowUpDate()
        );
        response.setCreatedAt(activity.getCreatedAt());

        return response;
    }
}