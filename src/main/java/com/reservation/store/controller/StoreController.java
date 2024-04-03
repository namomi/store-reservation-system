package com.reservation.store.controller;

import com.reservation.store.domain.Store;

import com.reservation.store.dto.StoreInfo;
import com.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/store/register/{email}")
    public void registerStore(@RequestBody StoreInfo storeInfo, @PathVariable String email) {
        storeService.registerStore(storeInfo, email);
    }

    @GetMapping("/store")
    public List<Store> findAllStores(@RequestBody String email) {
        return storeService.findStores(email);
    }

    @GetMapping("/store/search")
    public List<Store> searchStores(@RequestParam String name) {
        return storeService.searchStore(name);
    }

    @GetMapping("/store/{id}")
    public Store getStoreDetails(@PathVariable Long id) {
        return storeService.getStoreDetails(id)
                .orElseThrow(() -> new RuntimeException("해당 가게의 정보가 없습니다."));
    }
}
