package com.dataaccesslayer;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Map;

public class Database {
    private static Database instance = null;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    private final String url;
    private final String userName;
    private final String password;

    private Database(final String driver, final String hostName, final String port,
                     final String database, final String userName, final String password) {
        this.url = "jdbc:" + driver + "://" + hostName + ":" + port + "/" + database;
        this.userName = userName;
        this.password = password;
    }

    public static Database getDatabase() {
        return instance;
    }

    public static Database Create(final String driver, final String hostName, final String port,
                                  final String database, final String userName, final String password) {
        if (instance == null) {
            instance = new Database(driver, hostName, port, database, userName, password);
            return instance;
        } else {
            return instance;
        }
    }

    public static void Destroy() {
        if (instance != null) {
            instance = null;
        }
    }

    public void BeginConnection() {
        try {
            if (connection == null && statement == null) {
                connection = DriverManager.getConnection(url, userName, password);
                connection.setAutoCommit(false);
                statement = connection.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getCause());
        }
    }

    public void EndConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
        } catch (SQLException ex) {
            if (ex.getCause() == null) {
                System.out.println("Database not connected");
            }
        }
    }

    public int ExecuteUpdate(final String query) throws SQLException {
        int affectedRows = statement.executeUpdate(query);
        if (affectedRows == 0) {
            throw new SQLException("None rows has been affected due to failed insert");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("No key obtained from inserted entity");
        }
    }

    public int ExecutePreparedUpdate(final String query, final Map parameters) throws SQLException {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        parameters.forEach((index, parameterEntry) -> {
            var parameter = (AbstractMap.SimpleEntry<Object, Object>) parameterEntry;
            if (parameter.getKey().equals(Integer.class)) {
                try {
                    preparedStatement.setInt((Integer) index, (Integer) parameter.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (parameter.getKey().equals(String.class)) {
                try {
                    preparedStatement.setString((Integer) index, (String) parameter.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (parameter.getKey().equals(byte[].class)) {
                try {
                    preparedStatement.setBytes((Integer) index, (byte[]) parameter.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (parameter.getKey().equals(Boolean.class)) {
                try {
                    preparedStatement.setBoolean((Integer) index, (Boolean) parameter.getValue());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (parameter.getKey().equals(java.util.Date.class)) {
                try {
                    java.util.Date utilDate = (java.util.Date) parameter.getValue();
                    if (utilDate == null) {
                        preparedStatement.setDate((Integer) index, null);
                    } else {
                        Date sqlDate = new Date(utilDate.getTime());
                        preparedStatement.setDate((Integer) index, sqlDate);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("None rows has been affected due to failed insert");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("No key obtained from inserted entity");
        }
    }

    public ResultSet ExecuteSelect(final String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ResultSet ExecutePreparedSelect(final String query, final Map parameters) {
        try {
            preparedStatement = connection.prepareStatement(query);
            parameters.forEach((index, parameterEntry) -> {
                var parameter = (AbstractMap.SimpleEntry<Object, Object>) parameterEntry;
                if (parameter.getKey().equals(Integer.class)) {
                    try {
                        preparedStatement.setInt((Integer) index, (Integer) parameter.getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (parameter.getKey().equals(String.class)) {
                    try {
                        preparedStatement.setString((Integer) index, (String) parameter.getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (parameter.getKey().equals(byte[].class)) {
                    try {
                        preparedStatement.setBytes((Integer) index, (byte[]) parameter.getValue());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (parameter.getKey().equals(Boolean.class)) {
                    try {
                        preparedStatement.setBoolean((Integer) index, (Boolean) parameter.getValue());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (parameter.getKey().equals(Date.class)) {
                    try {
                        preparedStatement.setDate((Integer) index, (Date) parameter.getValue());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getLocalizedMessage());
            return null;
        }
    }

    public void Rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void Commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
