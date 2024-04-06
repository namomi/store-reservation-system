package com.reservation.store.service;

import com.reservation.store.domain.Store;
import com.reservation.store.domain.User;
import com.reservation.store.dto.StoreInfo;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.repository.StoreRepository;
import com.reservation.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.reservation.store.constant.Role.PARTNER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StoreService storeService;


    @Test
    public void registerStoreTest(){
        //given
        UserInfo userInfo = createUser();
        User user = User.createUser(userInfo, PARTNER, passwordEncoder);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userInfo.password())).thenReturn("encodedPassword");

        StoreInfo storeInfo = registerStore();
        Store store = Store.registerStore(storeInfo, user);
        when(storeRepository.findByName(store.getName())).thenReturn(Optional.of(store));

        //when
        storeService.registerStore(storeInfo, userInfo.email());

        //then
        verify(storeRepository, times(1)).save(any(Store.class));
        Optional<Store> savedStore = storeRepository.findByName(store.getName());

        assertThat(savedStore.isPresent()).isTrue();
        savedStore.ifPresent(s -> {
            assertThat(s.getName()).isEqualTo("이삭토스트");
            assertThat(s.getLocation()).isEqualTo("서울 중구");
            assertThat(s.getDescription()).isEqualTo("토스트 가게");
        });
    }

    @Test
    public void findStoresTest() {
        // given
        String email = "user@example.com";
        UserInfo userInfo1 = createUser();
        User user = User.createUser(userInfo1, PARTNER, passwordEncoder);
        StoreInfo storeInfo = registerStore();
        Store store1 = Store.registerStore(storeInfo, user);
        Store store2 = Store.registerStore(new StoreInfo("뚜레쥬르", "서울 강남구", "베이커리"), user);;
        user.setStore(store1);
        user.setStore(store2);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        List<Store> stores = storeService.findStores(email);

        // then
        assertThat(stores).hasSize(2);
        assertThat(stores.get(0).getName()).isEqualTo("이삭토스트");
        assertThat(stores.get(1).getName()).isEqualTo("뚜레쥬르");
    }

    @Test
    public void searchStoreTest() {
        // given
        String name = "이삭";
        Store store = getStore();
        when(storeRepository.findByNameContaining(name)).thenReturn(Arrays.asList(store));

        // when
        List<Store> stores = storeService.searchStore(name);

        // then
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getName()).isEqualTo("이삭토스트");
    }

    private Store getStore() {
        UserInfo userInfo1 = createUser();
        User user = User.createUser(userInfo1, PARTNER, passwordEncoder);
        StoreInfo storeInfo = registerStore();
        Store store = Store.registerStore(storeInfo, user);
        return store;
    }

    @Test
    public void getStoreDetailsTest() {
        // given
        Long id = 1L;
        Store store = getStore();
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));

        // when
        Optional<Store> foundStore = storeService.getStoreDetails(id);

        // then
        assertThat(foundStore.isPresent()).isTrue();
        foundStore.ifPresent(s -> {
            assertThat(s.getName()).isEqualTo("이삭토스트");
            assertThat(s.getLocation()).isEqualTo("서울 중구");
        });
    }

    private StoreInfo registerStore() {
        return new StoreInfo("이삭토스트", "서울 중구", "토스트 가게");
    }

    private UserInfo createUser() {
        return new UserInfo("정히나", "test@name.com", "1234", "010-1111-3333");
    }
}