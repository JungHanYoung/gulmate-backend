package io.hanyoung.gulmatebackend.web.purchase;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import io.hanyoung.gulmatebackend.domain.purchase.PurchaseRepository;
import io.hanyoung.gulmatebackend.web.purchase.dto.PurchaseCheckRequestDto;
import io.hanyoung.gulmatebackend.web.purchase.dto.PurchaseCheckResponseDto;
import io.hanyoung.gulmatebackend.web.purchase.dto.PurchaseUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Transactional
    public Long update(Long id, PurchaseUpdateRequestDto requestDto) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장보기 데이터가 없습니다."));

        purchase.update(requestDto);

        return id;
    }

    @Transactional
    public PurchaseCheckResponseDto check(Long id, PurchaseCheckRequestDto requestDto, Account account) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장보기 데이터가 없습니다."));

        purchase.check(requestDto, account);

        return new PurchaseCheckResponseDto(purchase);
    }

}
