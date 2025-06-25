// plexus-hr/frontend/js/script.js
 
// Backend API Base URL
const API_BASE_URL = 'http://localhost:8000/api';
 
// --- Utility Functions ---
 
function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
 
    const container = document.querySelector('.container') || document.body;
    // Prepend to body or container to ensure it's visible at the top
    container.prepend(messageDiv);
 
    // Automatically remove the message after 5 seconds
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}
 
function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    // Ensure date is valid before formatting
    if (isNaN(date.getTime())) {
        console.warn('Invalid date string provided to formatDateForInput:', dateString);
        return '';
    }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
 
/**
 * Shows a custom confirmation modal.
 * @param {string} message - The message to display in the modal.
 * @returns {Promise<boolean>} - Resolves to true if confirmed, false if cancelled.
 */
function showConfirmModal(message) {
    return new Promise((resolve) => {
        const modalOverlay = document.getElementById('confirmationModal');
        const modalMessage = document.getElementById('modalMessage');
        const modalConfirmBtn = document.getElementById('modalConfirmBtn');
        const modalCancelBtn = document.getElementById('modalCancelBtn');
 
        modalMessage.textContent = message;
        modalOverlay.classList.add('active');
 
        const handleConfirm = () => {
            modalOverlay.classList.remove('active');
            modalConfirmBtn.removeEventListener('click', handleConfirm);
            modalCancelBtn.removeEventListener('click', handleCancel);
            resolve(true);
        };
 
        const handleCancel = () => {
            modalOverlay.classList.remove('active');
            modalConfirmBtn.removeEventListener('click', handleConfirm);
            modalCancelBtn.removeEventListener('click', handleCancel);
            resolve(false);
        };
 
        modalConfirmBtn.addEventListener('click', handleConfirm);
        modalCancelBtn.addEventListener('click', handleCancel);
    });
}
 
// --- Navigation and Routing (Simple) ---
function loadPage(pageName) {
    // This assumes simple HTML files for each page
    window.location.href = `${pageName}.html`;
}
 
// --- Login/Authentication (Placeholder) ---
// In a real application, you'd send credentials to the backend,
// receive a JWT token, and store it in localStorage.
// For this simple example, we'll just simulate a login.
function handleLogin(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const loginError = document.getElementById('loginError');
 
    // Simulate authentication
    if (username === 'admin' && password === 'admin123') {
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('userRole', 'Admin'); // Store role
        window.location.href = 'dashboard.html';
    } else if (username === 'user1' && password === 'user123') {
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('userRole', 'Employee'); // Store role
        window.location.href = 'dashboard.html';
    }
    else {
        loginError.textContent = 'Invalid username or password.';
    }
}
 
function checkLogin() {
    if (!localStorage.getItem('isLoggedIn') || localStorage.getItem('isLoggedIn') !== 'true') {
        // If not on the index (login) page, redirect to login
        if (window.location.pathname.endsWith('/index.html') || window.location.pathname.endsWith('/')) {
            // Already on login page, do nothing
        } else {
            window.location.href = 'index.html';
        }
    }
    // Set visibility based on role (e.g., for admin features)
    const userRole = localStorage.getItem('userRole');
    if (userRole === 'Employee') {
        // Hide admin-specific navigation items
        const adminNavItems = document.querySelectorAll('.admin-only');
        adminNavItems.forEach(item => item.style.display = 'none');
    }
}
 
function logout() {
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('userRole');
    window.location.href = 'index.html';
}
 
// Attach logout to the logout button if present
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
});
 
 
// --- Employee Management ---
 
