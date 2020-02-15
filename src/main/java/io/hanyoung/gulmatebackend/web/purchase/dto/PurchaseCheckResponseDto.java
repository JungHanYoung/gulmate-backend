package io.hanyoung.gulmatebackend.web.purchase.dto;

import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseCheckResponseDto {

    private Long updatedId;
    private String checker;
    private LocalDateTime checkedDateTime;

    public PurchaseCheckResponseDto(Purchase entity) {
        this.updatedId = entity.getId();
        this.checker = entity.getChecker() != null ? entity.getChecker().getName() : null;
        this.checkedDateTime = entity.getCheckedDateTime();
    }

    @Builder
    public PurchaseCheckResponseDto(Long updatedId, String checker, LocalDateTime checkedDateTime) {
        this.updatedId = updatedId;
        this.checker = checker;
        this.checkedDateTime = checkedDateTime;
    }
}
