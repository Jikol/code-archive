package com.orm.dao;

import com.orm.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    private static Query query;

    private static final String SQL_INSERT_PROCEDURE =
            "CALL InsertEmployee(:namePar, :surnamePar, :genderPar, :birthDatePar, :hourlyWagePar, :restaurantIdPar)";

    private static final String SQL_UPDATE =
            "UPDATE \"Employee\"\n" +
            "SET \"Employee\".\"name\" = :namePar,\n" +
            "\"Employee\".surname = :surnamePar,\n" +
            "\"Employee\".gender = :genderPar,\n" +
            "\"Employee\".birth_date = TO_DATE(:birthDatePar, 'dd-mm-yyyy'),\n" +
            "\"Employee\".withdrawal_date = CASE\n" +
            "WHEN TO_DATE(:withdrawalDatePar, 'dd-mm-yyyy') >\n" +
            "\"Employee\".accession_date\n" +
            "THEN TO_DATE(:withdrawalDatePar, 'dd-mm-yyyy')\n" +
            "ELSE\n" +
            "\"Employee\".withdrawal_date\n" +
            "END,\n" +
            "\"Employee\".hourly_wage = :hourlyWagePar,\n" +
            "\"Employee\".foreman_id = CASE\n" +
            "WHEN :foremanIdPar IN (\n" +
            "SELECT e.employee_id\n" +
            "FROM \"Employee\" e\n" +
            "WHERE e.foreman_id IS NULL\n" +
            "AND e.restaurant_id = (\n" +
            "SELECT e2.restaurant_id\n" +
            "FROM \"Employee\" e2\n" +
            "WHERE e2.employee_id =\n" +
            ":idPar\n" +
            "AND e2.foreman_id IS NOT\n" +
            "NULL\n" +
            "))\n" +
            "THEN :foremanIdPar\n" +
            "ELSE \"Employee\".foreman_id\n" +
            "END\n" +
            "WHERE \"Employee\".employee_id = :idPar";

    private static final String SQL_DELETE =
            "DELETE FROM \"Employee\"\n" +
            "WHERE \"Employee\".employee_id = :idPar";

    private static final String SQL_SELECT =
            "SELECT e.employee_id, e.\"name\", e.surname, e.gender, e.birth_date, e.accession_date, e.withdrawal_date, e.hourly_wage, e.restaurant_id, e.foreman_id, r.\"name\" AS restaurantName\n" +
            "FROM \"Employee\" e\n" +
            "JOIN \"Restaurant\" r ON e.restaurant_id = r.restaurant_id\n" +
            "WHERE e.employee_id = :idPar";

    private static final String SQL_SELECT_HOURS_WORKED =
            "SELECT SUM((s.departure_time - s.arrival_time) * 24)\n" +
            "FROM \"Shift\" s\n" +
            "WHERE s.employee_id = :idPar AND s.departure_time IS NOT NULL";

    private static final String SQL_SELECT_ALL =
            "SELECT e.employee_id, e.\"name\", e.surname, e.gender, e.birth_date, e.accession_date, e.withdrawal_date, e.hourly_wage, r.\"name\" AS restaurantName, t.shifts\n, r.restaurant_id\n" +
            "FROM \"Employee\" e\n" +
            "JOIN \"Restaurant\" r ON e.restaurant_id =\n" +
            "r.restaurant_id\n" +
            "JOIN (\n" +
            "SELECT s.employee_id, SUM((s.departure_time - s.arrival_time) * 24) AS shifts\n" +
            "FROM \"Shift\" s\n" +
            "WHERE s.departure_time IS NOT NULL\n" +
            "GROUP BY s.employee_id\n" +
            ") t ON t.employee_id = e.employee_id";

    private static final String SQL_SELECT_ALL_WITHOUT_DRIVE =
            "SELECT e.employee_id, e.\"name\", e.surname, e.gender, e.birth_date, e.accession_date, e.withdrawal_date, e.hourly_wage, e.restaurant_id\n" +
            "FROM \"Employee\" e\n" +
            "WHERE e.employee_id NOT IN (\n" +
            "    SELECT d.employee_id\n" +
            "    FROM \"Drive\" d)";

    private static final String SQL_SELECT_MAX_EMPLOYEE =
            "SELECT MAX(e.employee_id)\n" +
            "FROM \"Employee\" e\n" +
            "WHERE e.restaurant_id = :idPar AND e.employee_id NOT IN (\n" +
            "    SELECT d.employee_id\n" +
            "    FROM \"Drive\" d)";

    public static int Insert(Employee employee, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_INSERT_PROCEDURE);
        query.setParameter("namePar", employee.getName());
        query.setParameter("surnamePar", employee.getSurname());
        query.setParameter("genderPar", employee.getGender());
        query.setParameter("birthDatePar", employee.getBirthDate());
        query.setParameter("hourlyWagePar", employee.getHourlyWage());
        query.setParameter("restaurantIdPar", employee.getRestaurant().getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Update(Employee employee, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_UPDATE);
        query.setParameter("namePar", employee.getName());
        query.setParameter("surnamePar", employee.getSurname());
        query.setParameter("genderPar", employee.getGender());
        query.setParameter("birthDatePar", employee.getBirthDate());
        query.setParameter("withdrawalDatePar", employee.getWithdrawalDate());
        query.setParameter("hourlyWagePar", employee.getHourlyWage());
        query.setParameter("foremanIdPar", employee.getForeman().getId());
        query.setParameter("idPar", employee.getId());

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static int Delete(int idEmployee, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        db.BeginTransaction();
        query = db.getSession().createSQLQuery(SQL_DELETE);
        query.setParameter("idPar", idEmployee);

        int rows = db.ExecuteQuery(query);
        db.EndTransaction();

        return rows;
    }

    public static EmployeeAll Select(int idEmployee, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT);
        query.setParameter("idPar", idEmployee);

        Object[] object = (Object[]) query.getSingleResult();

        EmployeeAll employeeAll = new EmployeeAll();
        employeeAll.setId(Integer.parseInt(object[0].toString()));
        employeeAll.setName(object[1].toString());
        employeeAll.setSurname(object[2].toString());
        employeeAll.setGender(object[3].toString());
        employeeAll.setBirthDate((object[4].toString()));
        employeeAll.setAccessionDate((object[5].toString()));
        if (object[6] != null) {
            employeeAll.setWithdrawalDate(object[6].toString());
        }
        employeeAll.setHourlyWage(Integer.parseInt(object[7].toString()));
        Restaurant restaurant = new Restaurant();
        restaurant.setId(Integer.parseInt(object[8].toString()));
        Employee employee = new Employee();
        if (object[9] != null) {
            employee.setId(Integer.parseInt(object[9].toString()));
        }
        restaurant.setName(object[10].toString());

        query = db.getSession().createSQLQuery(SQL_SELECT_HOURS_WORKED);
        query.setParameter("idPar", idEmployee);

        Object objectHoursWorked = query.getSingleResult();

        employeeAll.setHoursWorked(Double.parseDouble(objectHoursWorked.toString()));
        employeeAll.setForeman(employee);
        employeeAll.setRestaurant(restaurant);

        return employeeAll;
    }

    public static Employee SelectMaxEmployee(int idPar, Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_MAX_EMPLOYEE);
        query.setParameter("idPar", idPar);

        Object object = query.getSingleResult();

        Employee employee = new EmployeeAll();
        employee.setId(Integer.parseInt(object.toString()));

        return employee;
    }

    public static List<EmployeeAll> SelectAll(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL);
        List<EmployeeAll> employeeAlls = new ArrayList<EmployeeAll>();

        List<Object[]> rows = query.list();
        EmployeeAll employeeAll;
        Restaurant restaurant;

        for (Object[] row : rows) {
            restaurant = new Restaurant();
            restaurant.setName(row[8].toString());
            restaurant.setId(Integer.parseInt(row[10].toString()));
            employeeAll = new EmployeeAll();
            employeeAll.setId(Integer.parseInt(row[0].toString()));
            employeeAll.setName(row[1].toString());
            employeeAll.setSurname(row[2].toString());
            employeeAll.setGender(row[3].toString());
            employeeAll.setBirthDate(row[4].toString());
            employeeAll.setAccessionDate(row[5].toString());
            if (row[6] != null) {
                employeeAll.setWithdrawalDate(row[6].toString());
            }
            employeeAll.setHourlyWage(Integer.parseInt(row[7].toString()));
            employeeAll.setHoursWorked(Double.parseDouble(row[9].toString()));
            employeeAll.setRestaurant(restaurant);
            employeeAlls.add(employeeAll);
        }

        return employeeAlls;
    }

    public static List<Employee> SelectAllWithoutDrive(Database pDb) {
        Database db;
        if (pDb == null) {
            db = Database.create();
            db.Connect();
        } else {
            db = pDb;
        }

        query = db.getSession().createSQLQuery(SQL_SELECT_ALL_WITHOUT_DRIVE);
        List<Employee> employees = new ArrayList<>();

        List<Object[]> rows = query.list();
        Employee employee;
        Restaurant restaurant;

        for (Object[] row : rows) {
            restaurant = new Restaurant();
            restaurant.setId(Integer.parseInt(row[8].toString()));
            employee = new EmployeeAll();
            employee.setId(Integer.parseInt(row[0].toString()));
            employee.setName(row[1].toString());
            employee.setSurname(row[2].toString());
            employee.setGender(row[3].toString());
            employee.setBirthDate(row[4].toString());
            employee.setAccessionDate(row[5].toString());
            if (row[6] != null) {
                employee.setWithdrawalDate(row[6].toString());
            }
            employee.setHourlyWage(Integer.parseInt(row[7].toString()));
            employee.setRestaurant(restaurant);
            employees.add(employee);
        }

        return employees;
    }
}
