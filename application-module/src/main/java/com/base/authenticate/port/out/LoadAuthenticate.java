package com.base.authenticate.port.out;

import com.base.authenticate.dto.AuthenticationDetails;
import java.util.Optional;

public interface LoadAuthenticate {

    Optional<AuthenticationDetails> load(String username);

}