async function fetchEmployees() {
    try {
        const response = await fetch(`${API_BASE_URL}/employees`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const employees = await response.json();
        renderEmployees(employees);
    } catch (error) {
        console.error('Error fetching employees:', error);
        showMessage('Failed to load employees.', 'error');
    }
}
 
async function fetchDepartments() {
    try {
        const response = await fetch(`${API_BASE_URL}/departments`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching departments:', error);
        showMessage('Failed to load departments.', 'error');
        return [];
    }
}
 
async function fetchDesignations() {
    try {
        const response = await fetch(`${API_BASE_URL}/designations`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching designations:', error);
        showMessage('Failed to load designations.', 'error');
        return [];
    }
}
 
async function populateDropdowns() {
    const departments = await fetchDepartments();
    const designations = await fetchDesignations();
 
    const deptSelects = document.querySelectorAll('select[name="departmentId"]');
    const desgSelects = document.querySelectorAll('select[name="designationId"]');
 
    deptSelects.forEach(select => {
        select.innerHTML = '<option value="">--Select Department--</option>';
        departments.forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.departmentId;
            option.textContent = dept.departmentName;
            select.appendChild(option);
        });
    });
 
    desgSelects.forEach(select => {
        select.innerHTML = '<option value="">--Select Designation--</option>';
        designations.forEach(desg => {
            const option = document.createElement('option');
            option.value = desg.designationId;
            option.textContent = desg.designationName;
            select.appendChild(option);
        });
    });
}
 
async function renderEmployees(employees) {
    const tableBody = document.getElementById('employeeTableBody');
    if (!tableBody) return; // Ensure element exists on the page
 
    tableBody.innerHTML = ''; // Clear existing rows
    if (employees.length === 0) {
        const noDataRow = tableBody.insertRow();
        noDataRow.innerHTML = '<td colspan="7" class="px-6 py-4 whitespace-nowrap text-center text-gray-500">No employees found.</td>';
        return;
    }
 
    const departments = await fetchDepartments();
    const designations = await fetchDesignations();
 
    employees.forEach(emp => {
        const row = tableBody.insertRow();
        const departmentName = departments.find(d => d.departmentId === emp.departmentId)?.departmentName || 'N/A';
        const designationName = designations.find(d => d.designationId === emp.designationId)?.designationName || 'N/A';
 
        row.insertCell().textContent = emp.employeeId;
        row.insertCell().textContent = `${emp.firstName} ${emp.lastName}`;
        row.insertCell().textContent = emp.email;
        row.insertCell().textContent = departmentName;
        row.insertCell().textContent = designationName;
        row.insertCell().textContent = emp.status;
 
        const actionCell = row.insertCell();
        actionCell.className = 'action-buttons';
       
        // Edit Button
        const editBtn = document.createElement('button');
        editBtn.textContent = 'Edit';
        editBtn.className = 'edit-btn';
        editBtn.onclick = () => window.location.href = `add-employee.html?id=${emp.employeeId}`;
        actionCell.appendChild(editBtn);
 
        // Delete Button
        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Delete';
        deleteBtn.className = 'delete-btn';
        deleteBtn.onclick = () => deleteEmployee(emp.employeeId);
        actionCell.appendChild(deleteBtn);
    });
}
 
/**
 * Validates employee form data.
 * @param {HTMLFormElement} form - The employee form element.
 * @returns {boolean} - True if all validations pass, false otherwise.
 */
function validateEmployeeForm(form) {
    const dobInput = form.dateOfBirth.value;
    const dojInput = form.dateOfJoining.value;
    const phoneNumber = form.phoneNumber.value;
    const emergencyContactPhone = form.emergencyContactPhone.value;
 
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Normalize today's date to midnight for comparison
 
    // Date of Birth Validation (not in the future)
    if (dobInput) {
        const dobDate = new Date(dobInput);
        if (dobDate > today) {
            showMessage('Date of Birth cannot be a future date.', 'error');
            return false;
        }
    }
 
    // Date of Joining Validation (not in the past, i.e., today or future)
    if (dojInput) {
        const dojDate = new Date(dojInput);
        if (dojDate < today) {
            showMessage('Date of Joining cannot be a past date.', 'error');
            return false;
        }
    }
 
    // Phone Number Validation (10 digits)
    if (phoneNumber && !/^\d{10}$/.test(phoneNumber)) {
        showMessage('Phone Number must be exactly 10 digits.', 'error');
        return false;
    }
 
    // Emergency Contact Phone Validation (10 digits)
    if (emergencyContactPhone && !/^\d{10}$/.test(emergencyContactPhone)) {
        showMessage('Emergency Contact Phone must be exactly 10 digits.', 'error');
        return false;
    }
 
    return true; // All validations passed
}
 
/**
 * Handles adding a new employee.
 * @param {Event} event - The form submission event.
 */
async function addEmployee(event) {
    const form = event.target;
 
    // Run client-side validation
    if (!validateEmployeeForm(form)) {
        return; // Stop if validation fails
    }
 
    const employeeData = {
        employeeId: form.employeeId.value, // Employee ID might be auto-generated by backend, or entered for new. Adjust as per your system.
        firstName: form.firstName.value,
        lastName: form.lastName.value,
        dateOfBirth: form.dateOfBirth.value || null,
        gender: form.gender.value,
        email: form.email.value,
        phoneNumber: form.phoneNumber.value,
        address: form.address.value,
        dateOfJoining: form.dateOfJoining.value || null,
        departmentId: parseInt(form.departmentId.value),
        designationId: parseInt(form.designationId.value),
        basicSalary: parseFloat(form.basicSalary.value),
        bankAccountNumber: form.bankAccountNumber.value,
        emergencyContactName: form.emergencyContactName.value,
        emergencyContactPhone: form.emergencyContactPhone.value,
        status: form.status.value,
        // Termination details are typically not set when adding a new employee
        // terminationDate: null,
        // terminationReason: null
    };
 
    const url = `${API_BASE_URL}/employees`;
    const method = 'POST';
 
    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(employeeData)
        });
 
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
 
        const result = await response.json();
        showMessage(result.message || 'Employee added successfully!', 'success');
 
        setTimeout(() => {
            window.location.href = 'employees.html';
        }, 1500);
 
    } catch (error) {
        console.error('Error adding employee:', error);
        showMessage(`Failed to add employee: ${error.message}`, 'error');
    }
}
/**
 * Handles updating an existing employee.
 * @param {Event} event - The form submission event.
 */
