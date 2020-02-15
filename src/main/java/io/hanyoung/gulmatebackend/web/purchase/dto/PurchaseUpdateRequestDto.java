package io.hanyoung.gulmatebackend.web.purchase.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class PurchaseUpdateRequestDto {

    private String title;
    private String place;
    private boolean complete;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @Builder
    public PurchaseUpdateRequestDto(String title, String place, boolean complete, LocalDateTime deadline) {
        this.title = title;
        this.place = place;
        this.complete = complete;
        this.deadline = deadline;
    }
}
