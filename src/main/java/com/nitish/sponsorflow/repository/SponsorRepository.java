package com.nitish.sponsorflow.repository;

import com.nitish.sponsorflow.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor,Long>{

}
