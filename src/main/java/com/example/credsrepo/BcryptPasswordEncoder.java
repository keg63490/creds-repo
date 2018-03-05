//package com.example.credsrepo;

import java.security.SecureRandom;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("passwordEncoder")
public class BcryptPasswordEncoder implements PasswordEncoder, InitializingBean {

    @Value("${bcryptRounds}")
    private int bcryptRounds = 10;
    private BCryptPasswordEncoder delegate;
    @Value("${passwordEncryptionSeed}")
    private String seed = "E1F53135E559C253";

    @Override
    public void afterPropertiesSet() throws Exception {
        delegate = new BCryptPasswordEncoder(bcryptRounds, new SecureRandom(seed.getBytes("ISO-8859-1")));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }

}
