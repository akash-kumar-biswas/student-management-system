// Authentication helper
function getAuthHeader() {
    const auth = sessionStorage.getItem('auth');
    return { 'Authorization': 'Basic ' + auth, 'Content-Type': 'application/json' };
}

function checkAuth() {
    if (!sessionStorage.getItem('auth') || sessionStorage.getItem('userType') !== 'TEACHER') {
        window.location.href = 'index.html';
    }
    document.getElementById('username-display').textContent = sessionStorage.getItem('username');
}

function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

// Tab management
function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    
    document.getElementById(tabName + '-tab').classList.add('active');
    event.target.classList.add('active');
    
    if (tabName === 'students') loadStudents();
    else if (tabName === 'teachers') loadTeachers();
    else if (tabName === 'courses') loadCourses();
    else if (tabName === 'departments') loadDepartments();
}

// Modal management
function showAddStudentModal() {
    loadDepartmentsDropdown('studentDepartment');
    loadCoursesDropdown('studentCourses');
    document.getElementById('addStudentModal').style.display = 'block';
}

function showAddTeacherModal() {
    loadDepartmentsDropdown('teacherDepartment');
    document.getElementById('addTeacherModal').style.display = 'block';
}

function showAddCourseModal() {
    document.getElementById('addCourseModal').style.display = 'block';
}

