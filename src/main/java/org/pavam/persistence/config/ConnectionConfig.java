package org.pavam.persistence.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

//    java -Ddb.url=jdbc:postgresql://localhost:5432 -Ddb.name=mydb -Ddb.user=my-user -Ddb.pass=my-secret -jar app.jar

//    private static final String MYSQL_URL  = System.getProperty("db.url", "jdbc:mysql://localhost:3306/");
//    private static final String MYSQL_DATABASE_NAME = System.getProperty("db.name", "board");
//    private static final String MYSQL_USER = System.getProperty("db.user", "board");
//    private static final String MYSQL_PASSWORD = System.getProperty("db.pass", "board");

    private static final String MYSQL_URL  = "jdbc:mysql://localhost:3306/";
    private static final String MYSQL_DATABASE_NAME = "board";
    private static final String MYSQL_USER = "board";
    private static final String MYSQL_PASSWORD = "board";

    public static Connection getConnection() throws SQLException {
//        var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/board", "board", "board");
        var connection = DriverManager.getConnection(MYSQL_URL + MYSQL_DATABASE_NAME, MYSQL_USER, MYSQL_PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }

}
