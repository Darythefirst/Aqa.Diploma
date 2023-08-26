package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    public SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        String url = System.getProperty("db.url");
        String user = System.getProperty("db.user");
        String pass = System.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var statusSql = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        String paymentStatus = runner.query(conn, statusSql, new ScalarHandler<String>());
        return paymentStatus;
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var statusSql = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        String creditStatus = runner.query(conn, statusSql, new ScalarHandler<String>());
        return creditStatus;
    }

    @SneakyThrows
    public static void cleanDB() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
        runner.execute(connection, "DELETE FROM order_entity");
    }
}
