package com.reservation.store.controller;

import com.reservation.store.domain.Store;

import com.reservation.store.dto.StoreInfo;
import com.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores/register")
    public void registerStore(@RequestBody StoreInfo storeInfo, String email) {
        storeService.registerStore(storeInfo, email);
    }

    @GetMapping("/stores")
    public List<Store> findAllStores(@RequestBody String email) {
        return storeService.findStores(email);
    }
}
