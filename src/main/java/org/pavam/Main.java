package org.pavam;

import org.pavam.persistence.migration.MigrationStrategy;

import java.sql.SQLException;

import static org.pavam.persistence.config.ConnectionConfig.getConnection;

public class Main {

    public static void main(String[] args) throws SQLException {
        try(var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        }
//        System.out.println(
//                Thread.currentThread().getContextClassLoader()
//                        .getResource("db/changelog/db.changelog-master.yml")
//        );
    }
}
