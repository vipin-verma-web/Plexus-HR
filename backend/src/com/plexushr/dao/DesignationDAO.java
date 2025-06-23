package com.plexushr.dao;

import com.plexushr.model.Designation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DesignationDAO {

    public List<Designation> getAllDesignations() {
        List<Designation> designations = new ArrayList<>();
        String sql = "SELECT * FROM Designations ORDER BY designation_name";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                designations.add(mapResultSetToDesignation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return designations;
    }

    public Designation getDesignationById(int designationId) {
        Designation designation = null;
        String sql = "SELECT * FROM Designations WHERE designation_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, designationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    designation = mapResultSetToDesignation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return designation;
    }

    public boolean addDesignation(Designation designation) {
        String sql = "INSERT INTO Designations (designation_name) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, designation.getDesignationName());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDesignation(Designation designation) {
        String sql = "UPDATE Designations SET designation_name=? WHERE designation_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, designation.getDesignationName());
            pstmt.setInt(2, designation.getDesignationId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDesignation(int designationId) {
        String sql = "DELETE FROM Designations WHERE designation_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, designationId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Designation mapResultSetToDesignation(ResultSet rs) throws SQLException {
        Designation designation = new Designation();
        designation.setDesignationId(rs.getInt("designation_id"));
        designation.setDesignationName(rs.getString("designation_name"));
        return designation;
    }
}