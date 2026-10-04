package com.nitish.sponsorflow.controller;

import com.nitish.sponsorflow.dto.ActivityRequest;
import com.nitish.sponsorflow.dto.ActivityResponse;
import com.nitish.sponsorflow.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "http://localhost:5173")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(
            ActivityService activityService
    ) {
        this.activityService = activityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse createActivity(
            @Valid @RequestBody ActivityRequest request
    ) {
        return activityService.createActivity(request);
    }

    @GetMapping("/sponsor/{sponsorId}")
    public List<ActivityResponse> getActivitiesBySponsor(
            @PathVariable Long sponsorId
    ) {
        return activityService.getActivitiesBySponsor(
                sponsorId
        );
    }

    @GetMapping("/follow-ups")
    public List<ActivityResponse> getUpcomingFollowUps() {
        return activityService.getUpcomingFollowUps();
    }

    @PutMapping("/{activityId}")
    public ActivityResponse updateActivity(
            @PathVariable Long activityId,
            @Valid @RequestBody ActivityRequest request
    ) {
        return activityService.updateActivity(
                activityId,
                request
        );
    }
    @DeleteMapping("/{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(
            @PathVariable Long activityId
    ) {
        activityService.deleteActivity(activityId);
    }
}