async function updateEmployee(event) {
    const form = event.target;
    const employeeId = form.employeeId.value;
 
    if (!employeeId) {
        showMessage('Employee ID is required for updating!', 'error');
        return;
    }
 
    // Run client-side validation
    if (!validateEmployeeForm(form)) {
        return; // Stop if validation fails
    }
 
    const employeeData = {
        employeeId: employeeId, // Ensure the ID is part of the data for consistency, though URL also uses it.
        firstName: form.firstName.value,
        lastName: form.lastName.value,
        dateOfBirth: form.dateOfBirth.value || null,
        gender: form.gender.value,
        email: form.email.value,
        phoneNumber: form.phoneNumber.value,
        address: form.address.value,
        dateOfJoining: form.dateOfJoining.value || null,
        departmentId: parseInt(form.departmentId.value),
        designationId: parseInt(form.designationId.value),
        basicSalary: parseFloat(form.basicSalary.value),
        bankAccountNumber: form.bankAccountNumber.value,
        emergencyContactName: form.emergencyContactName.value,
        emergencyContactPhone: form.emergencyContactPhone.value,
        status: form.status.value,
        terminationDate: form.terminationDate.value || null,
        terminationReason: form.terminationReason.value || null
    };
 
    const url = `${API_BASE_URL}/employees/${employeeId}`;
    const method = 'PUT';
 
    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(employeeData)
        });
 
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
 
        const result = await response.json();
        showMessage(result.message || 'Employee updated successfully!', 'success');
 
        setTimeout(() => {
            window.location.href = 'employees.html';
        }, 1500);
 
    } catch (error) {
        console.error('Error updating employee:', error);
        showMessage(`Failed to update employee: ${error.message}`, 'error');
    }
}
 
