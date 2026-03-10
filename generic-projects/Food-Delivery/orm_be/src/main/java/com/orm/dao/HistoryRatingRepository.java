package com.orm.dao;

import com.orm.HistoryRating;
import com.orm.Restaurant;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class HistoryRatingRepository {

    private static Query query;

    private static final String SQL_SELECT_RATING =
            "SELECT r.rating\n" +
                    "FROM \"Restaurant\" r\n" +
                    "WHERE r.restaurant_id = :idPar";

    private static final String SQL_INSERT_HISTORY_RATING =
            "INSERT INTO \"History_rating\" (rating, change_date, restaurant_id)\n" +
                    "VALUES (:ratingPar, SYSDATE, :idPar)";

    private static final String SQL_SELECT =
            "SELECT r.restaurant_id, r.\"name\", h.history_rating_id, h.rating, h.change_date\n" +
            "FROM \"History_rating\" h\n" +
            "    JOIN \"Restaurant\" r ON h.restaurant_id = r.restaurant_id\n" +
            "WHERE h.history_rating_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT r.restaurant_id, r.\"name\", h.history_rating_id, h.rating, h.change_date\n" +
            "FROM \"History_rating\" h\n" +
            "    JOIN \"Restaurant\" r ON h.restaurant_id = r.restaurant_id\n";

    public static HistoryRating Select(int idHistoryRating, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idHistoryRating);

        Object[] object = (Object[]) query.getSingleResult();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[0].toString()));
        restaurant.setName(object[1].toString());

        HistoryRating historyRating = new HistoryRating();
        historyRating.setId(Integer.parseInt(object[2].toString()));
        historyRating.setRating(Integer.parseInt(object[3].toString()));
        historyRating.setChangeDate(object[4].toString());
        historyRating.setRestaurant(restaurant);

        return historyRating;
    }

    public static List<HistoryRating> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<HistoryRating> historyRatings = new ArrayList<HistoryRating>();

        List<Object[]> rows = query.list();
        Restaurant restaurant;
        HistoryRating historyRating;

        for (Object[] row : rows) {
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[0].toString()));
            restaurant.setName(row[1].toString());

            historyRating = new HistoryRating();
            historyRating.setId(Integer.parseInt(row[2].toString()));
            historyRating.setRating(Integer.parseInt(row[3].toString()));
            historyRating.setChangeDate(row[4].toString());
            historyRating.setRestaurant(restaurant);

            historyRatings.add(historyRating);
        }

        return historyRatings;
    }

    public static int RatingHistorization(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_SELECT_RATING);
        query.setParameter("idPar", idRestaurant);

        Object object = query.getSingleResult();
        int rating = 0;
        int rows = 0;
        try {
            rating = (Integer.parseInt(object.toString()));

            query = db.getSession().createSQLQuery(SQL_INSERT_HISTORY_RATING);
            query.setParameter("ratingPar", rating);
            query.setParameter("idPar", idRestaurant);

            rows = db.ExecuteQuery(query);
            db.EndTransaction();

            return rows;
        } catch (Exception ex) {
            System.out.println("Restaurant has not been rated yet");
        } finally {
            return rows;
        }
    }
}
