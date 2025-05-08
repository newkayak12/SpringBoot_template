package com.base.config.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

public class HttpHeaderLocaleResolver implements LocaleResolver {

    private static final List<Locale> SUPPORTED_LANGUAGE = List.of(Locale.KOREA, Locale.ENGLISH);
    private static final Locale DEFAULT_LANGUAGE = Locale.KOREA;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (!StringUtils.hasText(acceptLanguage)) {
            return DEFAULT_LANGUAGE;
        }

        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(acceptLanguage);
        return Optional.ofNullable(Locale.lookup(languageRanges, SUPPORTED_LANGUAGE)).orElse(DEFAULT_LANGUAGE);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }

}
