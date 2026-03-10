package com.orm.dao;

import com.orm.Restaurant;
import com.orm.Vehicle;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Vehicle\" (brand, \"type\", license_plate, restaurant_id)\n" +
            "VALUES (:brandPar, :typePar, :licensePlatePar, :restaurantIdPar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Vehicle\"\n" +
            "SET \"Vehicle\".brand = CASE\n" +
            "                        WHEN :brandPar IS NOT NULL\n" +
            "                        THEN :brandPar\n" +
            "                        ELSE \"Vehicle\".brand\n" +
            "                      END,\n" +
            "    \"Vehicle\".\"type\" = CASE\n" +
            "                        WHEN :typePar IS NOT NULL\n" +
            "                        THEN :typePar\n" +
            "                        ELSE \"Vehicle\".\"type\"\n" +
            "                      END,\n" +
            "     \"Vehicle\".license_plate = CASE\n" +
            "                                WHEN :licensePlatePar != \n" +
            "                                THEN :licensePlatePar\n" +
            "                                ELSE \"Vehicle\".license_plate\n" +
            "                               END\n" +
            "WHERE \"Vehicle\".vehicle_id = :idPar";

    private static final String SQL_SELECT =
            "SELECT v.vehicle_id, v.brand, v.\"type\", v.license_plate, v.restaurant_id\n" +
            "FROM \"Vehicle\" v\n" +
            "WHERE v.vehicle_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Vehicle\"\n" +
            "WHERE \"Vehicle\".vehicle_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT v.vehicle_id, v.brand, v.\"type\", v.license_plate, r.\"name\"\n" +
            "FROM \"Vehicle\" v\n" +
            "    JOIN \"Restaurant\" r ON v.restaurant_id = r.restaurant_id";

    private static final String SQL_SELECT_ALL_BY_RESTAURANT_ID =
            "SELECT v.vehicle_id, v.brand, v.\"type\", v.license_plate, r.\"name\"\n" +
            "FROM \"Vehicle\" v\n" +
            "    JOIN \"Restaurant\" r ON v.restaurant_id = r.restaurant_id\n" +
            "WHERE r.restaurant_id = :idPar AND v.vehicle_id NOT IN (\n" +
            "    SELECT d.vehicle_id\n" +
            "    FROM \"Drive\" d)";

    public static int Insert(Vehicle vehicle, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("brandPar", vehicle.getBrand());
        query.setParameter("typePar", vehicle.getType());
        query.setParameter("licensePlatePar", vehicle.getLicensePlate());
        query.setParameter("restaurantIdPar", vehicle.getRestaurant().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Vehicle vehicle, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("brandPar", vehicle.getBrand());
        query.setParameter("typePar", vehicle.getType());
        query.setParameter("licensePlatePar", vehicle.getLicensePlate());
        query.setParameter("idPar", vehicle.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static Vehicle Select(int idVehicle, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idVehicle);

        Object[] object = (Object[]) query.getSingleResult();

        Vehicle vehicle = new Vehicle();
        vehicle.setId(Integer.parseInt(object[0].toString()));
        vehicle.setBrand(object[1].toString());
        vehicle.setType(object[2].toString());
        vehicle.setLicensePlate(object[3].toString());
        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[4].toString()));
        vehicle.setRestaurant(restaurant);

        return vehicle;
    }

    public static int Delete(int idVehicle, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idVehicle);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static List<Vehicle> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Vehicle> vehiclese = new ArrayList<Vehicle>();

        List<Object[]> rows = query.list();
        Vehicle vehicle;
        Restaurant restaurant;

        for (Object[] row : rows) {
            vehicle = new Vehicle();
            vehicle.setId(Integer.parseInt(row[0].toString()));
            vehicle.setBrand(row[1].toString());
            if (row[2].toString().equals("car")) {
                vehicle.setType("Auto");
            } else {
                vehicle.setType("Motorka");
            }
            vehicle.setLicensePlate(row[3].toString());
            restaurant = new Restaurant();
            restaurant.setName(row[4].toString());
            vehicle.setRestaurant(restaurant);

            vehiclese.add(vehicle);
        }

        return vehiclese;
    }

    public static List<Vehicle> SelectAllByRestaurantId(int idRestaurant, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL_BY_RESTAURANT_ID);
        query.setParameter("idPar", idRestaurant);
        List<Vehicle> vehiclese = new ArrayList<Vehicle>();

        List<Object[]> rows = query.list();
        Vehicle vehicle;
        Restaurant restaurant;

        for (Object[] row : rows) {
            vehicle = new Vehicle();
            vehicle.setId(Integer.parseInt(row[0].toString()));
            vehicle.setBrand(row[1].toString());
            if (row[2].toString().equals("car")) {
                vehicle.setType("Auto");
            } else {
                vehicle.setType("Motorka");
            }
            vehicle.setLicensePlate(row[3].toString());
            restaurant = new Restaurant();
            restaurant.setName(row[4].toString());
            vehicle.setRestaurant(restaurant);

            vehiclese.add(vehicle);
        }

        return vehiclese;
    }
}
