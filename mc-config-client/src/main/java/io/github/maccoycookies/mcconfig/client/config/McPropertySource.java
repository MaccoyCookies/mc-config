package io.github.maccoycookies.mcconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

public class McPropertySource extends EnumerablePropertySource<McConfigService> {

    public McPropertySource(String name, McConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
