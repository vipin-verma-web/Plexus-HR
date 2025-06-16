DROP TABLE Users CASCADE CONSTRAINTS;
DROP TABLE Employees CASCADE CONSTRAINTS;
DROP TABLE Designations CASCADE CONSTRAINTS;
DROP TABLE Departments CASCADE CONSTRAINTS;
 
DROP SEQUENCE departments_seq;
DROP SEQUENCE designations_seq;
DROP SEQUENCE users_seq;





CREATE TABLE Departments (
    department_id NUMBER PRIMARY KEY,
    department_name VARCHAR2(100) UNIQUE NOT NULL
);
 
CREATE SEQUENCE departments_seq
START WITH 1
INCREMENT BY 1
NOCACHE;
 
CREATE OR REPLACE TRIGGER trg_departments_id
BEFORE INSERT ON Departments
FOR EACH ROW
BEGIN
  :NEW.department_id := departments_seq.NEXTVAL;
END;
/
 
CREATE TABLE Designations (
    designation_id NUMBER PRIMARY KEY,
    designation_name VARCHAR2(100) UNIQUE NOT NULL
);
 
CREATE SEQUENCE designations_seq
START WITH 1
INCREMENT BY 1
NOCACHE;
 
CREATE OR REPLACE TRIGGER trg_designations_id
BEFORE INSERT ON Designations
FOR EACH ROW
BEGIN
  :NEW.designation_id := designations_seq.NEXTVAL;
END;
/
 
CREATE TABLE Employees (
    employee_id VARCHAR2(10) PRIMARY KEY,
    first_name VARCHAR2(50) NOT NULL,
    last_name VARCHAR2(50) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR2(10),
    email VARCHAR2(100) UNIQUE NOT NULL,
    phone_number VARCHAR2(20),
    address VARCHAR2(255),
    date_of_joining DATE NOT NULL,
    department_id NUMBER REFERENCES Departments(department_id),
    designation_id NUMBER REFERENCES Designations(designation_id),
    basic_salary NUMBER(10, 2),
    bank_account_number VARCHAR2(50),
    emergency_contact_name VARCHAR2(100),
    emergency_contact_phone VARCHAR2(20),
    status VARCHAR2(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Inactive', 'Terminated')),
    termination_date DATE,
    termination_reason VARCHAR2(255)
);
 
CREATE TABLE Users (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) NOT NULL CHECK (role IN ('Admin', 'Employee'))
);
 
CREATE SEQUENCE users_seq
START WITH 1
INCREMENT BY 1
NOCACHE;
 
CREATE OR REPLACE TRIGGER trg_users_id
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
  :NEW.user_id := users_seq.NEXTVAL;
END;
/
 
INSERT INTO Departments (department_name) VALUES ('Human Resources');
INSERT INTO Departments (department_name) VALUES ('Engineering');
INSERT INTO Departments (department_name) VALUES ('Sales');
 
INSERT INTO Designations (designation_name) VALUES ('HR Manager');
INSERT INTO Designations (designation_name) VALUES ('Software Engineer');
INSERT INTO Designations (designation_name) VALUES ('Sales Executive');
 
INSERT INTO Users (username, password, role) VALUES ('admin', 'admin123', 'Admin');
INSERT INTO Users (username, password, role) VALUES ('user1', 'user123', 'Employee');
 
COMMIT;