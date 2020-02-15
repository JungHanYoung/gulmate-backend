package io.hanyoung.gulmatebackend.domain.purchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query(value = "SELECT p FROM Purchase p WHERE p.family.id = :familyId")
    Page<Purchase> findAllByFamilyId(@Param("familyId") Long familyId, Pageable pageable);
}
