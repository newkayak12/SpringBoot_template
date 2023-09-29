package com.server.base.components.exceptions;

import lombok.Getter;

@Getter
public enum BecauseOf {
    ALREADY_EXIST_ACCOUNT("이미 존재하는 계정입니다."),
    NO_DATA("데이터가 없습니다."),
    PASSWORD_NOT_MATCHED("아이디 혹은 비밀번호를 확인해주세요."),
    ACCOUNT_NOT_EXIST("아이디 혹은 비밀번호를 확인해주세요."),
    UPDATE_FAILURE("변경에 실패했습니다."),
    INSERT_FAILURE("등록에 실패했습니다."),
    DELETE_FAILURE("삭제에 실패했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    EXCEED_MAXIMUM_PROFILE_COUNT("프로필은 최대 4개까지 만들 수 있습니다."),
    PAYMENT_FAILURE("지불 사항 검증에 실패했습니다.");


    private String msg;
    BecauseOf(String msg){
        this.msg = msg;
    }
}
