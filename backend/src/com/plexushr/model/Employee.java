package com.plexushr.model;

import java.util.Date;

public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dateOfJoining;
    private int departmentId;
    private int designationId;
    private double basicSalary;
    private String bankAccountNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String status; // Active, Inactive, Terminated
    private Date terminationDate;
    private String terminationReason;

    // Constructors
    public Employee() {}

    public Employee(String employeeId, String firstName, String lastName, Date dateOfBirth, String gender, String email, String phoneNumber, String address, Date dateOfJoining, int departmentId, int designationId, double basicSalary, String bankAccountNumber, String emergencyContactName, String emergencyContactPhone, String status, Date terminationDate, String terminationReason) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfJoining = dateOfJoining;
        this.departmentId = departmentId;
        this.designationId = designationId;
        this.basicSalary = basicSalary;
        this.bankAccountNumber = bankAccountNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.status = status;
        this.terminationDate = terminationDate;
        this.terminationReason = terminationReason;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(Date dateOfJoining) { this.dateOfJoining = dateOfJoining; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public int getDesignationId() { return designationId; }
    public void setDesignationId(int designationId) { this.designationId = designationId; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }
    public String getTerminationReason() { return terminationReason; }
    public void setTerminationReason(String terminationReason) { this.terminationReason = terminationReason; }

    @Override
    public String toString() {
        return "Employee{" +
               "employeeId='" + employeeId + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", departmentId=" + departmentId +
               ", designationId=" + designationId +
               '}';
    }
}