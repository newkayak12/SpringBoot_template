package com.server.base.components.configure;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

public abstract class ConfigMsg {


    public static void msg(String config) {
        String RESET = "\u001B[0m";
        String FONT_GREEN = "\u001B[32m";
        System.out.println(String.format(" %s ::::::::::::: %s ::::: %sON%s", LocalDateTime.now(), config, FONT_GREEN, RESET));
    }
}
