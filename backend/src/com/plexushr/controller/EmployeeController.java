package com.plexushr.controller;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
 
import org.json.JSONArray;
import org.json.JSONObject;
 
import com.plexushr.dao.DepartmentDAO;
import com.plexushr.dao.DesignationDAO;
import com.plexushr.dao.EmployeeDAO;
import com.plexushr.model.Department;
import com.plexushr.model.Designation;
import com.plexushr.model.Employee;
import com.sun.net.httpserver.HttpExchange;
 
public class EmployeeController {
 
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final DepartmentDAO departmentDAO = new DepartmentDAO();
    private static final DesignationDAO designationDAO = new DesignationDAO();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
    public static void handleRequest(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String method = exchange.getRequestMethod();
        System.out.println("Received " + method + " request for: " + path);
 
        // CORS Headers - Crucial for frontend to communicate
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
 
        // Handle preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }
 
        // --- Employee Endpoints ---
        if (path.startsWith("/api/employees")) {
            handleEmployeeRequests(exchange, method, path);
        }
        // --- Department Endpoints ---
        else if (path.startsWith("/api/departments")) {
            handleDepartmentRequests(exchange, method, path);
        }
        // --- Designation Endpoints ---
        else if (path.startsWith("/api/designations")) {
            handleDesignationRequests(exchange, method, path);
        }
        // --- Not Found ---
        else {
            sendResponse(exchange, 404, "{\"message\": \"Not Found\"}");
        }
    }
 
