package com.reservation.store.service;

import com.reservation.store.domain.User;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reservation.store.constant.Role.CUSTOMER;
import static com.reservation.store.constant.Role.PARTNER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveCustomer(UserInfo userInfo) {
        validateDuplicateUser(userInfo);
        userRepository.save(User.createUser(userInfo, CUSTOMER, passwordEncoder));
    }

    @Transactional
    public void savePartner(UserInfo userInfo) {
        validateDuplicateUser(userInfo);
        userRepository.save(User.createUser(userInfo, PARTNER, passwordEncoder));
    }

    private void validateDuplicateUser(UserInfo userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new RuntimeException("이미 가입된 회웝입니다.");
        }
    }


}
