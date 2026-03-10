package com.orm.dao;

import com.orm.Restaurant;
import com.orm.RestaurantAll;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Restaurant\" (\"name\", \"desc\", food_type, delivery_time ) \n" +
            "VALUES (:namePar,:descPar,:foodTypePar,:deliveryTimePar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Restaurant\"\n" +
            "SET \"Restaurant\".\"name\" = CASE\n" +
            "                            WHEN :namePar IS NOT NULL\n" +
            "                            THEN :namePar\n" +
            "                            ELSE \"Restaurant\".\"name\"\n" +
            "                         END,\n" +
            "    \"Restaurant\".\"desc\" = CASE\n" +
            "                            WHEN :descPar IS NOT NULL\n" +
            "                            THEN :descPar\n" +
            "                            ELSE \"Restaurant\".\"desc\"\n" +
            "                         END,\n" +
            "    \"Restaurant\".food_type = CASE\n" +
            "                               WHEN :foodTypePar IS NOT NULL\n" +
            "                               THEN :foodTypePar\n" +
            "                               ELSE \"Restaurant\".food_type\n" +
            "                             END,\n" +
            "    \"Restaurant\".delivery_time = CASE\n" +
            "                                   WHEN :deliveryTimePar != 0\n" +
            "                                   THEN :deliveryTimePar\n" +
            "                                   ELSE \"Restaurant\".delivery_time\n" +
            "                                 END\n" +
            "WHERE \"Restaurant\".restaurant_id = :idPar\n";

    private static final String SQL_DELETE =
            "DELETE FROM \"Restaurant\"\n" +
            "WHERE \"Restaurant\".restaurant_id = :idPar";

    private static final String SQL_SELECT =
            "SELECT r.restaurant_id, r.\"name\", r.\"desc\", r.food_type, r.delivery_time, r.rating\n" +
            "FROM \"Restaurant\" r\n" +
            "WHERE r.restaurant_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT r.restaurant_id, r.\"name\", r.\"desc\", r.food_type, r.delivery_time, r.rating, e1.ec, v1.vc, o1.oc\n" +
            "FROM \"Restaurant\" r\n" +
            "JOIN (\n" +
            "SELECT e.restaurant_id, COUNT(*) AS ec\n" +
            "FROM \"Employee\" e\n" +
            "GROUP BY e.restaurant_id\n" +
            ") e1 ON e1.restaurant_id = r.restaurant_id\n" +
            "JOIN (\n" +
            "SELECT v.restaurant_id, COUNT(*) AS vc\n" +
            "FROM \"Vehicle\" v\n" +
            "GROUP BY v.restaurant_id\n" +
            ") v1 ON v1.restaurant_id = r.restaurant_id\n" +
            "JOIN (\n" +
            "SELECT o.restaurant_id, COUNT(*) AS oc\n" +
            "FROM \"Order\" o\n" +
            "GROUP BY o.restaurant_id\n" +
            ") o1 ON o1.restaurant_id = r.restaurant_id";

    private static final String SQL_REWARD_PROCEDURE =
            "CALL rewardBestRestaurant(:idPar)";

    private static final String SQL_GET_AVG_RATING =
            "SELECT AVG(r.rating)\n" +
            "FROM \"Rating\" r\n" +
            "WHERE r.restaurant_id = :idPar";

    private static final String SQL_SET_AVG_RATING =
            "UPDATE \"Restaurant\"\n" +
            "SET \"Restaurant\".rating = :ratingPar\n" +
            "WHERE \"Restaurant\".restaurant_id = :idPar";

    public static int Insert(Restaurant restaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("namePar", restaurant.getName());
        query.setParameter("descPar", restaurant.getDesc());
        query.setParameter("foodTypePar", restaurant.getFoodType());
        query.setParameter("deliveryTimePar", restaurant.getDeliveryTime());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Restaurant restaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("namePar", restaurant.getName());
        query.setParameter("descPar", restaurant.getDesc());
        query.setParameter("foodTypePar", restaurant.getFoodType());
        query.setParameter("deliveryTimePar", restaurant.getDeliveryTime());
        query.setParameter("idPar", restaurant.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idRestaurant);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static Restaurant Select(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idRestaurant);

        Object[] object = (Object[]) query.getSingleResult();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[0].toString()));
        restaurant.setName(object[1].toString());
        if (object[2] != null) {
            restaurant.setDesc(object[2].toString());
        }
        restaurant.setFoodType(object[3].toString());
        restaurant.setDeliveryTime(Integer.parseInt(object[4].toString()));
        if (object[5] != null) {
            restaurant.setRating(Integer.parseInt(object[5].toString()));
        }

        return restaurant;
    }

    public static List<RestaurantAll> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<RestaurantAll> restaurantsAll = new ArrayList<RestaurantAll>();

        List<Object[]> rows = query.list();
        RestaurantAll restaurantAll;

        for (Object[] row : rows) {
            restaurantAll = new RestaurantAll();
            restaurantAll.setId(Integer.parseInt(row[0].toString()));
            restaurantAll.setName(row[1].toString());
            if (row[2] != null) {
                restaurantAll.setDesc(row[2].toString());
            }
            restaurantAll.setFoodType(row[3].toString());
            restaurantAll.setDeliveryTime(Integer.parseInt(row[4].toString()));
            if (row[5] != null) {
                restaurantAll.setRating(Integer.parseInt(row[5].toString()));
            }
            restaurantAll.setEmployeeCount(Integer.parseInt(row[6].toString()));
            restaurantAll.setVehicleCount(Integer.parseInt(row[7].toString()));
            restaurantAll.setOrderCount(Integer.parseInt(row[8].toString()));

            restaurantsAll.add(restaurantAll);
        }

        return restaurantsAll;
    }

    public static int RewardBestRestaurant(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_REWARD_PROCEDURE);
        query.setParameter("idPar", idRestaurant);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int SetAvgRating(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_GET_AVG_RATING);
        query.setParameter("idPar", idRestaurant);

        Object object = query.getSingleResult();

        query = db.getSession().createSQLQuery(SQL_SET_AVG_RATING);
        query.setParameter("ratingPar", object.toString());
        query.setParameter("idPar", idRestaurant);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

}
