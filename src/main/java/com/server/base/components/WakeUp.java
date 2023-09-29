package com.server.base.components;

import com.server.base.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created on 2023-05-04
 * Project user-service
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WakeUp {
    private final UserRepository repository;

    @EventListener(value = {ApplicationReadyEvent.class})
    public void message(){

        log.warn("{} is ready", repository.wakeUpMsg("\n" +
                "  _   _                                        _          \n" +
                " | | | |___  ___ _ __      ___  ___ _ ____   _(_) ___ ___ \n" +
                " | | | / __|/ _ \\ '__|____/ __|/ _ \\ '__\\ \\ / / |/ __/ _ \\\n" +
                " | |_| \\__ \\  __/ | |_____\\__ \\  __/ |   \\ V /| | (_|  __/\n" +
                "  \\___/|___/\\___|_|       |___/\\___|_|    \\_/ |_|\\___\\___|"));
    }
}
