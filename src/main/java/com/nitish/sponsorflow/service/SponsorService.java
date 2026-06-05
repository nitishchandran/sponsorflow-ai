package com.nitish.sponsorflow.service;

import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SponsorService {
    @Autowired
    private SponsorRepository sponsorRepository;

    public Sponsor createSponsor(Sponsor sponsor){

        return sponsorRepository.save(sponsor);
    }
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }
    public Optional<Sponsor> getSponsorById(Long id) {
        return sponsorRepository.findById(id);
    }
    public void deleteSponsor(Long id){
        sponsorRepository.deleteById(id);
    }
}
