package com.plexushr.dao;

import com.plexushr.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM Departments ORDER BY department_name";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public Department getDepartmentById(int departmentId) {
        Department department = null;
        String sql = "SELECT * FROM Departments WHERE department_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, departmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    department = mapResultSetToDepartment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }

    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO Departments (department_name) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getDepartmentName());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDepartment(Department department) {
        String sql = "UPDATE Departments SET department_name=? WHERE department_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setInt(2, department.getDepartmentId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDepartment(int departmentId) {
        String sql = "DELETE FROM Departments WHERE department_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, departmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDepartmentId(rs.getInt("department_id"));
        department.setDepartmentName(rs.getString("department_name"));
        return department;
    }
}