package io.hanyoung.gulmatebackend.web.purchase;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import io.hanyoung.gulmatebackend.domain.purchase.PurchaseRepository;
import io.hanyoung.gulmatebackend.web.purchase.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseService purchaseService;

    @GetMapping("/api/v1/family/{familyId}/purchase")
    public ResponseEntity<?> getAllPurchaseById(
            @AuthUser Account account,
            @PathVariable Long familyId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        if(accountBelongToFamily(account, familyId)) {
            Page<Purchase> allByFamilyId = purchaseRepository.findAllByFamilyId(account.getCurrentFamily().getId(), pageable);

            return ResponseEntity.ok(allByFamilyId.stream()
            .map(PurchaseResponseDto::new)
            .collect(Collectors.toList()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @PostMapping("/api/v1/{familyId}/purchase")
    public ResponseEntity<?> createPurchase(
            @AuthUser Account account,
            @RequestBody PurchaseSaveRequestDto requestDto,
            @PathVariable Long familyId
    ) {
        if(accountBelongToFamily(account, familyId)) {
            Purchase savedPurchase = purchaseRepository.save(requestDto.toEntity(account.getCurrentFamily(), account));
            return ResponseEntity.ok(new PurchaseResponseDto(savedPurchase));
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @PutMapping("/api/v1/{familyId}/purchase/{purchaseId}")
    public ResponseEntity<?> updatePurchase(
            @PathVariable Long familyId,
            @PathVariable Long purchaseId,
            @AuthUser Account account,
            @RequestBody PurchaseUpdateRequestDto requestDto
    ) throws Exception {
        if(accountBelongToFamily(account, familyId)) {
            Long updatedId = purchaseService.update(purchaseId, requestDto);
            return ResponseEntity.ok(updatedId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: 해당 인증정보는 다음 데이터에 접근할 수 없습니다.");
    }

    @PutMapping("/api/v1/{familyId}/purchase/{purchaseId}/complete")
    public ResponseEntity<?> checkPurchase(
            @PathVariable Long familyId,
            @PathVariable Long purchaseId,
            @AuthUser Account account,
            @RequestBody PurchaseCheckRequestDto requestDto
    ) {
        if(accountBelongToFamily(account, familyId)) {
            PurchaseCheckResponseDto responseDto = purchaseService.check(purchaseId, requestDto, account);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: 해당 인증정보는 다음 데이터에 접근할 수 없습니다.");
    }

    @DeleteMapping("/api/v1/{familyId}/purchase/{purchaseId}")
    public ResponseEntity<?> deletePurchase(
            @AuthUser Account account,
            @PathVariable Long familyId,
            @PathVariable Long purchaseId
    ) throws Exception {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Error: Can't found purchase"));
        if(purchaseBelongToFamily(purchase, familyId) && accountBelongToFamily(account, familyId)) {
            purchaseRepository.deleteById(purchaseId);
            return ResponseEntity.ok(purchaseId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: Can't access to delete purchase");
    }

    private boolean accountBelongToFamily(Account account, Long familyId) {
        return account.getCurrentFamily().getId().equals(familyId);
    }

    private boolean purchaseBelongToFamily(Purchase purchase, Long familyId) {
        return purchase.getFamily().getId().equals(familyId);
    }

}
