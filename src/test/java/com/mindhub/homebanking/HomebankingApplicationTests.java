package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class HomebankingApplicationTests {

	@MockBean
	PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

}
