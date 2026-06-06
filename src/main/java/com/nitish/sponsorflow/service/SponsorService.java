package com.nitish.sponsorflow.service;

import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.entity.SponsorStatus;
import com.nitish.sponsorflow.exception.SponsorNotFoundException;
import com.nitish.sponsorflow.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id)
                .orElseThrow(() ->
                        new SponsorNotFoundException(
                                "Sponsor not found with id: " + id));
    }
    public void deleteSponsor(Long id){
        sponsorRepository.deleteById(id);
    }
    public List<Sponsor> searchByCompanyName(
            String companyName) {

        return sponsorRepository
                .findByCompanyName(companyName);
    }
    public List<Sponsor> searchByStatus(SponsorStatus status) {

        return sponsorRepository.findByStatus(status);
    }
    public List<Sponsor> searchByIndustry(
            String industry) {

        return sponsorRepository
                .findByIndustry(industry);
    }
    public List<Sponsor> getSponsorsSorted(String field) {

        return sponsorRepository.findAll(
                Sort.by(field)
        );
    }
    public Page<Sponsor> getSponsorsPaginated(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return sponsorRepository.findAll(pageable);
    }
}
