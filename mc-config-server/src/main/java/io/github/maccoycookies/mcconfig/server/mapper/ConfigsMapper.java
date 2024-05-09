package io.github.maccoycookies.mcconfig.server.mapper;

import io.github.maccoycookies.mcconfig.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Maccoy
 * @date 2024/5/3 22:44
 * Description mapper for configs table
 */
@Mapper
@Repository
public interface ConfigsMapper {

    @Select("select * from configs where app = #{app} and env = #{env} and ns = #{ns};")
    List<Configs> list(@Param("app") String app, @Param("env") String env, @Param("ns") String ns);

    @Select("select * from configs where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey};")
    Configs select(@Param("app") String app, @Param("env") String env, @Param("ns") String ns, @Param("pkey") String pkey);

    @Insert("insert into configs (app, env, ns, pkey, pval) values (#{app} , #{env} , #{ns} , #{pkey} , #{pval} );")
    int insert(Configs paramConfig);

    @Update("update configs set pval = #{pval} where app = #{app} and env = #{env} and ns = #{ns} and pkey = #{pkey};")
    int update(Configs paramConfig);

}
