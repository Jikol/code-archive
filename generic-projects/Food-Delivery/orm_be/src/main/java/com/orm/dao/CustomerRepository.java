package com.orm.dao;

import com.orm.Address;
import com.orm.Customer;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Customer\" (\"name\", surname, gender, phone_number, birth_date, address_id)\n" +
            "VALUES (:namePar, :surnamePar, :genderPar, :phoneNumberPar, TO_DATE(:birthDatePar, 'yyyy-mm-dd'), (CASE WHEN :idPar != 0 THEN :idPar ELSE NULL END))";

    private static final String SQL_UPDATE =
            "UPDATE \"Customer\"\n" +
            "SET \"Customer\".\"name\" = CASE\n" +
            "                            WHEN :namePar IS NOT NULL\n" +
            "                            THEN :namePar\n" +
            "                            ELSE \"Customer\".\"name\"\n" +
            "                        END,\n" +
            "    \"Customer\".surname = CASE\n" +
            "                            WHEN :surnamePar IS NOT NULL\n" +
            "                            THEN :surnamePar\n" +
            "                            ELSE \"Customer\".surname\n" +
            "                        END,\n" +
            "    \"Customer\".gender = CASE\n" +
            "                            WHEN :genderPar IS NOT NULL\n" +
            "                            THEN :genderPar\n" +
            "                            ELSE \"Customer\".gender\n" +
            "                        END,\n" +
            "    \"Customer\".phone_number = CASE\n" +
            "                            WHEN :phoneNumberPar IS NOT NULL\n" +
            "                            THEN :phoneNumberPar\n" +
            "                            ELSE \"Customer\".phone_number\n" +
            "                        END,\n" +
            "    \"Customer\".birth_date = CASE\n" +
            "                            WHEN :birthDatePar IS NOT NULL\n" +
            "                            THEN TO_DATE(:birthDatePar, 'dd-mm-yyyy')\n" +
            "                            ELSE \"Customer\".birth_date\n" +
            "                        END,\n" +
            "    \"Customer\".address_id = CASE\n" +
            "                            WHEN :addressIdPar != 0\n" +
            "                            THEN :addressIdPar\n" +
            "                            ELSE \"Customer\".address_id\n" +
            "                        END\n" +
            "WHERE \"Customer\".customer_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Customer\"\n" +
            "WHERE \"Customer\".customer_id = :idPar";

    private static final String SQL_SELECT_ID =
            "SELECT c.customer_id, c.\"name\", c.surname, c.gender, c.phone_number, c.birth_date, c.address_id, a.street, a.street_number, a.city, a.zip\n" +
            "FROM \"Customer\" c\n" +
            "    JOIN \"Address\" a ON a.address_id = c.address_id\n" +
            "WHERE c.customer_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT c.customer_id, c.\"name\", c.surname, c.gender, c.phone_number, c.birth_date, c.address_id, a.street, a.street_number, a.city, a.zip\n" +
            "FROM \"Customer\" c\n" +
            "    JOIN \"Address\" a ON a.address_id = c.address_id";

    private static final String SQL_SELECT_LOGIN =
            "SELECT c.customer_id, c.\"name\", c.surname, c.gender, c.phone_number, c.birth_date\n" +
            "FROM \"Customer\" c\n" +
            "WHERE c.\"name\" LIKE :namePar AND c.phone_number LIKE :phonePar";

    public static int Insert(Customer customer, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("namePar", customer.getName());
        query.setParameter("surnamePar", customer.getSurname());
        query.setParameter("genderPar", customer.getGender());
        query.setParameter("phoneNumberPar", customer.getPhoneNumber());
        query.setParameter("birthDatePar", customer.getBirthDate());
        if (customer.getAddress() != null) {
            query.setParameter("idPar", customer.getAddress().getId());
        } else {
            query.setParameter("idPar", 0);
        }

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Customer customer, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("namePar", customer.getName());
        query.setParameter("surnamePar", customer.getSurname());
        query.setParameter("genderPar", customer.getGender());
        query.setParameter("phoneNumberPar", customer.getPhoneNumber());
        query.setParameter("birthDatePar", customer.getBirthDate());
        if (customer.getAddress() != null) {
            query.setParameter("addressIdPar", customer.getAddress().getId());
        } else {
            query.setParameter("addressIdPar", 0);
        }
        query.setParameter("idPar", customer.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idCustomer, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idCustomer);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static Customer Select(int idCustomer, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ID);
        query.setParameter("idPar", idCustomer);

        Object[] object = (Object[]) query.getSingleResult();

        Address address = new Address();
        address.setId(Integer.parseInt(object[6].toString()));
        address.setStreet(object[7].toString());
        address.setStreetNumber(object[8].toString());
        address.setCity(object[9].toString());
        address.setZip(Integer.parseInt(object[10].toString()));

        Customer customer = new Customer();
        customer.setId(Integer.parseInt(object[0].toString()));
        customer.setName(object[1].toString());
        customer.setSurname(object[2].toString());
        customer.setGender(object[3].toString());
        customer.setPhoneNumber(object[4].toString());
        customer.setBirthDate(object[5].toString());
        customer.setAddress(address);

        return customer;
    }

    public static Customer SelectIdLogin(String name, String phone, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_LOGIN);
        query.setParameter("namePar", name);
        query.setParameter("phonePar", phone);

        Object[] object = (Object[]) query.getSingleResult();

        Customer customer = new Customer();
        customer.setId(Integer.parseInt(object[0].toString()));
        customer.setName(object[1].toString());
        customer.setSurname(object[2].toString());
        customer.setGender(object[3].toString());
        customer.setPhoneNumber(object[4].toString());
        customer.setBirthDate(object[5].toString());

        System.out.println(customer);

        return customer;
    }

    public static List<Customer> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Customer> customers = new ArrayList<Customer>();

        List<Object[]> rows = query.list();
        Address address;
        Customer customer;

        for (Object[] row : rows) {
            address = new Address();
            address.setId(Integer.parseInt(row[6].toString()));
            address.setStreet(row[7].toString());
            address.setStreetNumber(row[8].toString());
            address.setCity(row[9].toString());
            address.setZip(Integer.parseInt(row[10].toString()));

            customer = new Customer();
            customer.setId(Integer.parseInt(row[0].toString()));
            customer.setName(row[1].toString());
            customer.setSurname(row[2].toString());
            customer.setGender(row[3].toString());
            customer.setPhoneNumber(row[4].toString());
            customer.setBirthDate(row[5].toString());
            customer.setAddress(address);

            customers.add(customer);
        }

        return customers;
    }
}
