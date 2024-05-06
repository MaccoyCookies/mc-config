package io.github.maccoycookies.mcconfig.client.config;

import java.util.Map;

public class McConfigServiceImpl implements McConfigService {

    Map<String, String> config;

    public McConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return this.config.get(name);
    }
}
