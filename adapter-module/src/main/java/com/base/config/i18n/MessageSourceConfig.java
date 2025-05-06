package com.base.config.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.yaml.snakeyaml.Yaml;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        YamlMessageSource yamlMessageSource = new YamlMessageSource();
        yamlMessageSource.setBasename("classpath:messages");
        yamlMessageSource.setDefaultEncoding("UTF-8");
        yamlMessageSource.setFallbackToSystemLocale(false);
        return yamlMessageSource;
    }

    public static class YamlMessageSource extends ResourceBundleMessageSource {

        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, new YamlConverter());
        }
    }

    public static class YamlConverter extends ResourceBundle.Control {

        @Override
        public List<String> getFormats(String baseName) {
            return List.of("yaml", "yml");
        }


        @Override
        public ResourceBundle newBundle(
            String baseName, Locale locale, String format, ClassLoader loader, boolean reload
        ) throws IOException {

            String resourceName = toResourceName(toBundleName(baseName, locale), format);
            try (InputStream inputStream = loader.getResourceAsStream(resourceName)) {

                if (inputStream == null) {
                    return null;
                }

                Yaml yaml = new org.yaml.snakeyaml.Yaml();
                Map<String, Object> data = yaml.load(inputStream);

                return new ResourceBundle() {
                    @Override
                    protected Object handleGetObject(String key) {
                        return data.get(key);
                    }

                    @Override
                    public Enumeration<String> getKeys() {
                        return Collections.enumeration(data.keySet());
                    }
                };
            }
        }
    }
}
