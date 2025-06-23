package com.plexushr.dao;

import com.plexushr.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeById(String employeeId) {
        Employee employee = null;
        String sql = "SELECT * FROM Employees WHERE employee_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee = mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (employee_id, first_name, last_name, date_of_birth, gender, email, phone_number, address, date_of_joining, department_id, designation_id, basic_salary, bank_account_number, emergency_contact_name, emergency_contact_phone, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getEmployeeId());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setDate(4, employee.getDateOfBirth() != null ? new java.sql.Date(employee.getDateOfBirth().getTime()) : null);
            pstmt.setString(5, employee.getGender());
            pstmt.setString(6, employee.getEmail());
            pstmt.setString(7, employee.getPhoneNumber());
            pstmt.setString(8, employee.getAddress());
            pstmt.setDate(9, employee.getDateOfJoining() != null ? new java.sql.Date(employee.getDateOfJoining().getTime()) : null);
            pstmt.setInt(10, employee.getDepartmentId());
            pstmt.setInt(11, employee.getDesignationId());
            pstmt.setDouble(12, employee.getBasicSalary());
            pstmt.setString(13, employee.getBankAccountNumber());
            pstmt.setString(14, employee.getEmergencyContactName());
            pstmt.setString(15, employee.getEmergencyContactPhone());
            pstmt.setString(16, employee.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET first_name=?, last_name=?, date_of_birth=?, gender=?, email=?, phone_number=?, address=?, date_of_joining=?, department_id=?, designation_id=?, basic_salary=?, bank_account_number=?, emergency_contact_name=?, emergency_contact_phone=?, status=?, termination_date=?, termination_reason=? WHERE employee_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setDate(3, employee.getDateOfBirth() != null ? new java.sql.Date(employee.getDateOfBirth().getTime()) : null);
            pstmt.setString(4, employee.getGender());
            pstmt.setString(5, employee.getEmail());
            pstmt.setString(6, employee.getPhoneNumber());
            pstmt.setString(7, employee.getAddress());
            pstmt.setDate(8, employee.getDateOfJoining() != null ? new java.sql.Date(employee.getDateOfJoining().getTime()) : null);
            pstmt.setInt(9, employee.getDepartmentId());
            pstmt.setInt(10, employee.getDesignationId());
            pstmt.setDouble(11, employee.getBasicSalary());
            pstmt.setString(12, employee.getBankAccountNumber());
            pstmt.setString(13, employee.getEmergencyContactName());
            pstmt.setString(14, employee.getEmergencyContactPhone());
            pstmt.setString(15, employee.getStatus());
            pstmt.setDate(16, employee.getTerminationDate() != null ? new java.sql.Date(employee.getTerminationDate().getTime()) : null);
            pstmt.setString(17, employee.getTerminationReason());
            pstmt.setString(18, employee.getEmployeeId()); // WHERE clause

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmployee(String employeeId) {
        String sql = "DELETE FROM Employees WHERE employee_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getString("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setDateOfBirth(rs.getDate("date_of_birth"));
        employee.setGender(rs.getString("gender"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setAddress(rs.getString("address"));
        employee.setDateOfJoining(rs.getDate("date_of_joining"));
        employee.setDepartmentId(rs.getInt("department_id"));
        employee.setDesignationId(rs.getInt("designation_id"));
        employee.setBasicSalary(rs.getDouble("basic_salary"));
        employee.setBankAccountNumber(rs.getString("bank_account_number"));
        employee.setEmergencyContactName(rs.getString("emergency_contact_name"));
        employee.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        employee.setStatus(rs.getString("status"));
        employee.setTerminationDate(rs.getDate("termination_date"));
        employee.setTerminationReason(rs.getString("termination_reason"));
        return employee;
    }
}