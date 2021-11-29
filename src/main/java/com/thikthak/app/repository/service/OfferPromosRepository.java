package com.thikthak.app.repository.service;

import com.thikthak.app.domain.service.OfferPromos;
import com.thikthak.app.domain.system.VisitorsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferPromosRepository extends JpaRepository<OfferPromos,Long> {

    // Good working
    // https://stackoverflow.com/questions/29172313/spring-data-repository-does-not-delete-manytoone-entity
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM OFFER_PROMOS WHERE id = ?1", nativeQuery = true)
    void deleteById(Long postId);

}