// Example of a combined handler for a single form that handles both
async function addOrUpdateEmployee(event) {
    event.preventDefault(); // Prevent default form submission
    const form = event.target;
    const employeeId = form.employeeId.value.trim(); // Trim to check for empty string
 
    const isUpdate = !!employeeId; // If employeeId is present, it's an update
 
    if (isUpdate) {
        await updateEmployee(event); // Pass the original event to updateEmployee
    } else {
        await addEmployee(event);    // Pass the original event to addEmployee
    }
}
 
 
async function deleteEmployee(employeeId) {
    const confirmed = await showConfirmModal(`Are you sure you want to delete employee with ID: ${employeeId}?`);
    if (!confirmed) {
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/employees/${employeeId}`, {
            method: 'DELETE'
        });
 
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
 
        const result = await response.json();
        showMessage(result.message, 'success');
        fetchEmployees(); // Refresh the list
    } catch (error) {
        console.error('Error deleting employee:', error);
        showMessage(`Failed to delete employee: ${error.message}`, 'error');
    }
}
 
async function loadEmployeeForEdit() {
    const urlParams = new URLSearchParams(window.location.search);
    const employeeId = urlParams.get('id');
    if (employeeId) {
        document.getElementById('formTitle').textContent = 'Edit Employee';
        document.getElementById('employeeId').readOnly = true; // Make ID non-editable
        document.getElementById('employeeId').value = employeeId; // Set ID
       
        try {
            const response = await fetch(`${API_BASE_URL}/employees/${employeeId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const employee = await response.json();
           
            // Populate form fields
            document.getElementById('firstName').value = employee.firstName || '';
            document.getElementById('lastName').value = employee.lastName || '';
            document.getElementById('dateOfBirth').value = formatDateForInput(employee.dateOfBirth);
            document.getElementById('gender').value = employee.gender || '';
            document.getElementById('email').value = employee.email || '';
            document.getElementById('phoneNumber').value = employee.phoneNumber || '';
            document.getElementById('address').value = employee.address || '';
            document.getElementById('dateOfJoining').value = formatDateForInput(employee.dateOfJoining);
            document.getElementById('departmentId').value = employee.departmentId || '';
            document.getElementById('designationId').value = employee.designationId || '';
            document.getElementById('basicSalary').value = employee.basicSalary || '';
            document.getElementById('bankAccountNumber').value = employee.bankAccountNumber || '';
            document.getElementById('emergencyContactName').value = employee.emergencyContactName || '';
            document.getElementById('emergencyContactPhone').value = employee.emergencyContactPhone || '';
            document.getElementById('status').value = employee.status || '';
            document.getElementById('terminationDate').value = formatDateForInput(employee.terminationDate);
            document.getElementById('terminationReason').value = employee.terminationReason || '';
 
        } catch (error) {
            console.error('Error loading employee for edit:', error);
            showMessage('Failed to load employee for editing.', 'error');
            // Optionally redirect back to employee list if load fails
            setTimeout(() => {
                window.location.href = 'employees.html';
            }, 1500);
        }
    } else {
        document.getElementById('formTitle').textContent = 'Add New Employee';
        document.getElementById('employeeId').readOnly = false;
        // Optionally clear form fields for new entry
        const addEmployeeForm = document.getElementById('addEmployeeForm');
        if (addEmployeeForm) {
            addEmployeeForm.reset(); // Resets all form fields
        }
    }
}
 
 
// --- Department Management ---
async function fetchAndRenderDepartments() {
    const tableBody = document.getElementById('departmentTableBody');
    if (!tableBody) return;
    tableBody.innerHTML = ''; // Clear existing rows
 
    try {
        const departments = await fetchDepartments();
        if (departments.length === 0) {
            const noDataRow = tableBody.insertRow();
            noDataRow.innerHTML = '<td colspan="3" class="px-6 py-4 whitespace-nowrap text-center text-gray-500">No departments found.</td>';
            return;
        }
        departments.forEach(dept => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = dept.departmentId;
            row.insertCell().textContent = dept.departmentName;
 
            const actionCell = row.insertCell();
            actionCell.className = 'action-buttons';
 
            const editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.className = 'edit-btn';
            editBtn.onclick = () => editDepartment(dept.departmentId, dept.departmentName);
            actionCell.appendChild(editBtn);
 
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.className = 'delete-btn';
            deleteBtn.onclick = () => deleteDepartment(dept.departmentId);
            actionCell.appendChild(deleteBtn);
        });
    } catch (error) {
        console.error('Error rendering departments:', error);
    }
}
 
