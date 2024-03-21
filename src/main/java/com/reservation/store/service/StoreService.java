package com.reservation.store.service;

import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.StoreInfo;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public void registerStore(StoreInfo storeInfo, String email) {
        validateDuplicateUserAndStore(storeInfo, email);
        User user = getUser(email);
        storeRepository.save(Store.registerStore(storeInfo, user));
    }

    public List<Store> findStores(String email) {
        User user = getUser(email);
        return user.getStores();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 사용자가 존재하지 않습니다."));
    }

    private void validateDuplicateUserAndStore(StoreInfo storeInfo, String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("회원이 존재 하지 않습니다.");
        }
        if (storeRepository.existsByLocation(storeInfo.getLocation())) {
            throw new RuntimeException("같은 위치 이미 매장이 등록되어있습니다.");
        }
    }
}
