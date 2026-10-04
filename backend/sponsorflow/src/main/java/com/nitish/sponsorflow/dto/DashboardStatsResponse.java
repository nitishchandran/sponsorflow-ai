package com.nitish.sponsorflow.dto;

import lombok.Data;

@Data
public class DashboardStatsResponse {
    private long totalSponsors;

    private long leadSponsors;

    private long meetingSponsors;

    private long wonSponsors;

    private long lostSponsors;
}
