package com.server.base.components.base;

import com.server.base.repository.dto.reference.AccountDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@NoArgsConstructor
public class BaseService {
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    protected ModelMapper mapper;

    protected Boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }

    protected void encryptPassword(AccountDto accountDto){
        accountDto.setUserPwd(bCryptPasswordEncoder.encode(accountDto.getUserPwd()));
    }
}
