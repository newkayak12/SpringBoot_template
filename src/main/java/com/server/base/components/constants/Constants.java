package com.server.base.components.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "constants")
public class Constants {

    public static String SALT;
    public static String TOKEN_NAME;
    public static String PROJECT_NAME;

    public static String EMAIL;
    public static String EMAIL_KEY;

    @Value("${constant.file_path}")
    public void setSalt(String _SALT) { SALT = _SALT; }
    @Value("${constant.token_name}")
    public void setTokenName(String _TOKEN_NAME) { TOKEN_NAME = _TOKEN_NAME; }
    @Value("${constant.project_name}")
    public void setProjectName(String _PROJECT_NAME) { PROJECT_NAME = _PROJECT_NAME; }

    @Value("${constant.email}")
    public void setEmail(String _EMAIL) { EMAIL = _EMAIL; }
    @Value("${constant.email_key}")
    public void setEmailKey(String _EMAIL_KEY) { EMAIL_KEY = _EMAIL_KEY; }
}
