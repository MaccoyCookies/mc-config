package io.github.maccoycookies.mcconfig.client.config;

public interface McConfigService {

    String[] getPropertyNames();

    String getProperty(String name);

}
