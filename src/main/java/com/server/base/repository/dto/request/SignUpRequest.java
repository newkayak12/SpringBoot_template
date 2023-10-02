package com.server.base.repository.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.base.repository.dto.reference.AccountDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(value = {
        "userNo",
        "regDate",
})
@Data
public class SignUpRequest extends AccountDto {

}
