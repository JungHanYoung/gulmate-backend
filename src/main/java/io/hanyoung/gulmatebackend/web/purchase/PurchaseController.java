package io.hanyoung.gulmatebackend.web.purchase;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import io.hanyoung.gulmatebackend.domain.purchase.PurchaseRepository;
import io.hanyoung.gulmatebackend.web.purchase.dto.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Gulmate Purchase API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseService purchaseService;

    @ApiOperation(value = "get all purchases from gulmate", notes = "해당 귤메이트의 장보기 목록 전체", response = PurchaseResponseDto.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/family/{familyId}/purchase")
    public ResponseEntity<?> getAllPurchaseList(
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

    @ApiOperation(value = "create purchase", notes = "장보기 추가")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path")
    })
    @PostMapping("/{familyId}/purchase")
    public ResponseEntity<?> createPurchase(
            @AuthUser Account account,
            @ApiParam(required = true, value = "장보기 아이템 저장 데이") @RequestBody PurchaseSaveRequestDto requestDto,
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

    @ApiOperation(value = "update purchase", notes = "장보기 수정", response = Long.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "purchaseId", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @PutMapping("/{familyId}/purchase/{purchaseId}")
    public ResponseEntity<?> updatePurchase(
            @PathVariable Long familyId,
            @PathVariable Long purchaseId,
            @AuthUser Account account,
            @ApiParam @RequestBody PurchaseUpdateRequestDto requestDto
    ) {
        if(accountBelongToFamily(account, familyId)) {
            Long updatedId = purchaseService.update(purchaseId, requestDto);
            return ResponseEntity.ok(updatedId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: 해당 인증정보는 다음 데이터에 접근할 수 없습니다.");
    }

    @ApiOperation(value = "check purchase", notes = "장보기 완료 체크", response = PurchaseCheckResponseDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "purchaseId", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @PutMapping("/{familyId}/purchase/{purchaseId}/complete")
    public ResponseEntity<?> checkPurchase(
            @PathVariable Long familyId,
            @PathVariable Long purchaseId,
            @AuthUser Account account,
            @ApiParam @RequestBody PurchaseCheckRequestDto requestDto
    ) {
        if(accountBelongToFamily(account, familyId)) {
            PurchaseCheckResponseDto responseDto = purchaseService.check(purchaseId, requestDto, account);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: 해당 인증정보는 다음 데이터에 접근할 수 없습니다.");
    }

    @ApiOperation(value = "delete purchase from familyId", notes = "장보기 아이템 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "purchaseId", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{familyId}/purchase/{purchaseId}")
    public ResponseEntity<?> deletePurchase(
            @AuthUser Account account,
            @PathVariable Long familyId,
            @PathVariable Long purchaseId
    ) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Error: Can't found purchase"));
        if(purchaseBelongToFamily(purchase, familyId) && accountBelongToFamily(account, familyId)) {
            purchaseRepository.deleteById(purchaseId);
            return ResponseEntity.ok(purchaseId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Error: Can't access to delete purchase");
    }

    @ApiOperation(value = "get purchase list at today", response = PurchaseResponseDto.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{familyId}/purchase/today")
    public ResponseEntity<?> getTodayPurchaseList(
            @PathVariable Long familyId,
            @AuthUser Account account
    ) {
        if(!accountBelongToFamily(account, familyId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(
                purchaseRepository.findAllByTodayAndFamilyId(familyId)
                    .stream()
                .map(PurchaseResponseDto::new)
                .collect(Collectors.toList())
        );

    }

    private boolean accountBelongToFamily(Account account, Long familyId) {
        return account.getMemberInfos()
                .stream()
                .anyMatch(memberInfo -> memberInfo.getFamily().getId().equals(familyId));
    }

    private boolean purchaseBelongToFamily(Purchase purchase, Long familyId) {
        return purchase.getFamily().getId().equals(familyId);
    }

}
