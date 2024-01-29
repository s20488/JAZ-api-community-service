package com.jsonplaceholder.communityservice.repositories;

import com.jsonplaceholder.communityservice.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
