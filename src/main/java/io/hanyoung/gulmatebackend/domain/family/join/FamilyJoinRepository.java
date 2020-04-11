package io.hanyoung.gulmatebackend.domain.family.join;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FamilyJoinRepository extends JpaRepository<FamilyJoin, FamilyJoinId> {

//    @Query("SELECT fj FROM FamilyJoin fj WHERE account_id = :accountId")
    List<FamilyJoin> findByAccountId(Long accountId);
}
