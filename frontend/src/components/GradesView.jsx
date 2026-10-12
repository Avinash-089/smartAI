import React, { useState } from 'react';
import { Award, Plus, FileText, CheckCircle2 } from 'lucide-react';

export default function GradesView({ students }) {
  const [gradesList, setGradesList] = useState([
    { id: 1, studentName: 'Avinash', subject: 'Data Structures & Algorithms', exam: 'Midterm', marks: 88, max: 100, grade: 'A' },
    { id: 2, studentName: 'Priya Sharma', subject: 'Machine Learning Fundamentals', exam: 'Midterm', marks: 52, max: 100, grade: 'D' },
    { id: 3, studentName: 'Rahul Varma', subject: 'Cloud Computing & DevOps', exam: 'Midterm', marks: 74, max: 100, grade: 'B' },
    { id: 4, studentName: 'Sneha Patel', subject: 'Database Management Systems', exam: 'Midterm', marks: 95, max: 100, grade: 'A+' }
  ]);

  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    studentName: 'Avinash',
    subject: 'Data Structures & Algorithms',
    exam: 'Midterm',
    marks: 85,
    max: 100
  });

  const handleAddGrade = (e) => {
    e.preventDefault();
    const pct = (formData.marks / formData.max) * 100;
    let grade = 'F';
    if (pct >= 90) grade = 'A+';
    else if (pct >= 80) grade = 'A';
    else if (pct >= 70) grade = 'B';
    else if (pct >= 60) grade = 'C';
    else if (pct >= 50) grade = 'D';

    setGradesList([...gradesList, { ...formData, id: Date.now(), grade }]);
    setShowModal(false);
  };

  return (
    <div>
      <div className="table-card">
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Semester Examination Results & Gradebook</h3>
          <button className="btn btn-primary" onClick={() => setShowModal(true)}>
            <Plus size={16} /> Enter Examination Marks
          </button>
        </div>

        <table className="custom-table">
          <thead>
            <tr>
              <th>Student Name</th>
              <th>Course Subject</th>
              <th>Assessment Type</th>
              <th>Marks Scored</th>
              <th>Percentage</th>
              <th>Assigned Grade</th>
            </tr>
          </thead>
          <tbody>
            {gradesList.map((g) => {
              const pct = Math.round((g.marks / g.max) * 100);
              return (
                <tr key={g.id}>
                  <td style={{ fontWeight: 600 }}>{g.studentName}</td>
                  <td>{g.subject}</td>
                  <td><span className="badge badge-low" style={{ background: '#f1f5f9', color: '#475569' }}>{g.exam}</span></td>
                  <td style={{ fontWeight: 600 }}>{g.marks} / {g.max}</td>
                  <td>{pct}%</td>
                  <td>
                    <span className={`badge ${pct >= 80 ? 'badge-low' : pct >= 60 ? 'badge-medium' : 'badge-high'}`}>
                      {g.grade}
                    </span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '16px' }}>Input Exam Marks</h3>
            <form onSubmit={handleAddGrade}>
              <div className="form-group">
                <label>Select Student</label>
                <select 
                  value={formData.studentName} 
                  onChange={(e) => setFormData({ ...formData, studentName: e.target.value })}
                >
                  {students.map(s => <option key={s.id} value={s.name}>{s.name} ({s.rollNumber || `ID:${s.id}`})</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>Subject</label>
                <select 
                  value={formData.subject}
                  onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                >
                  <option value="Data Structures & Algorithms">Data Structures & Algorithms</option>
                  <option value="Machine Learning Fundamentals">Machine Learning Fundamentals</option>
                  <option value="Database Management Systems">Database Management Systems</option>
                  <option value="Cloud Computing & DevOps">Cloud Computing & DevOps</option>
                </select>
              </div>
              <div className="form-group">
                <label>Exam Type</label>
                <select 
                  value={formData.exam}
                  onChange={(e) => setFormData({ ...formData, exam: e.target.value })}
                >
                  <option value="Midterm">Midterm Examination</option>
                  <option value="End Semester">End Semester Final</option>
                  <option value="Continuous Assessment">Continuous Assessment / Assignment</option>
                </select>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
                <div className="form-group">
                  <label>Marks Scored</label>
                  <input 
                    type="number" 
                    required 
                    value={formData.marks}
                    onChange={(e) => setFormData({ ...formData, marks: parseFloat(e.target.value) })}
                  />
                </div>
                <div className="form-group">
                  <label>Maximum Marks</label>
                  <input 
                    type="number" 
                    required 
                    value={formData.max}
                    onChange={(e) => setFormData({ ...formData, max: parseFloat(e.target.value) })}
                  />
                </div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '20px' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  Save Result
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
