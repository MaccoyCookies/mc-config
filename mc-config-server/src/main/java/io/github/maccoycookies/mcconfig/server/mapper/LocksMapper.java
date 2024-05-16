package io.github.maccoycookies.mcconfig.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * mapper for dist lock
 */
@Mapper
@Repository
public interface LocksMapper {

    @Select("set innodb_lock_wait_timeout = 5")
    void waitTime();

    @Select("select app from locks where id = 1 for update")
    String selectForUpdate();


}
