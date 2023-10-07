package com.server.base.repository.dto.reference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import com.server.base.components.enums.Role;
import com.server.base.components.validations.AccountValid;
import com.server.base.components.validations.ProfileValid;
import com.server.base.components.validations.TicketValid;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Created on 2023-05-10
 * Project user-service
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"regDate","lastSignDate", "userPwd", "role", "refreshToken"}, allowGetters = false)
@Getter
@Setter
public class AccountDto implements Serializable, UserDetails {
    @NotEmpty(message = "계정 정보가 필요합니다.", groups = {ProfileValid.Save.class, TicketValid.Raise.class})
    private Long userNo;

    @Schema(defaultValue = "test")
    @NotEmpty(message = "아이디를 입력하세요.", groups = {AccountValid.SignUp.class, AccountValid.SignIn.class, AccountValid.FindPwd.class})
    private String userId;

    @Schema(defaultValue = "1212")
    @NotEmpty(message = "비밀번호를 입력하세요.", groups = {
                                                        AccountValid.SignUp.class,
                                                        AccountValid.SignIn.class,
                                                        AccountValid.changePwd.class
                                                     })
    private String userPwd;

    @Hidden
    private LocalDateTime regDate;
    @Hidden
    private LocalDateTime lastSignDate;
    @Hidden
    private Role role;
    @Hidden
    private String refreshToken;

    @QueryProjection
    public AccountDto(Long userNo, String userId, String userPwd, LocalDateTime regDate, LocalDateTime lastSignDate, Role role) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPwd = userPwd;
        this.regDate = regDate;
        this.lastSignDate = lastSignDate;
        this.role = role;
    }

    @QueryProjection
    public AccountDto(Long userNo, String userId, String userPwd, LocalDateTime regDate, LocalDateTime lastSignDate, Role role, String refreshToken) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPwd = userPwd;
        this.regDate = regDate;
        this.lastSignDate = lastSignDate;
        this.role = role;
        this.refreshToken = refreshToken;
    }


    @Hidden
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Hidden
    @Override
    public String getPassword() {
        return null;
    }

    @Hidden
    @Override
    public String getUsername() {
        return this.getUserId();
    }

    @Hidden
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Hidden
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Hidden
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Hidden
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Hidden
    public Boolean isInValid () {
        return Objects.isNull(this.userNo) ||
               Objects.isNull(this.userId) ||
               Objects.isNull(this.role);
    }
}
