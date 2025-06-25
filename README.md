# 📘 Plexus HR - Employee Management System

## Overview
Plexus HR is a full-stack web application designed to streamline employee management processes for organizations. Built using Java for backend logic, Oracle SQL for data persistence, and HTML/CSS/JavaScript for a responsive and interactive frontend, Plexus HR offers a robust solution for HR departments.

## 🚀 Features

### 🧑‍💼 Employee Registration and Profile Management
### 📋 Department and Role Assignment
### 📊 Attendance and Leave Tracking
### 📈 Performance Evaluation
### 🔐 Secure Login and Role-Based Access
### 🔍 Search and Filter Employees
### 📁 Export Reports (CSV/PDF)

## 🛠️ Tech Stack

### Backend
Java (Servlets/JSP or Spring Framework)
Oracle SQL (Database schema, stored procedures)

### Frontend
HTML5 (Structure)
CSS3 (Styling)
JavaScript (Interactivity and DOM manipulation)

## 📂 Project Structure

### PlexusHR/
├── backend/
│   ├── src/
│   │   ├── controllers/
│   │   ├── models/
│   │   └── services/
│   └── web.xml
├── frontend/
│   ├── index.html
│   ├── css/
│   ├── js/
│   └── assets/
├── database/
│   └── plexus_hr_schema.sql
└── README.md

## ⚙️ Setup Instructions
Prerequisites
Java JDK 11+
Oracle Database
Apache Tomcat or any Java EE server
Web browser
Steps
Clone the repository


Import the SQL schema

Use Oracle SQL Developer to run plexus_hr_schema.sql.
Configure backend

Set database connection parameters in dbconfig.properties.
Deploy to server

Package the backend as a WAR file and deploy to Tomcat.
Access the application

Open http://localhost:8000/plexus-hr in your browser.

## 📸 Screenshots


## 📌 Future Enhancements
Integration with payroll systems
Mobile-responsive design
RESTful API support
Analytics dashboard