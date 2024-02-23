package org.telegram.services;

import org.telegram.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class MessageService {
    private final Properties props;

    public MessageService() {
        this.props = new Properties();
        try {
            InputStream is = Main.class.getResourceAsStream("/message.properties");
            if (Objects.nonNull(is)) {
                this.props.load(is);
                is.close();
            }
        } catch (IOException ignored) {
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
