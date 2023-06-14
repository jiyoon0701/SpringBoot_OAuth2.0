package com.example.authServerPassword.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShaPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		log.info("ShaPasswordEncoder.encode :::: {}",rawPassword);
		return Crypto.sha256(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		log.info("ShaPasswordEncoder.matches :::: {}<->{}",rawPassword,encodedPassword);
		return Crypto.sha256(rawPassword.toString()).equals(encodedPassword);
	}
	
	
}
