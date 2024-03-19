package com.reservation.store.service;


import com.reservation.store.domain.User;
import com.reservation.store.dto.UserInfo;
import com.reservation.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class UserServiceTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Test
    public void saveUserTest() throws Exception{
        //given
        UserInfo user = createUser();

        //when
        userService.saveCustomer(user);
        User savedUser = userRepository.findByEmail(user.getEmail()).get();

        //then;
        assertThat(savedUser.getName()).isEqualTo("정히나");
        assertThat(savedUser.getEmail()).isEqualTo("test@name.com");
        assertThat(savedUser.getPhone()).isEqualTo("010-1111-3333");

    }

    public UserInfo createUser() {
        return new UserInfo("정히나", "test@name.com", "1234", "010-1111-3333");

    }
}