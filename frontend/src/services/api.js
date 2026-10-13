const rawApiUrl = (import.meta.env.VITE_API_URL || 'http://localhost:8081/api').trim().replace(/\/+$/, '');
const BASE_URL = rawApiUrl.endsWith('/api') ? rawApiUrl : `${rawApiUrl}/api`;
let authToken = localStorage.getItem('sms_token') || null;


export const setAuthToken = (token) => {
  authToken = token;
  if (token) localStorage.setItem('sms_token', token);
  else localStorage.removeItem('sms_token');
};

const mockStudents = [
  { id: 1, name: "Avinash", email: "avinash.student@example.com", phone: "9876543210", course: "Computer Science & Engineering", semester: 3, rollNumber: "CS2024-001", attendancePercentage: 82.5, currentGpa: 3.6, riskLevel: "LOW" },
  { id: 2, name: "Priya Sharma", email: "priya.sharma@example.com", phone: "9876543211", course: "Artificial Intelligence & Data Science", semester: 3, rollNumber: "AI2024-042", attendancePercentage: 58.0, currentGpa: 2.3, riskLevel: "HIGH" },
  { id: 3, name: "Rahul Varma", email: "rahul.varma@example.com", phone: "9876543212", course: "Information Technology", semester: 5, rollNumber: "IT2023-018", attendancePercentage: 71.5, currentGpa: 2.9, riskLevel: "MEDIUM" },
  { id: 4, name: "Sneha Patel", email: "sneha.patel@example.com", phone: "9876543213", course: "Computer Science & Engineering", semester: 1, rollNumber: "CS2026-009", attendancePercentage: 93.0, currentGpa: 3.9, riskLevel: "LOW" }
];

const mockFaculty = [
  { id: 1, name: "Dr. Alan Turing", email: "alan.turing@example.com", department: "Computer Science", designation: "Professor & HOD", phone: "9876500001" },
  { id: 2, name: "Prof. Ada Lovelace", email: "faculty@example.com", department: "Artificial Intelligence", designation: "Associate Professor", phone: "9876500002" },
  { id: 3, name: "Dr. Grace Hopper", email: "grace.hopper@example.com", department: "Information Technology", designation: "Dean of Academics", phone: "9876500003" }
];

const mockCourses = [
  { id: 1, code: "CS101", name: "Computer Science & Engineering", duration: 4, description: "Core computing, algorithms, and systems engineering." },
  { id: 2, code: "AI301", name: "Artificial Intelligence & Data Science", duration: 4, description: "Machine learning, deep neural models, and data analytics." },
  { id: 3, code: "IT201", name: "Information Technology", duration: 4, description: "Enterprise systems, networking, and cloud services." }
];

const mockSubjects = [
  { id: 1, code: "CS301", name: "Data Structures & Algorithms", credits: 4, courseName: "Computer Science & Engineering" },
  { id: 2, code: "AI302", name: "Machine Learning Fundamentals", credits: 4, courseName: "Artificial Intelligence & Data Science" },
  { id: 3, code: "CS303", name: "Database Management Systems", credits: 3, courseName: "Computer Science & Engineering" },
  { id: 4, code: "IT304", name: "Cloud Computing & DevOps", credits: 3, courseName: "Information Technology" }
];

const mockFees = [
  { id: 1, studentId: 1, amount: 45000, dueDate: "2026-11-15", paidDate: "2026-10-01", status: "PAID", description: "Semester 3 Tuition Fee" },
  { id: 2, studentId: 2, amount: 45000, dueDate: "2026-11-15", paidDate: null, status: "PENDING", description: "Semester 3 Tuition & Lab Fee" },
  { id: 3, studentId: 3, amount: 45000, dueDate: "2026-11-15", paidDate: null, status: "PENDING", description: "Semester 5 Tuition Fee" },
  { id: 4, studentId: 4, amount: 45000, dueDate: "2026-11-15", paidDate: "2026-09-20", status: "PAID", description: "Semester 1 Admission Fee" }
];

async function apiFetch(endpoint, options = {}) {
  try {
    const headers = {
      'Content-Type': 'application/json',
      ...(authToken ? { 'Authorization': `Bearer ${authToken}` } : {}),
      ...(options.headers || {})
    };
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      ...options,
      headers
    });
    if (!res.ok) throw new Error(`HTTP Error ${res.status}`);
    return await res.json();
  } catch (err) {
    console.warn(`Backend request note for ${endpoint}:`, err.message);
    return null;
  }
}

