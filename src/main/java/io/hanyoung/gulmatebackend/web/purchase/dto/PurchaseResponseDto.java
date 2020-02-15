package io.hanyoung.gulmatebackend.web.purchase.dto;

import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseResponseDto {

    private Long id;
    private String title;
    private String place;
    private boolean complete;
    private LocalDateTime deadline;
    private String creator;
    private String checker;
    private LocalDateTime checkedDateTime;

    public PurchaseResponseDto(Purchase entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.place = entity.getPlace();
        this.complete = entity.isComplete();
        this.deadline = entity.getDeadline();
        this.creator = entity.getAccount().getName();
        this.checker = entity.getChecker() != null ? entity.getChecker().getName() : null;
        this.checkedDateTime = entity.getCheckedDateTime();
    }

}
