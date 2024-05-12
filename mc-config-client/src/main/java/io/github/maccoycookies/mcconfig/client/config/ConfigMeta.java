package io.github.maccoycookies.mcconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Maccoy
 * @date 2024/5/12 17:29
 * Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMeta {

    String app;
    String env;
    String ns;
    String configServer;

}
