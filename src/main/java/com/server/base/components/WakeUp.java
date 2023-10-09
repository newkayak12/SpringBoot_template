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
        log.warn("{} is ready", repository.wakeUpMsg("bases"));
    }
}
