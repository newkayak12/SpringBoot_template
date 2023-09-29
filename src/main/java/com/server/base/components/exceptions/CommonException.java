package com.server.base.components.exceptions;

public class CommonException extends Exception{

    public CommonException(BecauseOf reason){
        super(reason.getMsg());
    }
}
