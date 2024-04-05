package com.reservation.store.service;

import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.StoreInfo;
import com.reservation.store.exception.CustomException;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.reservation.store.constant.Role.PARTNER;
import static com.reservation.store.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * @param storeInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)
     * @param email
     * 이메일 정보로 해당 게정이 점장인지 확인 후,
     * 이미 등록된 매장이 있는지 확인 후, 매장 등록한게 없다면 매장을 등록합니다.
     */
    @Transactional
    public void registerStore(StoreInfo storeInfo, String email) {
        User user = getUser(email);
        validateDuplicateUserAndStore(storeInfo, user);
        storeRepository.save(Store.registerStore(storeInfo, user));
    }


    /**
     * @param email
     * @return 등록된 매장정보들
     * 등록된 이메일 정보로 등록하신 매장정보를 반환합니다.
     */
    public List<Store> findStores(String email) {
        User user = getUser(email);
        return user.getStores();
    }

    /**
     * @param name (매장이름)
     * @return 매장이름가 일치한 매장정보들
     * 매장이름을 받아 등록된 매장들 중에 매장이름이 일치한 매장정보를 반홥니다.
     */
    public List<Store> searchStore(String name) {
        return storeRepository.findByNameContaining(name);
    }

    /**
     * @param id (매장 id)
     * @return 매장 정보
     * 매장 id로 해당 매장 정보를 반환합니다.
     */
    public Optional<Store> getStoreDetails(Long id) {
        return storeRepository.findById(id);
    }

    /**
     * @param email (유저 이메일)
     * @return 유저 이메릴정보로 가입된 회원이면 회우너 반환하고, 없으면 에러를 반환합니다.
     */
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    /**
     * @param storeInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)
     * @param user (가입한 유저정보)
     * 해당 유저가 점주인지 확인하고, 매장이 같은 위치에 등록되지 않았는지 검증합니다.
     */
    private void validateDuplicateUserAndStore(StoreInfo storeInfo, User user) {
        if (!PARTNER.equals(user.getRole())) {
            throw new CustomException(NOT_PARTNER);
        }
        if (storeRepository.existsByLocation(storeInfo.getLocation())) {
            throw new CustomException(DUPLICATE_STORE_LOCATION);
        }
    }
}
