package com.orm.dao;

import com.orm.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DriveRepository {

    private static Query query;

    private static final String SQL_INSERT =
            "INSERT INTO \"Drive\" (rental_time, return_time, vehicle_id, employee_id)\n" +
            "VALUES (SYSDATE, NULL, :vehicleIdPar, :employeeIdPar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Drive\"\n" +
            "SET \"Drive\".return_time = CASE\n" +
            "WHEN TO_DATE(:returnTimePar, 'dd-mm-yyyy HH24:MI:SS') >\n" +
            "\"Drive\".rental_time\n" +
            "THEN TO_DATE(:returnTimePar, 'dd-mm-yyyy HH24:MI:SS')\n" +
            "ELSE \"Drive\".return_time\n" +
            "END\n" +
            "WHERE \"Drive\".employee_id = :idPar AND \"Drive\".return_time IS NULL";

    private static final String SQL_DELETE =
            "DELETE FROM \"Drive\"\n" +
            "WHERE \"Drive\".return_time IS NOT NULL AND \"Drive\".drive_id = :idPar";

    private static final String SQL_SELECT_ALL =
            "SELECT d.drive_id, TO_CHAR(d.rental_time, 'HH24:MI:SS'), TO_CHAR(d.return_time, 'HH24:MI:SS'), v.brand, v.\"type\", v.license_plate, e.\"name\", e.gender\n" +
            "FROM \"Drive\" d\n" +
            "    JOIN \"Employee\" e ON e.employee_id = d.employee_id\n" +
            "    JOIN \"Vehicle\" v ON v.vehicle_id = d.vehicle_id";

    public static int Insert(Drive drive, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT);
        query.setParameter("vehicleIdPar", drive.getVehicle().getId());
        query.setParameter("employeeIdPar", drive.getEmployee().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Drive drive, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("returnTimePar", drive.getReturnTime());
        query.setParameter("idPar", drive.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idDrive, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idDrive);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static List<Drive> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<Drive> drives = new ArrayList<Drive>();

        List<Object[]> rows = query.list();
        Drive drive;
        Vehicle vehicle;
        Employee employee;

        for (Object[] row : rows) {
            drive = new Drive();
            drive.setId(Integer.parseInt(row[0].toString()));
            drive.setRentalTime(row[1].toString());
            if (row[2] != null) {
                drive.setReturnTime(row[2].toString());
            }
            vehicle = new Vehicle();
            vehicle.setBrand(row[3].toString());
            if (row[4].toString().equals("car")) {
                vehicle.setType("Auto");
            } else {
                vehicle.setType("Motorka");
            }
            vehicle.setLicensePlate(row[5].toString());
            employee = new Employee();
            employee.setName(row[6].toString());
            employee.setGender(row[7].toString());
            drive.setEmployee(employee);
            drive.setVehicle(vehicle);

            drives.add(drive);
        }

        return drives;
    }
}