async function addDepartment(event) {
    event.preventDefault();
    const departmentNameInput = document.getElementById('departmentName');
    const departmentName = departmentNameInput.value.trim();
 
    if (!departmentName) {
        showMessage('Department name cannot be empty.', 'error');
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/departments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ departmentName: departmentName })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        departmentNameInput.value = ''; // Clear form
        fetchAndRenderDepartments(); // Refresh list
    } catch (error) {
        console.error('Error adding department:', error);
        showMessage(`Failed to add department: ${error.message}`, 'error');
    }
}
 
let currentEditDepartmentId = null; // To store ID when editing
 
function editDepartment(id, name) {
    document.getElementById('departmentName').value = name;
    document.getElementById('addDepartmentBtn').style.display = 'none';
    document.getElementById('updateDepartmentBtn').style.display = 'inline-block';
    document.getElementById('cancelDepartmentEditBtn').style.display = 'inline-block';
    currentEditDepartmentId = id;
}
 
async function updateDepartment(event) {
    event.preventDefault();
    if (!currentEditDepartmentId) return;
 
    const departmentNameInput = document.getElementById('departmentName');
    const departmentName = departmentNameInput.value.trim();
 
    if (!departmentName) {
        showMessage('Department name cannot be empty.', 'error');
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/departments/${currentEditDepartmentId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ departmentName: departmentName })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        resetDepartmentForm();
        fetchAndRenderDepartments();
    } catch (error) {
        console.error('Error updating department:', error);
        showMessage(`Failed to update department: ${error.message}`, 'error');
    }
}
 
function cancelDepartmentEdit() {
    resetDepartmentForm();
}
 
function resetDepartmentForm() {
    document.getElementById('departmentName').value = '';
    document.getElementById('addDepartmentBtn').style.display = 'inline-block';
    document.getElementById('updateDepartmentBtn').style.display = 'none';
    document.getElementById('cancelDepartmentEditBtn').style.display = 'none';
    currentEditDepartmentId = null;
}
 
async function deleteDepartment(departmentId) {
    const confirmed = await showConfirmModal(`Are you sure you want to delete department ID: ${departmentId}?`);
    if (!confirmed) {
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/departments/${departmentId}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        fetchAndRenderDepartments();
    } catch (error) {
        console.error('Error deleting department:', error);
        showMessage(`Failed to delete department: ${error.message}`, 'error');
    }
}
 
 
// --- Designation Management ---
async function fetchAndRenderDesignations() {
    const tableBody = document.getElementById('designationTableBody');
    if (!tableBody) return;
    tableBody.innerHTML = ''; // Clear existing rows
 
    try {
        const designations = await fetchDesignations();
        if (designations.length === 0) {
            const noDataRow = tableBody.insertRow();
            noDataRow.innerHTML = '<td colspan="3" class="px-6 py-4 whitespace-nowrap text-center text-gray-500">No designations found.</td>';
            return;
        }
        designations.forEach(desg => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = desg.designationId;
            row.insertCell().textContent = desg.designationName;
 
            const actionCell = row.insertCell();
            actionCell.className = 'action-buttons';
 
            const editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.className = 'edit-btn';
            editBtn.onclick = () => editDesignation(desg.designationId, desg.designationName);
            actionCell.appendChild(editBtn);
 
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.className = 'delete-btn';
            deleteBtn.onclick = () => deleteDesignation(desg.designationId);
            actionCell.appendChild(deleteBtn);
        });
    } catch (error) {
        console.error('Error rendering designations:', error);
    }
}
 
