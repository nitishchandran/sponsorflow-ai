package com.nitish.sponsorflow.controller;

import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {
    @Autowired
    private SponsorService sponsorService;
    @PostMapping
    public Sponsor createSponsor(@RequestBody Sponsor sponsor){
        return sponsorService.createSponsor(sponsor);
    }
    @GetMapping
    public List<Sponsor> getAllSponsors(){
        return sponsorService.getAllSponsors();
    }
    @GetMapping("/{id}")
    public Sponsor getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id)
                .orElseThrow();
    }
    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
    }
}
