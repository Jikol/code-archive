package com.orm.dao;

import com.orm.Customer;
import com.orm.Rating;
import com.orm.Restaurant;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RatingRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Rating\" (rating, rating_date, rating_comment, restaurant_id, customer_id)\n" +
            "SELECT :ratingPar, SYSDATE, :ratingCommentPar, :restaurantIdPar, :customerIdPar\n" +
            "FROM (\n" +
            "    SELECT COUNT(*) AS pocet\n" +
            "    FROM \"Order\" o\n" +
            "    WHERE o.customer_id = :customerIdPar AND o.restaurant_id = :restaurantIdPar AND o.delivery_time IS NOT NULL) t\n" +
            "WHERE t.pocet > 0";

    private static final String SQL_UPDATE =
            "UPDATE \"Rating\"\n" +
            "SET \"Rating\".rating = CASE\n" +
            "                        WHEN :ratingPar IS NOT NULL\n" +
            "                        THEN :ratingPar\n" +
            "                        ELSE \"Rating\".rating\n" +
            "                      END,\n" +
            "    \"Rating\".rating_comment = CASE\n" +
            "                                WHEN :ratingCommentPar IS NOT NULL\n" +
            "                                THEN :ratingCommentPar\n" +
            "                                ELSE \"Rating\".rating_comment\n" +
            "                              END\n" +
            "WHERE \"Rating\".rating_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Rating\"\n" +
            "WHERE \"Rating\".rating_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT r.rating_id, r.rating, r.rating_date, r.rating_comment, r.restaurant_id, r.customer_id\n" +
            "FROM \"Rating\" r";

    private static final String SQL_SELECT =
            "SELECT r.rating_id, r.rating, r.rating_date, r.rating_comment, r.restaurant_id, r.customer_id\n" +
            "FROM \"Rating\" r\n" +
            "WHERE r.rating_id = :idPar";

    public static int Insert(Rating rating, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("ratingPar", rating.getRating());
        query.setParameter("ratingCommentPar", rating.getRatingComment());
        query.setParameter("restaurantIdPar", rating.getRestaurant().getId());
        query.setParameter("customerIdPar", rating.getCustomer().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        RestaurantRepository.SetAvgRating(rating.getRestaurant().getId(), db);

        return rows;
    }

    public static int Update(Rating rating, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("ratingPar", rating.getRating());
        query.setParameter("ratingCommentPar", rating.getRatingComment());
        query.setParameter("idPar", rating.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        RestaurantRepository.SetAvgRating(rating.getRestaurant().getId(), db);

        return rows;
    }

    public static int Delete(int idRating, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        Rating rating = Select(idRating, db);

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idRating);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        RestaurantRepository.SetAvgRating(rating.getRestaurant().getId(), db);

        return rows;
    }

    public static List<Rating> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Rating> ratings = new ArrayList<Rating>();

        List<Object[]> rows = query.list();
        Rating rating;
        Restaurant restaurant;
        Customer customer;

        for (Object[] row : rows) {
            rating = new Rating();
            rating.setId(Integer.parseInt(row[0].toString()));
            rating.setRating(Integer.parseInt(row[1].toString()));
            rating.setRatingDate(row[2].toString());
            if (row[3] != null) {
                rating.setRatingComment(row[3].toString());
            }
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[4].toString()));
            customer = new Customer();
            customer.setId(Integer.parseInt(row[5].toString()));
            rating.setRestaurant(restaurant);
            rating.setCustomer(customer);
            ratings.add(rating);
        }

        return ratings;
    }

    public static Rating Select(int idRating, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idRating);

        Object[] object = (Object[]) query.getSingleResult();

        Rating rating = new Rating();
        rating.setId(Integer.parseInt(object[0].toString()));
        rating.setRating(Integer.parseInt(object[1].toString()));
        rating.setRatingDate(object[2].toString());
        if (object[3] != null) {
            rating.setRatingComment(object[3].toString());
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[4].toString()));
        Customer customer = new Customer();
        customer.setId(Integer.parseInt(object[5].toString()));
        rating.setRestaurant(restaurant);
        rating.setCustomer(customer);

        return rating;
    }
}
