package com.nitish.sponsorflow.service;

import com.nitish.sponsorflow.dto.DashboardStatsResponse;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Sponsor updateSponsor(
            Long id,
            Sponsor updatedSponsor) {

        Sponsor sponsor = sponsorRepository
                .findById(id)
                .orElseThrow(() ->
                        new SponsorNotFoundException(
                                "Sponsor not found with id: " + id));

        sponsor.setCompanyName(
                updatedSponsor.getCompanyName());

        sponsor.setContactPerson(
                updatedSponsor.getContactPerson());

        sponsor.setDesignation(
                updatedSponsor.getDesignation());

        sponsor.setEmail(
                updatedSponsor.getEmail());

        sponsor.setPhone(
                updatedSponsor.getPhone());

        sponsor.setIndustry(
                updatedSponsor.getIndustry());

        sponsor.setNotes(
                updatedSponsor.getNotes());

        sponsor.setStatus(
                updatedSponsor.getStatus());

        return sponsorRepository.save(sponsor);
    }
    public long getTotalSponsorCount() {

        return sponsorRepository.count();

    }
    public long getSponsorCountByStatus(
            SponsorStatus status) {

        return sponsorRepository
                .findByStatus(status)
                .size();
    }
    public DashboardStatsResponse getDashboardStats(){
        DashboardStatsResponse stats= new DashboardStatsResponse();
        stats.setTotalSponsors(
                sponsorRepository.count());
        stats.setLeadSponsors(
                getSponsorCountByStatus(
                        SponsorStatus.LEAD));

        stats.setMeetingSponsors(
                getSponsorCountByStatus(
                        SponsorStatus.MEETING));

        stats.setWonSponsors(
                getSponsorCountByStatus(
                        SponsorStatus.WON));

        stats.setLostSponsors(
                getSponsorCountByStatus(
                        SponsorStatus.LOST));

        return stats;
    }
    public Map<String, Long> getIndustryCounts() {

        Map<String, Long> industryCounts =
                new HashMap<>();

        List<Sponsor> sponsors =
                sponsorRepository.findAll();

        for (Sponsor sponsor : sponsors) {

            String industry =
                    sponsor.getIndustry();

            industryCounts.put(
                    industry,
                    industryCounts.getOrDefault(
                            industry, 0L
                    ) + 1
            );
        }

        return industryCounts;
    }
}