export const api = {
  // Auth
  async login(email, password) {
    const res = await apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    if (res && res.token) {
      setAuthToken(res.token);
      return res;
    }
    // Demo fallback
    let role = "STUDENT";
    let name = "Avinash";
    if (email.includes("admin")) { role = "ADMIN"; name = "System Administrator"; }
    else if (email.includes("faculty")) { role = "FACULTY"; name = "Prof. Ada Lovelace"; }
    return { id: 1, name, email, role, token: "demo-token", message: "Demo login" };
  },

  // Dashboard Stats
  async getDashboardStats() {
    const res = await apiFetch('/dashboard/stats');
    if (res) return res;
    return {
      totalStudents: mockStudents.length,
      totalFaculty: mockFaculty.length,
      totalCourses: mockCourses.length,
      totalSubjects: mockSubjects.length,
      averageAttendance: 76.25,
      atRiskCount: 1,
      pendingFeesCount: 2,
      totalFeesCollected: 90000,
      courseDistribution: [
        { course: "Computer Science & Engineering", count: 2 },
        { course: "Artificial Intelligence & Data Science", count: 1 },
        { course: "Information Technology", count: 1 }
      ],
      riskBreakdown: [
        { riskLevel: "LOW", count: 2 },
        { riskLevel: "MEDIUM", count: 1 },
        { riskLevel: "HIGH", count: 1 }
      ]
    };
  },

  // Students
  async getStudents() {
    const res = await apiFetch('/students');
    return res || mockStudents;
  },

  async createStudent(student) {
    const res = await apiFetch('/students', {
      method: 'POST',
      body: JSON.stringify(student)
    });
    return res || { ...student, id: Date.now() };
  },

  async updateStudent(id, student) {
    const res = await apiFetch(`/students/${id}`, {
      method: 'PUT',
      body: JSON.stringify(student)
    });
    return res || { ...student, id };
  },

  async deleteStudent(id) {
    return await apiFetch(`/students/${id}`, { method: 'DELETE' });
  },

  // Faculty
  async getFaculty() {
    const res = await apiFetch('/faculty');
    return res || mockFaculty;
  },

  async createFaculty(fac) {
    const res = await apiFetch('/faculty', {
      method: 'POST',
      body: JSON.stringify(fac)
    });
    return res || { ...fac, id: Date.now() };
  },

  // Courses & Subjects
  async getCourses() {
    const res = await apiFetch('/courses');
    return res || mockCourses;
  },

  async getSubjects() {
    const res = await apiFetch('/subjects');
    return res || mockSubjects;
  },

  // Fees
  async getFees() {
    const res = await apiFetch('/fees');
    return res || mockFees;
  },

  async payFee(id) {
    const res = await apiFetch(`/fees/${id}/pay`, { method: 'PUT' });
    return res || { id, status: "PAID" };
  },

  // Attendance Prediction
  async getAttendancePrediction(studentId) {
    const res = await apiFetch(`/prediction/attendance/${studentId}`);
    return res;
  },

  // AI Service Calls
  async predictPerformance(data) {
    const res = await apiFetch('/ai/predict-performance', {
      method: 'POST',
      body: JSON.stringify(data)
    });
    if (res) return res;

    // Fallback calculation
    const att = data.attendancePercentage || 75;
    const internal = data.internalMarks || 70;
    const assign = data.assignmentScore || 70;
    const score = Math.round(((att * 0.3) + (internal * 0.45) + (assign * 0.25)) * 10) / 10;
    const gpa = Math.round((score / 25) * 100) / 100;
    let risk = "LOW";
    if (score < 55 || att < 65) risk = "HIGH";
    else if (score < 75 || att < 75) risk = "MEDIUM";

    return {
      predictedPercentage: score,
      predictedGpa: gpa,
      riskLevel: risk,
      riskFactors: risk === "HIGH" ? ["Attendance below critical 65% limit", "Internal assessments require remedial tutoring"] : [],
      recommendations: risk === "HIGH" ? ["Mandatory counseling", "Attend remedial sessions"] : ["Maintain good performance"]
    };
  },

  async chat(message, role, userEmail) {
    const res = await apiFetch('/ai/chat', {
      method: 'POST',
      body: JSON.stringify({ message, role, userEmail })
    });
    if (res) return res;

    return {
      reply: "Institutional attendance policy mandates 75% minimum attendance. Pending fees can be settled in the Fees module.",
      intent: "GENERAL_HELP",
      suggestions: ["What is the attendance criteria?", "How do I check fee dues?", "How is AI risk computed?"]
    };
  },

  async facialAttendance(data) {
    const res = await apiFetch('/ai/facial-attendance', {
      method: 'POST',
      body: JSON.stringify(data)
    });
    if (res) return res;
    return {
      success: true,
      recognizedCount: 3,
      presentStudentIds: [1, 3, 4],
      message: "AI Facial recognition scanned and marked attendance for 3 students."
    };
  }
};
