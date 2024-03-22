package com.reservation.store.service;

import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.StoreInfo;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.reservation.store.constant.Role.PARTNER;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerStore(StoreInfo storeInfo, String email) {
        User user = getUser(email);
        validateDuplicateUserAndStore(storeInfo, user);
        storeRepository.save(Store.registerStore(storeInfo, user));
    }

    public List<Store> findStores(String email) {
        User user = getUser(email);
        return user.getStores();
    }

    public List<Store> searchStore(String name) {
        return storeRepository.findByNameContaining(name);
    }

    public Optional<Store> getStoreDetails(Long id) {
        return storeRepository.findById(id);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 사용자가 존재하지 않습니다."));
    }

    private void validateDuplicateUserAndStore(StoreInfo storeInfo, User user) {
        if (!PARTNER.equals(user.getRole())) {
            throw new RuntimeException("점장이 아니면 등록할 수 없습니다.");
        }
        if (storeRepository.existsByLocation(storeInfo.getLocation())) {
            throw new RuntimeException("같은 위치 이미 매장이 등록되어있습니다.");
        }
    }
}
