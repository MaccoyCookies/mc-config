package io.github.maccoycookies.mcconfig.server;

import io.github.maccoycookies.mcconfig.server.mapper.LocksMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * distributed locks
 */
@Component
public class DistributedLocks {

    @Autowired
    private LocksMapper locksMapper;

    @Autowired
    private DataSource dataSource;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    Connection connection;

    @PostConstruct
    public void init() {
        try {
            this.connection = dataSource.getConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        executorService.scheduleWithFixedDelay(this::tryLock, 1, 5, TimeUnit.SECONDS);
    }

    public boolean lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout = 5");
        connection.createStatement().execute("select app from locks where id = 1 for update");
        if (locked.get()) {
            System.out.println("===> reenter this dist lock");
        } else {
            System.out.println("===> get a dist lock");
        }
        return true;
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception exception) {
            System.out.println("lock failed");
            locked.set(false);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception exception) {
            System.out.println("ignore this close exception");
        }
    }

}
