package com.reservation.store.service;

import com.reservation.store.domain.User;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.exception.CustomException;
import com.reservation.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reservation.store.constant.Role.CUSTOMER;
import static com.reservation.store.constant.Role.PARTNER;
import static com.reservation.store.exception.ErrorCode.DUPLICATE_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @param userInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)
     *  유저 정보를 받아 이미 회원가입한 회원인지 검증한 후,
     *  userRepository에 저장
     */
    @Transactional
    public void saveCustomer(UserInfo userInfo) {
        validateDuplicateUser(userInfo);
        userRepository.save(User.createUser(userInfo, CUSTOMER, passwordEncoder));
    }

    /**
     * @param userInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)
     *  점장 정보를 받아 이미 회원가입한 회원인지 검증한 후,
     *  userRepository에 저장
     */
    @Transactional
    public void savePartner(UserInfo userInfo) {
        validateDuplicateUser(userInfo);
        userRepository.save(User.createUser(userInfo, PARTNER, passwordEncoder));
    }


    /**
     * @param userInfo (유저이름, 이메일, 비밀번호, 휴대폰 번호)
     * 이메일 기준으로 회원을 찾고 데이터베이스에 있다면, 에러를 내립니다.
     * 데이터베이스에 없는 정보면 에러가 나지않습니다.
     */
    private void validateDuplicateUser(UserInfo userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new CustomException(DUPLICATE_USER);
        }
    }


}
