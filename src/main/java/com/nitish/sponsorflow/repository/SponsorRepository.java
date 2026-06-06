package com.nitish.sponsorflow.repository;

import com.nitish.sponsorflow.entity.Sponsor;
import com.nitish.sponsorflow.entity.SponsorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor,Long> {
    List<Sponsor> findByCompanyName(String companyName);
    List<Sponsor> findByStatus(SponsorStatus status);
    List<Sponsor> findByIndustry(String industry);
}