async function addDesignation(event) {
    event.preventDefault();
    const designationNameInput = document.getElementById('designationName');
    const designationName = designationNameInput.value.trim();
 
    if (!designationName) {
        showMessage('Designation name cannot be empty.', 'error');
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/designations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ designationName: designationName })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        designationNameInput.value = ''; // Clear form
        fetchAndRenderDesignations(); // Refresh list
    } catch (error) {
        console.error('Error adding designation:', error);
        showMessage(`Failed to add designation: ${error.message}`, 'error');
    }
}
 
let currentEditDesignationId = null; // To store ID when editing
 
function editDesignation(id, name) {
    document.getElementById('designationName').value = name;
    document.getElementById('addDesignationBtn').style.display = 'none';
    document.getElementById('updateDesignationBtn').style.display = 'inline-block';
    document.getElementById('cancelDesignationEditBtn').style.display = 'inline-block';
    currentEditDesignationId = id;
}
 
async function updateDesignation(event) {
    event.preventDefault();
    if (!currentEditDesignationId) return;
 
    const designationNameInput = document.getElementById('designationName');
    const designationName = designationNameInput.value.trim();
 
    if (!designationName) {
        showMessage('Designation name cannot be empty.', 'error');
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/designations/${currentEditDesignationId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ designationName: designationName })
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        resetDesignationForm();
        fetchAndRenderDesignations();
    } catch (error) {
        console.error('Error updating designation:', error);
        showMessage(`Failed to update designation: ${error.message}`, 'error');
    }
}
 
function cancelDesignationEdit() {
    resetDesignationForm();
}
 
function resetDesignationForm() {
    document.getElementById('designationName').value = '';
    document.getElementById('addDesignationBtn').style.display = 'inline-block';
    document.getElementById('updateDesignationBtn').style.display = 'none';
    document.getElementById('cancelDesignationEditBtn').style.display = 'none';
    currentEditDesignationId = null;
}
 
async function deleteDesignation(designationId) {
    const confirmed = await showConfirmModal(`Are you sure you want to delete designation ID: ${designationId}?`);
    if (!confirmed) {
        return;
    }
 
    try {
        const response = await fetch(`${API_BASE_URL}/designations/${designationId}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.json();
        showMessage(result.message, 'success');
        fetchAndRenderDesignations();
    } catch (error) {
        console.error('Error deleting designation:', error);
        showMessage(`Failed to delete designation: ${error.message}`, 'error');
    }
}
 
 
// --- Event Listeners for Page Load ---
document.addEventListener('DOMContentLoaded', () => {
    checkLogin(); // Check login status on every page load
 
    // Login page specific logic
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
 
    // Employees page specific logic
    if (document.getElementById('employeeTableBody')) {
        fetchEmployees();
    }
 
    // Add/Edit Employee page specific logic
    const addEmployeeForm = document.getElementById('addEmployeeForm');
    if (addEmployeeForm) {
        populateDropdowns();
        addEmployeeForm.addEventListener('submit', addOrUpdateEmployee);
        loadEmployeeForEdit(); // Check if editing existing employee
    }
 
    // Departments page specific logic
    const departmentForm = document.getElementById('departmentForm');
    if (departmentForm) {
        fetchAndRenderDepartments();
        // Use form.addEventListener('submit', ...) for buttons inside form for proper submission handling
        document.getElementById('addDepartmentBtn').addEventListener('click', addDepartment);
        document.getElementById('updateDepartmentBtn').addEventListener('click', updateDepartment);
        document.getElementById('cancelDepartmentEditBtn').addEventListener('click', cancelDepartmentEdit);
    }
 
    // Designations page specific logic
    const designationForm = document.getElementById('designationForm');
    if (designationForm) {
        fetchAndRenderDesignations();
        // Use form.addEventListener('submit', ...) for buttons inside form for proper submission handling
        document.getElementById('addDesignationBtn').addEventListener('click', addDesignation);
        document.getElementById('updateDesignationBtn').addEventListener('click', updateDesignation);
        document.getElementById('cancelDesignationEditBtn').addEventListener('click', cancelDesignationEdit);
    }
});
 
 