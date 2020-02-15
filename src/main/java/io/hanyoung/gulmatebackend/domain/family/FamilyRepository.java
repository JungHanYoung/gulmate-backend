package io.hanyoung.gulmatebackend.domain.family;

import io.hanyoung.gulmatebackend.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    boolean existsByInviteKey(String inviteKey);

    Optional<Family> findByInviteKey(String inviteKey);

//    @Query(value = "SELECT f.* FROM Family f WHERE ")

//    @Query(value = "SELECT f FROM Family f WHERE f.account_id = :accountId")
//    List<Family> findByAccountId(@Param("accountId") Long accountId);

//    @Query(value = "SELECT f FROM Family f, Account a WHERE a.id = :accountId AND f.id = a.family_id")
//    List<Family> findByAccountIdWithJPQL(@Param("accountId") Long accountId);
//    @Query("SELECT f FROM Family f WHERE f.account_id = :accountId")
//    List<Family>
//    Optional<Family> findByAccountId(Long accountId);
}
