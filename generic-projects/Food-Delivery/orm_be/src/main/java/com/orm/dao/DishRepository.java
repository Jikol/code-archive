package com.orm.dao;

import com.orm.Content;
import com.orm.Dish;
import com.orm.Restaurant;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DishRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Dish\" (\"name\", weight, price, restaurant_id)\n" +
            "VALUES (:namePar, :weightPar, :pricePar, :restaurantIdPar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Dish\"\n" +
            "SET \"Dish\".\"name\" = CASE\n" +
            "                        WHEN :namePar IS NOT NULL \n" +
            "                        THEN :namePar\n" +
            "                        ELSE \"Dish\".\"name\"\n" +
            "                    END,\n" +
            "     \"Dish\".weight = CASE\n" +
            "                        WHEN :weightPar IS NOT NULL \n" +
            "                        THEN :weightPar\n" +
            "                        ELSE \"Dish\".weight\n" +
            "                    END\n" +
            "WHERE \"Dish\".dish_id = :idPar";

    private static final String SQL_SELECT =
            "SELECT d.dish_id, d.\"name\", d.weight, d.price, d.restaurant_id\n" +
            "FROM \"Dish\" d\n" +
            "WHERE d.dish_id = :idPar  ";

    private static final String SQL_INSERT_PRICE =
            "INSERT INTO \"History_dish_price\" (original_price, change_date, dish_id)\n" +
            "SELECT d.price, SYSDATE, d.dish_id\n" +
            "FROM \"Dish\" d\n" +
            "WHERE d.dish_id = :idPar";

    private static final String SQL_UPDATE_PRICE =
            "UPDATE \"Dish\"\n" +
            "SET \"Dish\".price = :pricePar\n" +
            "WHERE \"Dish\".dish_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Dish\"\n" +
            "WHERE \"Dish\".dish_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT d.dish_id, d.\"name\", d.weight, d.price, d.restaurant_id\n" +
            "FROM \"Dish\" d";

    private static final String SQL_SELECT_BY_ORDER_ID =
            "SELECT d.\"name\", d.weight, d.price, c.amount\n" +
            "FROM \"Content\" c\n" +
            "    JOIN \"Dish\" d ON c.dish_id = d.dish_id\n" +
            "WHERE c.order_id = :idPar";

    private static final String SQL_SELECT_BY_RESTAURANT_ID =
            "SELECT d.dish_id, d.\"name\", d.weight, d.price\n" +
            "FROM \"Dish\" d\n" +
            "WHERE d.restaurant_id = :idPar";

    public static int Insert(Dish dish, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("namePar", dish.getName());
        query.setParameter("weightPar", dish.getWeight());
        query.setParameter("pricePar", dish.getPrice());
        query.setParameter("restaurantIdPar", dish.getRestaurant().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Dish dish, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("namePar", dish.getName());
        query.setParameter("weightPar", dish.getWeight());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static Dish Select(int idDish, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idDish);

        Object[] object = (Object[]) query.getSingleResult();

        Dish dish = new Dish();
        dish.setId(Integer.parseInt(object[0].toString()));
        dish.setName(object[1].toString());
        dish.setWeight(Integer.parseInt(object[2].toString()));
        dish.setPrice(Integer.parseInt(object[3].toString()));
        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[4].toString()));
        dish.setRestaurant(restaurant);

        return dish;
    }

    public static List<Dish> SelectByOrderId(int idOrder, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_BY_ORDER_ID);
        query.setParameter("idPar", idOrder);

        List<Dish> dishes = new ArrayList<Dish>();

        List<Object[]> rows = query.list();
        Dish dish;
        Content content;

        for (Object[] row : rows) {
            dish = new Dish();
            dish.setName(row[0].toString());
            dish.setWeight(Integer.parseInt(row[1].toString()));
            dish.setPrice(Integer.parseInt(row[2].toString()));
            content = new Content();
            content.setAmount(Integer.parseInt(row[3].toString()));
            dish.setContent(content);

            dishes.add(dish);
        }

        return dishes;
    }

    public static List<Dish> SelectByRestaurantId(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_BY_RESTAURANT_ID);
        query.setParameter("idPar", idRestaurant);

        List<Dish> dishes = new ArrayList<Dish>();

        List<Object[]> rows = query.list();
        Dish dish;
        Content content;

        for (Object[] row : rows) {
            dish = new Dish();
            dish.setId(Integer.parseInt(row[0].toString()));
            dish.setName(row[1].toString());
            dish.setWeight(Integer.parseInt(row[2].toString()));
            dish.setPrice(Integer.parseInt(row[3].toString()));

            dishes.add(dish);
        }

        return dishes;
    }

    public static int DishPriceChange(Dish dish, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT_PRICE);
        query.setParameter("idPar", dish.getId());
        db.ExecuteQuery(query);

        query = db.getSession().createSQLQuery(SQL_UPDATE_PRICE);
        query.setParameter("idPar", dish.getId());
        query.setParameter("pricePar", dish.getPrice());
        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idDish, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idDish);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static List<Dish> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Dish> dishes = new ArrayList<Dish>();

        List<Object[]> rows = query.list();
        Dish dish;
        Restaurant restaurant;

        for (Object[] row : rows) {
            dish = new Dish();
            dish.setId(Integer.parseInt(row[0].toString()));
            dish.setName(row[1].toString());
            dish.setWeight(Integer.parseInt(row[2].toString()));
            dish.setPrice(Integer.parseInt(row[3].toString()));
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[4].toString()));
            dish.setRestaurant(restaurant);

            dishes.add(dish);
        }

        return dishes;
    }
}
