package io.hanyoung.gulmatebackend.web.purchase.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class PurchaseSaveRequestDto {

    private String title;
    private String place;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @Builder
    public PurchaseSaveRequestDto(String title, String place, LocalDateTime deadline) {
        this.title = title;
        this.place = place;
        this.deadline = deadline;
    }

    public Purchase toEntity(Family family, Account account) {
        return Purchase.builder()
                .title(title)
                .deadline(deadline)
                .account(account)
                .family(family)
                .place(place)
                .build();
    }
}
