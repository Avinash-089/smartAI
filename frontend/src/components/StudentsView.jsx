import React, { useState } from 'react';
import { Users, Plus, Search, Filter, Trash2, Edit, AlertCircle, ShieldAlert } from 'lucide-react';
import { api } from '../services/api';

export default function StudentsView({ students, setStudents, onRefresh }) {
  const [search, setSearch] = useState('');
  const [courseFilter, setCourseFilter] = useState('');
  const [riskFilter, setRiskFilter] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    course: 'Computer Science & Engineering',
    semester: 1,
    rollNumber: '',
    attendancePercentage: 85.0,
    currentGpa: 3.5,
    riskLevel: 'LOW'
  });

  const filtered = students.filter(s => {
    const matchesSearch = !search || s.name.toLowerCase().includes(search.toLowerCase()) || s.email.toLowerCase().includes(search.toLowerCase());
    const matchesCourse = !courseFilter || s.course === courseFilter;
    const matchesRisk = !riskFilter || s.riskLevel === riskFilter;
    return matchesSearch && matchesCourse && matchesRisk;
  });

  const handleAddStudent = async (e) => {
    e.preventDefault();
    const created = await api.createStudent(formData);
    setStudents([...students, created]);
    setShowAddModal(false);
    setFormData({
      name: '',
      email: '',
      phone: '',
      course: 'Computer Science & Engineering',
      semester: 1,
      rollNumber: '',
      attendancePercentage: 85.0,
      currentGpa: 3.5,
      riskLevel: 'LOW'
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this student record?')) {
      await api.deleteStudent(id);
      setStudents(students.filter(s => s.id !== id));
    }
  };

  return (
    <div>
      <div className="table-card">
        <div className="table-header-bar">
          <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', alignItems: 'center' }}>
            <div style={{ position: 'relative' }}>
              <input 
                type="text" 
                placeholder="Search student by name or email..." 
                className="search-input"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>

            <select 
              className="search-input" 
              style={{ width: 'auto' }}
              value={courseFilter}
              onChange={(e) => setCourseFilter(e.target.value)}
            >
              <option value="">All Courses</option>
              <option value="Computer Science & Engineering">Computer Science</option>
              <option value="Artificial Intelligence & Data Science">AI & Data Science</option>
              <option value="Information Technology">Information Technology</option>
            </select>

            <select 
              className="search-input" 
              style={{ width: 'auto' }}
              value={riskFilter}
              onChange={(e) => setRiskFilter(e.target.value)}
            >
              <option value="">All Risk Levels</option>
              <option value="LOW">Low Risk</option>
              <option value="MEDIUM">Medium Risk</option>
              <option value="HIGH">High Risk (At-Risk)</option>
            </select>
          </div>

          <button className="btn btn-primary" onClick={() => setShowAddModal(true)}>
            <Plus size={16} /> Enroll New Student
          </button>
        </div>

        <table className="custom-table">
          <thead>
            <tr>
              <th>Roll #</th>
              <th>Student Name</th>
              <th>Program & Sem</th>
              <th>Contact</th>
              <th>Attendance</th>
              <th>GPA</th>
              <th>AI Risk Flag</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((s) => (
              <tr key={s.id}>
                <td style={{ fontWeight: 600 }}>{s.rollNumber || `STU-${s.id}`}</td>
                <td>
                  <div style={{ fontWeight: 600 }}>{s.name}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>{s.email}</div>
                </td>
                <td>
                  <div>{s.course}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Semester {s.semester}</div>
                </td>
                <td>{s.phone || 'N/A'}</td>
                <td>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span style={{ fontWeight: 600 }}>{s.attendancePercentage || 0}%</span>
                    <div style={{ width: '40px', height: '6px', background: '#e2e8f0', borderRadius: '3px', overflow: 'hidden' }}>
                      <div style={{ 
                        width: `${s.attendancePercentage || 0}%`, 
                        height: '100%', 
                        background: (s.attendancePercentage || 0) < 65 ? '#ef4444' : (s.attendancePercentage || 0) < 75 ? '#f59e0b' : '#10b981' 
                      }} />
                    </div>
                  </div>
                </td>
                <td>
                  <span style={{ fontWeight: 700 }}>{s.currentGpa || 'N/A'}</span>
                </td>
                <td>
                  <span className={`badge badge-${(s.riskLevel || 'low').toLowerCase()}`}>
                    {s.riskLevel === 'HIGH' && <ShieldAlert size={12} style={{ display: 'inline', marginRight: 4 }} />}
                    {s.riskLevel || 'LOW'}
                  </span>
                </td>
                <td>
                  <button 
                    className="btn btn-sm btn-danger" 
                    onClick={() => handleDelete(s.id)}
                    title="Delete Student"
                  >
                    <Trash2 size={14} />
                  </button>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td colSpan="8" style={{ textAlign: 'center', padding: '32px', color: 'var(--text-secondary)' }}>
                  No students found matching current filters.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Add Student Modal */}
      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '16px' }}>Enroll New Student</h3>
            <form onSubmit={handleAddStudent}>
              <div className="form-group">
                <label>Full Name</label>
                <input 
                  type="text" 
                  required 
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Ramesh Kumar"
                />
              </div>
              <div className="form-group">
                <label>Email Address</label>
                <input 
                  type="email" 
                  required 
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  placeholder="e.g. ramesh@example.com"
                />
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <input 
                  type="text" 
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  placeholder="e.g. 9876543210"
                />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
                <div className="form-group">
                  <label>Degree Program</label>
                  <select 
                    value={formData.course}
                    onChange={(e) => setFormData({ ...formData, course: e.target.value })}
                  >
                    <option value="Computer Science & Engineering">Computer Science & Engineering</option>
                    <option value="Artificial Intelligence & Data Science">AI & Data Science</option>
                    <option value="Information Technology">Information Technology</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Semester</label>
                  <input 
                    type="number" 
                    min="1" 
                    max="8" 
                    value={formData.semester}
                    onChange={(e) => setFormData({ ...formData, semester: parseInt(e.target.value) })}
                  />
                </div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '20px' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowAddModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  Enroll Student
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
