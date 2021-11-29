package com.thikthak.app.repository.service;

import com.thikthak.app.domain.service.ProdServicePriceRateList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdServicePriceRateListRepository extends JpaRepository<ProdServicePriceRateList,Long>{


}
