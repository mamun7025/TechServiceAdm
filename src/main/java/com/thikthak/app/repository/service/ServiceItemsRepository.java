package com.thikthak.app.repository.service;

import com.thikthak.app.domain.service.ServiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceItemsRepository extends JpaRepository<ServiceItems, Long> {
}
