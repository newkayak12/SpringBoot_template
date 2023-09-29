package com.server.base.repository.userRepository;

import com.server.base.repository.dto.reference.AccountDto;
import com.server.base.repository.dto.request.SignInRequest;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<AccountDto> signIn(SignInRequest signInRequest);
}
