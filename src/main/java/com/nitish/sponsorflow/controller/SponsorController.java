package com.nitish.sponsorflow.controller;

import com.nitish.sponsorflow.dto.DashboardStatsResponse;
import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.entity.SponsorStatus;
import com.nitish.sponsorflow.service.SponsorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {
    @Autowired
    private SponsorService sponsorService;
    @PostMapping
    public Sponsor createSponsor(
            @Valid @RequestBody Sponsor sponsor){
        return sponsorService.createSponsor(sponsor);
    }
    @GetMapping
    public List<Sponsor> getAllSponsors(){
        return sponsorService.getAllSponsors();
    }
    @GetMapping("/{id}")
    public Sponsor getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
    }

    @GetMapping("/search/company/{companyName}")
    public List<Sponsor> searchByCompanyName(
            @PathVariable String companyName) {

        return sponsorService
                .searchByCompanyName(companyName);
    }
    @GetMapping("/search/status/{status}")
    public List<Sponsor> searchByStatus(
            @PathVariable SponsorStatus status) {

        return sponsorService.searchByStatus(status);
    }
    @GetMapping("/search/industry/{industry}")
    public List<Sponsor> searchByIndustry(
            @PathVariable String industry) {

        return sponsorService
                .searchByIndustry(industry);
    }
    @GetMapping("/sort/{field}")
    public List<Sponsor> getSponsorsSorted(
            @PathVariable String field) {

        return sponsorService
                .getSponsorsSorted(field);
    }
    @GetMapping("/page")
    public Page<Sponsor> getSponsorsPaginated(
            @RequestParam int page,
            @RequestParam int size) {

        return sponsorService
                .getSponsorsPaginated(page, size);
    }
    @PutMapping("/{id}")
    public Sponsor updateSponsor(
            @PathVariable Long id,
            @RequestBody Sponsor sponsor) {

        return sponsorService
                .updateSponsor(id, sponsor);
    }
    @GetMapping("/dashboard/count")
    public long getTotalSponsorCount() {

        return sponsorService
                .getTotalSponsorCount();

    }
    @GetMapping("/dashboard/count/{status}")
    public long getSponsorCountByStatus(
            @PathVariable SponsorStatus status) {

        return sponsorService
                .getSponsorCountByStatus(status);
    }
    @GetMapping("/dashboard/stats")
    public DashboardStatsResponse getDashboardStats() {

        return sponsorService
                .getDashboardStats();
    }
    @GetMapping("/dashboard/industry-count")
    public Map<String, Long> getIndustryCounts() {

        return sponsorService
                .getIndustryCounts();
    }
}
