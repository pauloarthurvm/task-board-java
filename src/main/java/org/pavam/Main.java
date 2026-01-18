package org.pavam;

import org.pavam.persistence.migration.MigrationStrategy;
import org.pavam.ui.simulator.MainMenu;

import java.sql.SQLException;

import static org.pavam.persistence.config.ConnectionConfig.getConnection;

public class Main {

    public static void main(String[] args) throws SQLException {
        try(var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        }
        new MainMenu().execute();
    }
}