    private static void handleEmployeeRequests(HttpExchange exchange, String method, String path) throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            if (path.equals("/api/employees")) {
                getAllEmployees(exchange);
            } else if (path.matches("/api/employees/[a-zA-Z0-9]+")) {
                String employeeId = path.substring(path.lastIndexOf('/') + 1);
                getEmployeeById(exchange, employeeId);
            } else {
                sendResponse(exchange, 404, "{\"message\": \"Employee endpoint not found\"}");
            }
        } else if ("POST".equalsIgnoreCase(method) && path.equals("/api/employees")) {
            addEmployee(exchange);
        } else if ("PUT".equalsIgnoreCase(method) && path.matches("/api/employees/[a-zA-Z0-9]+")) {
            String employeeId = path.substring(path.lastIndexOf('/') + 1);
            updateEmployee(exchange, employeeId);
        } else if ("DELETE".equalsIgnoreCase(method) && path.matches("/api/employees/[a-zA-Z0-9]+")) {
            String employeeId = path.substring(path.lastIndexOf('/') + 1);
            deleteEmployee(exchange, employeeId);
        } else {
            sendResponse(exchange, 405, "{\"message\": \"Method Not Allowed for employees\"}");
        }
    }
 
    private static void handleDepartmentRequests(HttpExchange exchange, String method, String path) throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            if (path.equals("/api/departments")) {
                getAllDepartments(exchange);
            } else if (path.matches("/api/departments/[0-9]+")) {
                int deptId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                getDepartmentById(exchange, deptId);
            } else {
                sendResponse(exchange, 404, "{\"message\": \"Department endpoint not found\"}");
            }
        } else if ("POST".equalsIgnoreCase(method) && path.equals("/api/departments")) {
            addDepartment(exchange);
        } else if ("PUT".equalsIgnoreCase(method) && path.matches("/api/departments/[0-9]+")) {
            int deptId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            updateDepartment(exchange, deptId);
        } else if ("DELETE".equalsIgnoreCase(method) && path.matches("/api/departments/[0-9]+")) {
            int deptId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            deleteDepartment(exchange, deptId);
        } else {
            sendResponse(exchange, 405, "{\"message\": \"Method Not Allowed for departments\"}");
        }
    }
 
    private static void handleDesignationRequests(HttpExchange exchange, String method, String path) throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            if (path.equals("/api/designations")) {
                getAllDesignations(exchange);
            } else if (path.matches("/api/designations/[0-9]+")) {
                int desgId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                getDesignationById(exchange, desgId);
            } else {
                sendResponse(exchange, 404, "{\"message\": \"Designation endpoint not found\"}");
            }
        } else if ("POST".equalsIgnoreCase(method) && path.equals("/api/designations")) {
            addDesignation(exchange);
        } else if ("PUT".equalsIgnoreCase(method) && path.matches("/api/designations/[0-9]+")) {
            int desgId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            updateDesignation(exchange, desgId);
        } else if ("DELETE".equalsIgnoreCase(method) && path.matches("/api/designations/[0-9]+")) {
            int desgId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            deleteDesignation(exchange, desgId);
        } else {
            sendResponse(exchange, 405, "{\"message\": \"Method Not Allowed for designations\"}");
        }
    }
 
    // --- Employee Handlers ---
    private static void getAllEmployees(HttpExchange exchange) throws IOException {
        List<Employee> employees = employeeDAO.getAllEmployees();
        JSONArray jsonArray = new JSONArray();
        for (Employee emp : employees) {
            jsonArray.put(employeeToJson(emp));
        }
        sendResponse(exchange, 200, jsonArray.toString());
    }
 
    private static void getEmployeeById(HttpExchange exchange, String employeeId) throws IOException {
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee != null) {
            sendResponse(exchange, 200, employeeToJson(employee).toString());
        } else {
            sendResponse(exchange, 404, "{\"message\": \"Employee not found\"}");
        }
    }
 
    private static void addEmployee(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Employee employee = jsonToEmployee(json);
            if (employeeDAO.addEmployee(employee)) {
                sendResponse(exchange, 201, "{\"message\": \"Employee added successfully\", \"employeeId\": \"" + employee.getEmployeeId() + "\"}");
            } else {
                sendResponse(exchange, 500, "{\"message\": \"Failed to add employee\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid employee data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void updateEmployee(HttpExchange exchange, String employeeId) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Employee employee = jsonToEmployee(json);
            employee.setEmployeeId(employeeId); // Ensure the ID from URL is used
            // Inside updateEmployee method in EmployeeController.java
            if (employeeDAO.updateEmployee(employee)) {
                sendResponse(exchange, 200, "{\"message\": \"Employee updated successfully\"}");
            } else {
                // THIS IS THE LINE THAT IS SENDING YOUR ERROR MESSAGE
                sendResponse(exchange, 500, "{\"message\": \"Failed to update employee or employee not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid employee data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void deleteEmployee(HttpExchange exchange, String employeeId) throws IOException {
        if (employeeDAO.deleteEmployee(employeeId)) {
            sendResponse(exchange, 200, "{\"message\": \"Employee deleted successfully\"}");
        } else {
            sendResponse(exchange, 500, "{\"message\": \"Failed to delete employee or employee not found\"}");
        }
    }
 
    // --- Department Handlers ---
    private static void getAllDepartments(HttpExchange exchange) throws IOException {
        List<Department> departments = departmentDAO.getAllDepartments();
        JSONArray jsonArray = new JSONArray();
        for (Department dept : departments) {
            jsonArray.put(departmentToJson(dept));
        }
        sendResponse(exchange, 200, jsonArray.toString());
    }
 
    private static void getDepartmentById(HttpExchange exchange, int departmentId) throws IOException {
        Department department = departmentDAO.getDepartmentById(departmentId);
        if (department != null) {
            sendResponse(exchange, 200, departmentToJson(department).toString());
        } else {
            sendResponse(exchange, 404, "{\"message\": \"Department not found\"}");
        }
    }
 
    private static void addDepartment(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Department department = new Department();
            department.setDepartmentName(json.getString("departmentName"));
            if (departmentDAO.addDepartment(department)) {
                sendResponse(exchange, 201, "{\"message\": \"Department added successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"message\": \"Failed to add department\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid department data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void updateDepartment(HttpExchange exchange, int departmentId) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Department department = new Department();
            department.setDepartmentId(departmentId);
            department.setDepartmentName(json.getString("departmentName"));
            if (departmentDAO.updateDepartment(department)) {
                sendResponse(exchange, 200, "{\"message\": \"Department updated successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"message\": \"Failed to update department or department not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid department data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void deleteDepartment(HttpExchange exchange, int departmentId) throws IOException {
        if (departmentDAO.deleteDepartment(departmentId)) {
            sendResponse(exchange, 200, "{\"message\": \"Department deleted successfully\"}");
        } else {
            sendResponse(exchange, 500, "{\"message\": \"Failed to delete department or department not found\"}");
        }
    }
 
    // --- Designation Handlers ---
    private static void getAllDesignations(HttpExchange exchange) throws IOException {
        List<Designation> designations = designationDAO.getAllDesignations();
        JSONArray jsonArray = new JSONArray();
        for (Designation desg : designations) {
            jsonArray.put(designationToJson(desg));
        }
        sendResponse(exchange, 200, jsonArray.toString());
    }
 
    private static void getDesignationById(HttpExchange exchange, int designationId) throws IOException {
        Designation designation = designationDAO.getDesignationById(designationId);
        if (designation != null) {
            sendResponse(exchange, 200, designationToJson(designation).toString());
        } else {
            sendResponse(exchange, 404, "{\"message\": \"Designation not found\"}");
        }
    }
 
    private static void addDesignation(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Designation designation = new Designation();
            designation.setDesignationName(json.getString("designationName"));
            if (designationDAO.addDesignation(designation)) {
                sendResponse(exchange, 201, "{\"message\": \"Designation added successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"message\": \"Failed to add designation\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid designation data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void updateDesignation(HttpExchange exchange, int designationId) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            JSONObject json = new JSONObject(requestBody);
            Designation designation = new Designation();
            designation.setDesignationId(designationId);
            designation.setDesignationName(json.getString("designationName"));
            if (designationDAO.updateDesignation(designation)) {
                sendResponse(exchange, 200, "{\"message\": \"Designation updated successfully\"}");
            } else {
                sendResponse(exchange, 500, "{\"message\": \"Failed to update designation or designation not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "{\"message\": \"Invalid designation data: " + e.getMessage() + "\"}");
        }
    }
 
    private static void deleteDesignation(HttpExchange exchange, int designationId) throws IOException {
        if (designationDAO.deleteDesignation(designationId)) {
            sendResponse(exchange, 200, "{\"message\": \"Designation deleted successfully\"}");
        } else {
            sendResponse(exchange, 500, "{\"message\": \"Failed to delete designation or designation not found\"}");
        }
    }
 
    // --- Utility Methods ---
    private static JSONObject employeeToJson(Employee emp) {
        JSONObject json = new JSONObject();
        json.put("employeeId", emp.getEmployeeId());
        json.put("firstName", emp.getFirstName());
        json.put("lastName", emp.getLastName());
        json.put("dateOfBirth", emp.getDateOfBirth() != null ? dateFormat.format(emp.getDateOfBirth()) : null);
        json.put("gender", emp.getGender());
        json.put("email", emp.getEmail());
        json.put("phoneNumber", emp.getPhoneNumber());
        json.put("address", emp.getAddress());
        json.put("dateOfJoining", emp.getDateOfJoining() != null ? dateFormat.format(emp.getDateOfJoining()) : null);
        json.put("departmentId", emp.getDepartmentId());
        json.put("designationId", emp.getDesignationId());
        json.put("basicSalary", emp.getBasicSalary());
        json.put("bankAccountNumber", emp.getBankAccountNumber());
        json.put("emergencyContactName", emp.getEmergencyContactName());
        json.put("emergencyContactPhone", emp.getEmergencyContactPhone());
        json.put("status", emp.getStatus());
        json.put("terminationDate", emp.getTerminationDate() != null ? dateFormat.format(emp.getTerminationDate()) : null);
        json.put("terminationReason", emp.getTerminationReason());
        return json;
    }
 
    private static Employee jsonToEmployee(JSONObject json) throws ParseException {
        Employee employee = new Employee();
        if (json.has("employeeId")) employee.setEmployeeId(json.getString("employeeId"));
        if (json.has("firstName")) employee.setFirstName(json.getString("firstName"));
        if (json.has("lastName")) employee.setLastName(json.getString("lastName"));
        if (json.has("dateOfBirth") && !json.isNull("dateOfBirth")) employee.setDateOfBirth(dateFormat.parse(json.getString("dateOfBirth")));
        if (json.has("gender")) employee.setGender(json.getString("gender"));
        if (json.has("email")) employee.setEmail(json.getString("email"));
        if (json.has("phoneNumber")) employee.setPhoneNumber(json.getString("phoneNumber"));
        if (json.has("address")) employee.setAddress(json.getString("address"));
        if (json.has("dateOfJoining") && !json.isNull("dateOfJoining")) employee.setDateOfJoining(dateFormat.parse(json.getString("dateOfJoining")));
        if (json.has("departmentId")) employee.setDepartmentId(json.getInt("departmentId"));
        if (json.has("designationId")) employee.setDesignationId(json.getInt("designationId"));
        if (json.has("basicSalary")) employee.setBasicSalary(json.getDouble("basicSalary"));
        if (json.has("bankAccountNumber")) employee.setBankAccountNumber(json.getString("bankAccountNumber"));
        if (json.has("emergencyContactName")) employee.setEmergencyContactName(json.getString("emergencyContactName"));
        if (json.has("emergencyContactPhone")) employee.setEmergencyContactPhone(json.getString("emergencyContactPhone"));
        if (json.has("status")) employee.setStatus(json.getString("status"));
        if (json.has("terminationDate") && !json.isNull("terminationDate")) employee.setTerminationDate(dateFormat.parse(json.getString("terminationDate")));
        if (json.has("terminationReason")) employee.setTerminationReason(json.getString("terminationReason"));
        return employee;
    }
 
    private static JSONObject departmentToJson(Department dept) {
        JSONObject json = new JSONObject();
        json.put("departmentId", dept.getDepartmentId());
        json.put("departmentName", dept.getDepartmentName());
        return json;
    }
 
    private static JSONObject designationToJson(Designation desg) {
        JSONObject json = new JSONObject();
        json.put("designationId", desg.getDesignationId());
        json.put("designationName", desg.getDesignationName());
        return json;
    }
 
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
 
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
 