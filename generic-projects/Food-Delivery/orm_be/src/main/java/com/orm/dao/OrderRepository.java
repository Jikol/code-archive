package com.orm.dao;

import com.orm.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private static Query query;

    private static final String SQL_INSERT_ORDER =
        "INSERT INTO \"Order\" (creation_time, payment_type, tip, restaurant_id, employee_id, customer_id)\n" +
        "VALUES (SYSDATE, :paymentTypPar, :tipPar, :restaurantIdPar, :employeeIdPar, :customerIdPar)";

    private static final String SQL_INSERT_CONTENT =
            "INSERT INTO \"Content\" (order_id, dish_id, amount)\n" +
            "VALUES (:orderIdPar, :dishIdPar, :amount)";

    private static final String SQL_SELECT_LAST_ORDER =
            "SELECT MAX(o.order_id)\n" +
            "FROM \"Order\" o";

    private static final String SQL_SELECT =
            "SELECT o.order_id, TO_CHAR(o.creation_time, 'HH24:MI'), o.handover_time, o.delivery_time, o.payment_type, o.tip, r.\"name\", r.\"desc\", r.rating, r.delivery_time AS pom, o.employee_id, o.customer_id\n" +
            "FROM \"Order\" o\n" +
            "    JOIN \"Restaurant\" r ON o.restaurant_id = r.restaurant_id\n" +
            "WHERE o.order_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT o.order_id, o.creation_time, o.handover_time, o.delivery_time, o.payment_type, o.tip, o.restaurant_id, o.employee_id, o.customer_id\n" +
            "FROM \"Order\" o\n";

    private static final String SQL_SELECT_ALL_ID =
            "SELECT o.order_id, TO_CHAR(o.creation_time, 'HH24:MI'), TO_CHAR(o.handover_time, 'HH24:MI'), o.delivery_time, o.payment_type, o.tip, r.restaurant_id, r.\"name\", r.\"desc\", r.food_type, r.delivery_time AS pom, r.rating\n" +
            "FROM \"Order\" o\n" +
            "    JOIN \"Restaurant\" r ON o.restaurant_id = r.restaurant_id\n" +
            "WHERE o.customer_id = :idPar";

    private static final String SQL_SELECT_ALL_ACTIVE_ID =
            "SELECT o.order_id, TO_CHAR(o.creation_time, 'HH24:MI'), TO_CHAR(o.handover_time, 'HH24:MI'), o.delivery_time, o.payment_type, o.tip, r.restaurant_id, r.\"name\", r.\"desc\", r.food_type, r.delivery_time AS pom, r.rating\n" +
            "FROM \"Order\" o\n" +
            "    JOIN \"Restaurant\" r ON o.restaurant_id = r.restaurant_id\n" +
            "WHERE o.customer_id = :idPar AND o.delivery_time IS NULL";

    private static final String SQL_DELETE =
            "DELETE FROM \"Order\"\n" +
            "WHERE \"Order\".order_id = :idPar";

    private static final String SQL_UPDATE_PROCEDURE =
            "CALL orderChange(:orderIdPar, :paymentTypePar, :tipPar, :newDishIdPar, :oldDishIdPar, :amountPar)";

    public static int Insert(Order order, List<Content> contents, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT_ORDER);
        query.setParameter("paymentTypPar", order.getPaymentType());
        query.setParameter("tipPar", order.getTip());
        query.setParameter("restaurantIdPar", order.getRestaurant().getId());
        query.setParameter("employeeIdPar", order.getEmployee().getId());
        query.setParameter("customerIdPar", order.getCustomer().getId());
        int rows = db.ExecuteQuery(query);

        query = db.getSession().createSQLQuery(SQL_SELECT_LAST_ORDER);

        Object object = query.getSingleResult();
        order.setId(Integer.parseInt(object.toString()));

        for (Content content : contents) {
            content.setOrder(order);
            query = db.getSession().createSQLQuery(SQL_INSERT_CONTENT);
            query.setParameter("orderIdPar", content.getOrder().getId());
            query.setParameter("dishIdPar", content.getDish().getId());
            query.setParameter("amount", content.getAmount());
            db.ExecuteQuery(query);
        }

        db.EndTransaction();
        return rows;
    }

    public static Order Select(int idOrder, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idOrder);

        Object[] object = (Object[]) query.getSingleResult();

        Order order = new Order();
        order.setId(Integer.parseInt(object[0].toString()));
        order.setCreationTime(object[1].toString());
        if (object[2] != null) {
            order.setHandoverTime(object[2].toString());
        }
        if (object[3] != null) {
            order.setDeliveryTime(object[3].toString());
        }
        order.setPaymentType(object[4].toString());
        if (object[5] != null) {
            order.setTip(Integer.parseInt(object[5].toString()));
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setName(object[6].toString());
        if (object[7] != null) {
            restaurant.setDesc(object[7].toString());
        }
        if (object[8] != null) {
            restaurant.setRating(Integer.parseInt(object[8].toString()));
        }
        restaurant.setDeliveryTime(Integer.parseInt(object[9].toString()));
        Employee employee = new Employee();
        employee.setId(Integer.parseInt(object[10].toString()));
        Customer customer = new Customer();
        customer.setId(Integer.parseInt(object[11].toString()));
        order.setRestaurant(restaurant);
        order.setEmployee(employee);
        order.setCustomer(customer);

        return order;
    }

    public static List<Order> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Order> orders = new ArrayList<Order>();

        List<Object[]> rows = query.list();
        Order order = new Order();
        Restaurant restaurant;
        Customer customer;
        Employee employee;

        for (Object[] row : rows) {
            order.setId(Integer.parseInt(row[0].toString()));
            order.setCreationTime(row[1].toString());
            if (row[2] != null) {
                order.setHandoverTime(row[2].toString());
            }
            if (row[3] != null) {
                order.setDeliveryTime(row[3].toString());
            }
            order.setPaymentType(row[4].toString());
            if (row[5] != null) {
                order.setTip(Integer.parseInt(row[5].toString()));

            }
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[6].toString()));
            employee = new Employee();
            employee.setId(Integer.parseInt(row[7].toString()));
            customer = new Customer();
            customer.setId(Integer.parseInt(row[8].toString()));
            order.setRestaurant(restaurant);
            order.setEmployee(employee);
            order.setCustomer(customer);

            orders.add(order);
        }

        return orders;
    }

    public static List<Order> SelectAllbyID(int id, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL_ID);
        query.setParameter("idPar", id);
        List<Order> orders = new ArrayList<Order>();

        List<Object[]> rows = query.list();
        Order order;
        Restaurant restaurant;

        for (Object[] row : rows) {
            order = new Order();
            order.setId(Integer.parseInt(row[0].toString()));
            order.setCreationTime(row[1].toString());
            if (row[2] != null) {
                order.setHandoverTime(row[2].toString());
            }
            if (row[3] != null) {
                order.setDeliveryTime(row[3].toString());
            }
            if (row[4].toString().equals("card")) {
                order.setPaymentType("Plat. karta");
            } else if (row[4].toString().equals("cash")) {
                order.setPaymentType("Penězi");
            } else {
                order.setPaymentType("Online");
            }
            if (row[5] != null) {
                order.setTip(Integer.parseInt(row[5].toString()));
            }
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[6].toString()));
            restaurant.setName(row[7].toString());
            if (row[8] != null) {
                restaurant.setDesc(row[8].toString());
            }
            restaurant.setFoodType(row[9].toString());
            restaurant.setDeliveryTime(Integer.parseInt(row[10].toString()));
            if (row[11] != null) {
                restaurant.setRating(Integer.parseInt(row[11].toString()));
            }
            order.setRestaurant(restaurant);

            orders.add(order);
        }

        return orders;
    }

    public static List<Order> SelectAllActiveByID(int id, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL_ACTIVE_ID);
        query.setParameter("idPar", id);
        List<Order> orders = new ArrayList<Order>();

        List<Object[]> rows = query.list();
        Order order;
        Restaurant restaurant;

        for (Object[] row : rows) {
            order = new Order();
            order.setId(Integer.parseInt(row[0].toString()));
            order.setCreationTime(row[1].toString());
            if (row[2] != null) {
                order.setHandoverTime(row[2].toString());
            }
            if (row[3] != null) {
                order.setDeliveryTime(row[3].toString());
            }
            if (row[4].toString().equals("card")) {
                order.setPaymentType("Plat. karta");
            } else if (row[4].toString().equals("cash")) {
                order.setPaymentType("Penězi");
            } else {
                order.setPaymentType("Online");
            }
            if (row[5] != null) {
                order.setTip(Integer.parseInt(row[5].toString()));
            }
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[6].toString()));
            restaurant.setName(row[7].toString());
            if (row[8] != null) {
                restaurant.setDesc(row[8].toString());
            }
            restaurant.setFoodType(row[9].toString());
            restaurant.setDeliveryTime(Integer.parseInt(row[10].toString()));
            if (row[11] != null) {
                restaurant.setRating(Integer.parseInt(row[11].toString()));
            }
            order.setRestaurant(restaurant);

            orders.add(order);
        }

        return orders;
    }

    public static int Update(Order order, int oldDishId, int newDishId, int amount, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE_PROCEDURE);
        query.setParameter("orderIdPar", order.getId());
        query.setParameter("paymentTypePar", order.getPaymentType());
        query.setParameter("tipPar", order.getTip());
        query.setParameter("newDishIdPar", newDishId);
        query.setParameter("oldDishIdPar", oldDishId);
        query.setParameter("amountPar", amount);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idOrder, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idOrder);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }
}
