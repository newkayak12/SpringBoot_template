package com.server.base.repository.dto.reference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.base.components.validations.AccountValid;
import com.server.base.components.validations.ProfileValid;
import com.server.base.components.validations.TicketValid;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created on 2023-05-10
 * Project user-service
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"lastSignDate", "userPwd"}, allowSetters = false)
@Getter
@Setter
public class AccountDto implements Serializable {
    @NotEmpty(message = "계정 정보가 필요합니다.", groups = {ProfileValid.Save.class, TicketValid.Raise.class})
    private Long userNo;

    @NotEmpty(message = "아이디를 입력하세요.", groups = {AccountValid.SignUp.class, AccountValid.SignIn.class, AccountValid.FindPwd.class})
    private String userId;
    @NotEmpty(message = "비밀번호를 입력하세요.", groups = {
                                                        AccountValid.SignUp.class,
                                                        AccountValid.SignIn.class,
                                                        AccountValid.changePwd.class
                                                     })
    private String userPwd;
    private LocalDateTime regDate;
    private LocalDateTime lastSignDate;


    @QueryProjection
    public AccountDto(Long userNo, String userId, String userPwd, LocalDateTime regDate, LocalDateTime lastSignDate) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPwd = userPwd;
        this.regDate = regDate;
        this.lastSignDate = lastSignDate;
    }
}
