package com.orm.dao;

import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

import javax.persistence.Query;

public class Database {

    private static Database instance = null;
    private Configuration connection;
    private Session session;
    private Transaction transaction = null;

    private Database() {
        connection = new Configuration();
        connection.setProperty("hibernate.connection.driver_class", "oracle.jdbc.driver.OracleDriver");
        connection.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@dbsys.cs.vsb.cz:1521:oracle");
        connection.setProperty("hibernate.connection.username", "jav0032");
        connection.setProperty("hibernate.connection.password", "230998");
        connection.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle8iDialect");
        connection.setProperty("show_sql", "true");
    }

    private Database(String hostName, String port, String SID, String userName, String password) {
        connection = new Configuration();
        connection.setProperty("hibernate.connection.driver_class", "oracle.jdbc.driver.OracleDriver");
        connection.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + SID);
        connection.setProperty("hibernate.connection.username", userName);
        connection.setProperty("hibernate.connection.password", password);
        connection.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle8iDialect");
        connection.setProperty("show_sql", "true");
    }

    public static Database create(String hostName, String port, String SID, String userName, String password) {
        if (instance == null) {
            instance = new Database(hostName, port, SID, userName, password);
            return instance;
        } else {
            return null;
        }
    }

    public static Database create() {
        if (instance == null) {
            instance = new Database();
            return instance;
        } else {
            return null;
        }
    }

    public void Connect() {
        try {
            session = connection.buildSessionFactory().openSession();
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
    }

    public void Close() {
        try {
            session.close();
        } catch (Exception ex) {
            if (ex.getCause() == null) {
                System.out.println("Database not connected");
            }
        }
    }

    public int ExecuteQuery(Query query) {
        int rows = 0;

        try {
            rows = query.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            Rollback();
        }

        return rows;
    }

    public void BeginTransaction() {
        transaction = session.beginTransaction();
    }

    public void EndTransaction() {
        try {
            transaction.commit();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void Rollback() {
        transaction.rollback();
    }

    public Session getSession() {
        return session;
    }

    @Override
    public java.lang.String toString() {
        return "Database{" +
                "connection=" + connection +
                ", session=" + session +
                ", transaction=" + transaction +
                '}';
    }

    public static Database getDatabase() {
        return instance;
    }
}
