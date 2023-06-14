package com.example.authServerPassword.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BcryptPasswordEncoder implements PasswordEncoder{

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encode(CharSequence rawPassword) {
		log.info("BcryptPasswordEncoder.encode :::: {}", rawPassword);
		return bCryptPasswordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		log.info("BcryptPasswordEncoder.matches :::: {} <-> {}", rawPassword, encodedPassword);
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}
}
