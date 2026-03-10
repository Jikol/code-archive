package com.orm.dao;

import com.orm.Address;
import com.orm.Customer;
import org.hibernate.query.Query;

public class AddressRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Address\" (street, street_number, city, zip)\n" +
                    "VALUES (:streetPar, :streetNumberPar, :cityPar, (CASE WHEN :zipPar != 0 THEN :zipPar ELSE NULL END))";

    private static final String SQL_SELECT_LAST_ID =
            "SELECT MAX(a.address_id)\n" +
            "FROM \"Address\" a";

    private static final String SQL_DELETE =
            "DELETE FROM \"Address\"\n" +
            "WHERE \"Address\".address_id = :idPar";

    private static final String SQL_UPDATE =
            "UPDATE \"Address\"\n" +
            "SET \"Address\".street = CASE\n" +
            "                        WHEN :streetPar IS NOT NULL\n" +
            "                        THEN :streetPar\n" +
            "                        ELSE \"Address\".street\n" +
            "                       END,\n" +
            "    \"Address\".street_number = CASE\n" +
            "                        WHEN :streetNumberPar IS NOT NULL\n" +
            "                        THEN :streetNumberPar\n" +
            "                        ELSE \"Address\".street_number\n" +
            "                       END,\n" +
            "    \"Address\".city = CASE\n" +
            "                        WHEN :cityPar IS NOT NULL\n" +
            "                        THEN :cityPar\n" +
            "                        ELSE \"Address\".city\n" +
            "                       END,\n" +
            "    \"Address\".zip = CASE\n" +
            "                        WHEN :zipPar != 0\n" +
            "                        THEN :zipPar\n" +
            "                        ELSE \"Address\".zip\n" +
            "                       END\n" +
            "WHERE \"Address\".address_id = :idPar";

    public static int Insert(Address address, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("streetPar", address.getStreet());
        query.setParameter("streetNumberPar", address.getStreetNumber());
        query.setParameter("cityPar", address.getCity());
        if (address.getZip() != 0) {
            query.setParameter("zipPar", address.getZip());
        } else {
            query.setParameter("zipPar", 0);
        }

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idAddress, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idAddress);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Address address, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("streetPar", address.getStreet());
        query.setParameter("streetNumberPar", address.getStreetNumber());
        query.setParameter("cityPar", address.getCity());
        if (address.getZip() != 0) {
            query.setParameter("zipPar", address.getZip());
        } else {
            query.setParameter("zipPar", 0);
        }
        query.setParameter("idPar", address.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int SelectLastId(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_LAST_ID);

        Object object = query.getSingleResult();

        return Integer.parseInt(object.toString());
    }

}
