package com.jsonplaceholder.communityservice.repositories;

import com.jsonplaceholder.communityservice.model.Geo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoRepository extends JpaRepository<Geo, Integer> {
}
