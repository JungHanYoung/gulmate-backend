package io.hanyoung.gulmatebackend.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.family.id = :familyId")
    List<Chat> getChatMessageListByFamilyId(@Param("familyId") Long familyId);

    @Query("SELECT c FROM Chat c WHERE c.family.id = :familyId AND c.id > :lastReadChatId")
    List<Chat> getUnreadChatMessageCount(@Param("familyId") Long familyId, @Param("lastReadChatId") Long lastReadChatId);
}
