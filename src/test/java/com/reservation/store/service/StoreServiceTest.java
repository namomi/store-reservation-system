package com.reservation.store.service;

import com.reservation.store.domain.Store;
import com.reservation.store.dto.StoreInfo;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    StoreService storeService;

    @Autowired
    UserService userService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void registerStoreTest(){
        //given
        UserInfo userInfo = createUser();
        userService.savePartner(userInfo);

        StoreInfo store = registerStore();

        //when
        storeService.registerStore(store,userInfo.getEmail());
        Store savedStore = storeRepository.findByName(store.getName()).get();

        //then
        assertThat(savedStore.getName()).isEqualTo("이삭토스트");
        assertThat(savedStore.getLocation()).isEqualTo("서울 중구");
        assertThat(savedStore.getDescription()).isEqualTo("토스트 가게");
    }

    public StoreInfo registerStore() {
        return new StoreInfo("이삭토스트", "서울 중구", "토스트 가게");
    }

    public UserInfo createUser() {
        return new UserInfo("정히나", "test@name.com", "1234", "010-1111-3333");
    }
}