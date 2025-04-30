package com.base.authenticate.usecase;

import com.base.authenticate.port.out.LoadAuthenticate;
import com.base.config.UseCase;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@UseCase
@RequiredArgsConstructor
public class LoadUserUseCase implements UserDetailsService {

    private final LoadAuthenticate loadAuthenticate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAuthenticate.load(username).orElseThrow(NoSuchElementException::new);
    }
}