function showAddDepartmentModal() {
    document.getElementById('addDepartmentModal').style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Load Students
async function loadStudents() {
    try {
        const response = await fetch('/api/students', { headers: getAuthHeader() });
        const students = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>Username</th><th>Name</th><th>Email</th><th>Department</th><th>Courses</th><th>Actions</th></tr></thead><tbody>';
        
        students.forEach(student => {
            const courses = student.courses ? student.courses.map(c => c.code).join(', ') : 'None';
            html += `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.username}</td>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.department.name}</td>
                    <td>${courses}</td>
                    <td>
                        <button class="btn btn-small" onclick="editStudent(${student.id}, '${student.name}', '${student.email}')">Edit</button>
                        <button class="btn btn-danger btn-small" onclick="deleteStudent(${student.id})">Delete</button>
                    </td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('students-list').innerHTML = html;
    } catch (error) {
        document.getElementById('students-list').innerHTML = '<p class="error">Failed to load students</p>';
    }
}

// Load Teachers
async function loadTeachers() {
    try {
        const response = await fetch('/api/students', { headers: getAuthHeader() });
        if (!response.ok) throw new Error('Failed to load');
        
        // Note: There's no get all teachers endpoint, so we'll show a message
        document.getElementById('teachers-list').innerHTML = '<p>Teacher management available through API. No list endpoint implemented.</p>';
    } catch (error) {
        document.getElementById('teachers-list').innerHTML = '<p class="error">Teacher data not available</p>';
    }
}

// Load Courses
async function loadCourses() {
    try {
        const response = await fetch('/api/courses', { headers: getAuthHeader() });
        const courses = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>Code</th><th>Title</th><th>Actions</th></tr></thead><tbody>';
        
        courses.forEach(course => {
            html += `
                <tr>
                    <td>${course.id}</td>
                    <td>${course.code}</td>
                    <td>${course.title}</td>
                    <td>
                        <button class="btn btn-danger btn-small" onclick="deleteCourse(${course.id})">Delete</button>
                    </td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('courses-list').innerHTML = html;
    } catch (error) {
        document.getElementById('courses-list').innerHTML = '<p class="error">Failed to load courses</p>';
    }
}

// Load Departments
async function loadDepartments() {
    try {
        const response = await fetch('/api/departments', { headers: getAuthHeader() });
        const departments = await response.json();
        
        let html = '<table><thead><tr><th>ID</th><th>Name</th></tr></thead><tbody>';
        
        departments.forEach(dept => {
            html += `
                <tr>
                    <td>${dept.id}</td>
                    <td>${dept.name}</td>
                </tr>
            `;
        });
        
        html += '</tbody></table>';
        document.getElementById('departments-list').innerHTML = html;
    } catch (error) {
        document.getElementById('departments-list').innerHTML = '<p class="error">Failed to load departments</p>';
    }
}

// Helper: Load departments dropdown
async function loadDepartmentsDropdown(selectId) {
    try {
        const response = await fetch('/api/departments', { headers: getAuthHeader() });
        const departments = await response.json();
        
        const select = document.getElementById(selectId);
        select.innerHTML = '';
        
        if (departments.length === 0) {
            select.innerHTML = '<option value="">No departments available - Create one first</option>';
        } else {
            departments.forEach(dept => {
                select.innerHTML += `<option value="${dept.id}">${dept.name}</option>`;
            });
        }
    } catch (error) {
        console.error('Failed to load departments');
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Failed to load departments</option>';
    }
}

// Helper: Load courses dropdown
async function loadCoursesDropdown(selectId) {
    try {
        const response = await fetch('/api/courses', { headers: getAuthHeader() });
        const courses = await response.json();
        
        const select = document.getElementById(selectId);
        select.innerHTML = '';
        courses.forEach(course => {
            select.innerHTML += `<option value="${course.id}">${course.code} - ${course.title}</option>`;
        });
    } catch (error) {
        console.error('Failed to load courses');
    }
}

// Add Student
document.getElementById('addStudentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const selectedCourses = Array.from(document.getElementById('studentCourses').selectedOptions).map(o => parseInt(o.value));
    
    const data = {
        username: document.getElementById('studentUsername').value,
        password: document.getElementById('studentPassword').value,
        name: document.getElementById('studentName').value,
        email: document.getElementById('studentEmail').value,
        departmentId: parseInt(document.getElementById('studentDepartment').value),
        courseIds: selectedCourses
    };
    
    try {
        const response = await fetch('/api/students', {
            method: 'POST',
            headers: getAuthHeader(),
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            closeModal('addStudentModal');
            loadStudents();
            e.target.reset();
            alert('Student added successfully!');
        } else {
            alert('Failed to add student');
        }
    } catch (error) {
        alert('Error adding student');
    }
});

// Edit Student
function editStudent(id, name, email) {
    document.getElementById('editStudentId').value = id;
    document.getElementById('editStudentName').value = name;
    document.getElementById('editStudentEmail').value = email;
    document.getElementById('editStudentModal').style.display = 'block';
}

document.getElementById('editStudentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const id = document.getElementById('editStudentId').value;
    const data = {
        name: document.getElementById('editStudentName').value,
        email: document.getElementById('editStudentEmail').value
    };
    
    try {
        const response = await fetch(`/api/students/${id}`, {
            method: 'PUT',
            headers: getAuthHeader(),
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            closeModal('editStudentModal');
            loadStudents();
            alert('Student updated successfully!');
        } else {
            alert('Failed to update student');
        }
    } catch (error) {
        alert('Error updating student');
    }
});

// Delete Student
async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student?')) return;
    
    try {
        const response = await fetch(`/api/students/${id}`, {
            method: 'DELETE',
            headers: getAuthHeader()
        });
        
        if (response.ok) {
            loadStudents();
            alert('Student deleted successfully!');
        } else {
            alert('Failed to delete student');
        }
    } catch (error) {
        alert('Error deleting student');
    }
}

// Add Teacher
document.getElementById('addTeacherForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('teacherName').value,
        email: document.getElementById('teacherEmail').value,
        departmentId: parseInt(document.getElementById('teacherDepartment').value)
    };
    
    try {
        const response = await fetch('/api/teachers', {
            method: 'POST',
            headers: getAuthHeader(),
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            closeModal('addTeacherModal');
            e.target.reset();
            alert('Teacher added successfully!');
        } else {
            alert('Failed to add teacher');
        }
    } catch (error) {
        alert('Error adding teacher');
    }
});

// Add Course
document.getElementById('addCourseForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        code: document.getElementById('courseCode').value,
        title: document.getElementById('courseTitle').value
    };
    
    try {
        const response = await fetch('/api/courses', {
            method: 'POST',
            headers: getAuthHeader(),
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            closeModal('addCourseModal');
            loadCourses();
            e.target.reset();
            alert('Course added successfully!');
        } else {
            alert('Failed to add course');
        }
    } catch (error) {
        alert('Error adding course');
    }
});

// Delete Course
async function deleteCourse(id) {
    if (!confirm('Are you sure you want to delete this course?')) return;
    
    try {
        const response = await fetch(`/api/courses/${id}`, {
            method: 'DELETE',
            headers: getAuthHeader()
        });
        
        if (response.ok) {
            loadCourses();
            alert('Course deleted successfully!');
        } else {
            alert('Failed to delete course');
        }
    } catch (error) {
        alert('Error deleting course');
    }
}

// Add Department
document.getElementById('addDepartmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('departmentName').value
    };
    
    try {
        const response = await fetch('/api/departments', {
            method: 'POST',
            headers: getAuthHeader(),
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            closeModal('addDepartmentModal');
            loadDepartments();
            e.target.reset();
            alert('Department added successfully!');
        } else {
            const errorText = await response.text();
            if (response.status === 500 && errorText.includes('already exists')) {
                alert('This department already exists!');
            } else if (response.status === 403) {
                alert('Access denied! Only teachers can add departments.');
            } else {
                alert('Failed to add department: ' + errorText);
            }
        }
    } catch (error) {
        alert('Error adding department: ' + error.message);
    }
});

// Initialize
checkAuth();
loadStudents();
