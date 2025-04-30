package password.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodeUtility {

    public static BCryptPasswordEncoder getInstance() {
        return SingletonHolder.INSTANCE.bCryptPasswordEncoder;
    }

    enum SingletonHolder {
        INSTANCE(new BCryptPasswordEncoder());
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        SingletonHolder(BCryptPasswordEncoder bCryptPasswordEncoder) {
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        }
    }

}
