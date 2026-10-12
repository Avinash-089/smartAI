import React, { useState } from 'react';
import { BrainCircuit, AlertTriangle, CheckCircle, TrendingUp, Sparkles, UserCheck, ShieldAlert } from 'lucide-react';
import { api } from '../services/api';

export default function AiPredictorView({ students }) {
  const [selectedStudent, setSelectedStudent] = useState(students[1] || students[0] || {});
  const [attendance, setAttendance] = useState(selectedStudent.attendancePercentage || 60);
  const [internalMarks, setInternalMarks] = useState(55);
  const [assignmentScore, setAssignmentScore] = useState(58);
  const [studyHours, setStudyHours] = useState(6);
  const [loading, setLoading] = useState(false);
  const [prediction, setPrediction] = useState(null);

  const handleSelectStudent = (student) => {
    setSelectedStudent(student);
    setAttendance(student.attendancePercentage || 75);
    setPrediction(null);
  };

  const runPrediction = async () => {
    setLoading(true);
    try {
      const res = await api.predictPerformance({
        studentId: selectedStudent.id,
        attendancePercentage: parseFloat(attendance),
        internalMarks: parseFloat(internalMarks),
        assignmentScore: parseFloat(assignmentScore),
        studyHoursPerWeek: parseInt(studyHours)
      });
      setPrediction(res);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Intro Banner */}
      <div style={{ 
        background: 'linear-gradient(135deg, #1e1b4b 0%, #312e81 100%)', 
        color: '#ffffff', 
        padding: '24px 28px', 
        borderRadius: '14px', 
        marginBottom: '28px',
        boxShadow: '0 8px 24px rgba(49, 46, 129, 0.25)'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
          <div style={{ background: '#4f46e5', padding: '8px', borderRadius: '10px' }}>
            <BrainCircuit size={24} color="#ffffff" />
          </div>
          <h2 style={{ fontSize: '1.35rem', fontWeight: 700 }}>AI Predictive Academic Performance & At-Risk Classifier</h2>
        </div>
        <p style={{ color: '#c7d2fe', fontSize: '0.92rem', maxWidth: '850px', lineHeight: '1.5' }}>
          Trained predictive model combining historical attendance ratios, continuous assessment scores, and study velocity. 
          Enables proactive institutional intervention before end-semester exam evaluations.
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '28px', marginBottom: '32px' }}>
        {/* Input Parameters Card */}
        <div className="table-card" style={{ padding: '24px', marginBottom: 0 }}>
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '18px' }}>Student Performance Simulator</h3>

          <div className="form-group">
            <label>Select Student Profile</label>
            <select 
              value={selectedStudent.id} 
              onChange={(e) => {
                const s = students.find(st => st.id === parseInt(e.target.value));
                if (s) handleSelectStudent(s);
              }}
            >
              {students.map(s => (
                <option key={s.id} value={s.id}>
                  {s.name} - {s.course} ({s.riskLevel || 'LOW'} Risk)
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
              <label>Attendance Rate</label>
              <span style={{ fontWeight: 700, color: attendance < 65 ? '#ef4444' : attendance < 75 ? '#f59e0b' : '#10b981' }}>
                {attendance}%
              </span>
            </div>
            <input 
              type="range" 
              min="20" 
              max="100" 
              value={attendance}
              onChange={(e) => setAttendance(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>

          <div className="form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
              <label>Internal Assessment Score</label>
              <span style={{ fontWeight: 700 }}>{internalMarks} / 100</span>
            </div>
            <input 
              type="range" 
              min="10" 
              max="100" 
              value={internalMarks}
              onChange={(e) => setInternalMarks(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>

          <div className="form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
              <label>Assignment Completion Rate</label>
              <span style={{ fontWeight: 700 }}>{assignmentScore}%</span>
            </div>
            <input 
              type="range" 
              min="10" 
              max="100" 
              value={assignmentScore}
              onChange={(e) => setAssignmentScore(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>

          <div className="form-group">
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
              <label>Weekly Independent Study Dedication</label>
              <span style={{ fontWeight: 700 }}>{studyHours} hrs/week</span>
            </div>
            <input 
              type="range" 
              min="2" 
              max="35" 
              value={studyHours}
              onChange={(e) => setStudyHours(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>

          <button 
            className="btn btn-primary" 
            style={{ width: '100%', justifyContent: 'center', marginTop: '12px' }}
            onClick={runPrediction}
            disabled={loading}
          >
            <Sparkles size={18} /> {loading ? 'Computing Neural Inference...' : 'Calculate Predicted Performance'}
          </button>
        </div>

        {/* Prediction Results Card */}
        <div className="table-card" style={{ padding: '24px', marginBottom: 0 }}>
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '18px' }}>ML Prognosis & Risk Diagnosis</h3>

          {prediction ? (
            <div>
              <div style={{ 
                background: prediction.riskLevel === 'HIGH' ? '#fef2f2' : prediction.riskLevel === 'MEDIUM' ? '#fffbeb' : '#f0fdf4',
                border: `1px solid ${prediction.riskLevel === 'HIGH' ? '#fecaca' : prediction.riskLevel === 'MEDIUM' ? '#fde68a' : '#bbf7d0'}`,
                borderRadius: '12px',
                padding: '20px',
                marginBottom: '20px'
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                  <span style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)' }}>CLASSIFICATION</span>
                  <span className={`badge badge-${prediction.riskLevel.toLowerCase()}`} style={{ fontSize: '0.9rem', padding: '6px 14px' }}>
                    {prediction.riskLevel} ACADEMIC RISK
                  </span>
                </div>
                <div style={{ display: 'flex', gap: '24px', marginTop: '12px' }}>
                  <div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Predicted Score</div>
                    <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{prediction.predictedPercentage}%</div>
                  </div>
                  <div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Projected GPA</div>
                    <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{prediction.predictedGpa} / 4.0</div>
                  </div>
                </div>
              </div>

              {prediction.riskFactors && prediction.riskFactors.length > 0 && (
                <div style={{ marginBottom: '16px' }}>
                  <h4 style={{ fontSize: '0.9rem', fontWeight: 700, color: '#b91c1c', marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <ShieldAlert size={16} /> Key Risk Determinants
                  </h4>
                  <ul style={{ paddingLeft: '20px', fontSize: '0.86rem', color: '#475569', lineHeight: '1.5' }}>
                    {prediction.riskFactors.map((rf, i) => <li key={i}>{rf}</li>)}
                  </ul>
                </div>
              )}

              {prediction.recommendations && (
                <div>
                  <h4 style={{ fontSize: '0.9rem', fontWeight: 700, color: '#15803d', marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <CheckCircle size={16} /> Prescribed Remedial Interventions
                  </h4>
                  <ul style={{ paddingLeft: '20px', fontSize: '0.86rem', color: '#475569', lineHeight: '1.5' }}>
                    {prediction.recommendations.map((rec, i) => <li key={i}>{rec}</li>)}
                  </ul>
                </div>
              )}
            </div>
          ) : (
            <div style={{ 
              height: '280px', 
              display: 'flex', 
              flexDirection: 'column', 
              alignItems: 'center', 
              justifyContent: 'center', 
              color: 'var(--text-secondary)',
              textAlign: 'center'
            }}>
              <BrainCircuit size={48} color="#cbd5e1" style={{ marginBottom: '12px' }} />
              <p style={{ fontSize: '0.95rem', fontWeight: 500 }}>Adjust parameters and click "Calculate Predicted Performance" to generate AI insights.</p>
            </div>
          )}
        </div>
      </div>

      {/* Flagged Students Summary */}
      <div className="table-card">
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
            <AlertTriangle size={18} color="#ef4444" />
            Currently Flagged At-Risk Students Requiring Departmental Review
          </h3>
        </div>
        <table className="custom-table">
          <thead>
            <tr>
              <th>Student</th>
              <th>Course Program</th>
              <th>Attendance Rate</th>
              <th>Current GPA</th>
              <th>Risk Severity</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {students.filter(s => s.riskLevel === 'HIGH' || s.riskLevel === 'MEDIUM').map((s) => (
              <tr key={s.id}>
                <td style={{ fontWeight: 600 }}>{s.name}</td>
                <td>{s.course}</td>
                <td><span style={{ fontWeight: 700, color: s.attendancePercentage < 65 ? '#ef4444' : '#f59e0b' }}>{s.attendancePercentage}%</span></td>
                <td style={{ fontWeight: 700 }}>{s.currentGpa}</td>
                <td>
                  <span className={`badge badge-${(s.riskLevel || 'low').toLowerCase()}`}>
                    {s.riskLevel}
                  </span>
                </td>
                <td>
                  <button 
                    className="btn btn-sm btn-primary"
                    onClick={() => handleSelectStudent(s)}
                  >
                    Simulate & Intervene
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
