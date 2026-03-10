package com.orm.dao;

import com.orm.Employee;
import com.orm.Shift;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ShiftRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Shift\" (arrival_time, departure_time, employee_id)\n" +
            "VALUES (TO_DATE(:arrivalTimePar, 'dd-mm-yyyy HH24:MI:SS'), TO_DATE(:departureTimePar, 'dd-mm-yyyy HH24:MI:SS'), :employeeIdPar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Shift\"\n" +
            "SET \"Shift\".departure_time = CASE\n" +
            "                            WHEN TO_DATE(:departureTimePar, 'dd-mm-yyyy HH24:MI:SS') <= \"Shift\".arrival_time \n" +
            "                            THEN \"Shift\".departure_time \n" +
            "                            ELSE TO_DATE(:departureTimePar, 'dd-mm-yyyy HH24:MI:SS')\n" +
            "                            END\n" +
            "WHERE \"Shift\".shift_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Shift\"\n" +
            "WHERE \"Shift\".shift_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT s.shift_id, s.arrival_time, s.departure_time, e.\"name\"\n" +
            "FROM \"Shift\" s\n" +
            "    JOIN \"Employee\" e ON e.employee_id = s.employee_id";

    public static int Insert(Shift shift, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("arrivalTimePar", shift.getArrivalTime());
        query.setParameter("departureTimePar", shift.getDepartureTime());
        query.setParameter("employeeIdPar", shift.getEmployee().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Shift shift, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("departureTimePar", shift.getDepartureTime());
        query.setParameter("idPar", shift.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idShift, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idShift);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static List<Shift> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Shift> shiftsAll = new ArrayList<Shift>();

        List<Object[]> rows = query.list();
        Shift shift;
        Employee employee;

        for (Object[] row : rows) {
            employee = new Employee();
            employee.setName(row[3].toString());
            shift = new Shift();
            shift.setId(Integer.parseInt(row[0].toString()));
            shift.setArrivalTime(row[1].toString());
            if (row[2] != null) {
                shift.setDepartureTime(row[2].toString());
            }
            shift.setEmployee(employee);

            shiftsAll.add(shift);
        }

        return shiftsAll;
    }
}
