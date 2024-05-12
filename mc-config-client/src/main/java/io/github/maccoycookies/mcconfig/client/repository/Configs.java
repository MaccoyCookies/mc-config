package io.github.maccoycookies.mcconfig.client.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Maccoy
 * @date 2024/5/3 22:43
 * Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;



}
