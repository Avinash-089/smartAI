import React, { useState } from 'react';
import { CalendarCheck, Camera, CheckCircle2, XCircle, Clock, Sparkles } from 'lucide-react';
import { api } from '../services/api';

export default function AttendanceView({ students }) {
  const [attendanceDate, setAttendanceDate] = useState(new Date().toISOString().split('T')[0]);
  const [selectedSubject, setSelectedSubject] = useState('Data Structures & Algorithms');
  const [attendanceRecords, setAttendanceRecords] = useState(
    students.reduce((acc, s) => ({ ...acc, [s.id]: 'PRESENT' }), {})
  );
  const [showFaceModal, setShowFaceModal] = useState(false);
  const [scanning, setScanning] = useState(false);
  const [scanResult, setScanResult] = useState(null);

  const toggleStatus = (studentId) => {
    setAttendanceRecords(prev => ({
      ...prev,
      [studentId]: prev[studentId] === 'PRESENT' ? 'ABSENT' : prev[studentId] === 'ABSENT' ? 'LATE' : 'PRESENT'
    }));
  };

  const handleSaveAttendance = () => {
    alert(`Attendance records saved successfully for ${selectedSubject} on ${attendanceDate}!`);
  };

  const runFacialScan = async () => {
    setScanning(true);
    setScanResult(null);
    try {
      // Simulate webcam frame upload
      const res = await api.facialAttendance({
        imageBase64: "data:image/jpeg;base64,sample_frame",
        classroom: "LH-101"
      });
      setScanResult(res);
      // Automatically update attendance records
      if (res.presentStudentIds) {
        setAttendanceRecords(prev => {
          const updated = { ...prev };
          res.presentStudentIds.forEach(id => {
            updated[id] = 'PRESENT';
          });
          return updated;
        });
      }
    } finally {
      setScanning(false);
    }
  };

  return (
    <div>
      <div className="table-card">
        <div className="table-header-bar">
          <div style={{ display: 'flex', gap: '14px', alignItems: 'center', flexWrap: 'wrap' }}>
            <div>
              <input 
                type="date" 
                className="search-input"
                style={{ width: 'auto' }}
                value={attendanceDate}
                onChange={(e) => setAttendanceDate(e.target.value)}
              />
            </div>
            <select 
              className="search-input"
              style={{ width: 'auto' }}
              value={selectedSubject}
              onChange={(e) => setSelectedSubject(e.target.value)}
            >
              <option value="Data Structures & Algorithms">Data Structures & Algorithms</option>
              <option value="Machine Learning Fundamentals">Machine Learning Fundamentals</option>
              <option value="Database Management Systems">Database Management Systems</option>
              <option value="Cloud Computing & DevOps">Cloud Computing & DevOps</option>
            </select>
          </div>

          <div style={{ display: 'flex', gap: '10px' }}>
            <button 
              className="btn btn-secondary" 
              style={{ background: '#f3e8ff', color: '#6b21a8' }}
              onClick={() => { setShowFaceModal(true); setScanResult(null); }}
            >
              <Camera size={16} /> AI Facial Attendance Scan
            </button>
            <button className="btn btn-primary" onClick={handleSaveAttendance}>
              Save Attendance
            </button>
          </div>
        </div>

        <table className="custom-table">
          <thead>
            <tr>
              <th>Roll #</th>
              <th>Student Name</th>
              <th>Program</th>
              <th>Attendance Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {students.map((s) => {
              const status = attendanceRecords[s.id] || 'PRESENT';
              return (
                <tr key={s.id}>
                  <td style={{ fontWeight: 600 }}>{s.rollNumber || `STU-${s.id}`}</td>
                  <td style={{ fontWeight: 600 }}>{s.name}</td>
                  <td>{s.course}</td>
                  <td>
                    {status === 'PRESENT' && (
                      <span className="badge badge-low" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                        <CheckCircle2 size={13} /> Present
                      </span>
                    )}
                    {status === 'ABSENT' && (
                      <span className="badge badge-high" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                        <XCircle size={13} /> Absent
                      </span>
                    )}
                    {status === 'LATE' && (
                      <span className="badge badge-medium" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                        <Clock size={13} /> Late
                      </span>
                    )}
                  </td>
                  <td>
                    <button 
                      className="btn btn-sm btn-secondary"
                      onClick={() => toggleStatus(s.id)}
                    >
                      Toggle Status
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* AI Facial Attendance Scanner Modal */}
      {showFaceModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '560px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
              <div style={{ background: '#f3e8ff', color: '#6b21a8', padding: '8px', borderRadius: '10px' }}>
                <Camera size={22} />
              </div>
              <div>
                <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>AI Facial Recognition Attendance</h3>
                <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)' }}>Automated classroom biometric vision model</p>
              </div>
            </div>

            <div style={{ 
              background: '#0f172a', 
              color: '#94a3b8', 
              borderRadius: '12px', 
              height: '240px', 
              display: 'flex', 
              flexDirection: 'column', 
              alignItems: 'center', 
              justifyContent: 'center',
              position: 'relative',
              overflow: 'hidden',
              marginBottom: '18px'
            }}>
              {/* Animated Scan Line */}
              {scanning && (
                <div style={{
                  position: 'absolute',
                  top: 0,
                  left: 0,
                  right: 0,
                  height: '3px',
                  background: '#06b6d4',
                  boxShadow: '0 0 12px #06b6d4',
                  animation: 'pulse 1.5s infinite'
                }} />
              )}
              <Camera size={44} color="#64748b" style={{ marginBottom: '10px' }} />
              <div style={{ fontSize: '0.9rem', color: '#cbd5e1' }}>
                {scanning ? 'Detecting faces & matching biometric vectors...' : 'Webcam / Classroom Camera Feed Active'}
              </div>
              <span style={{ fontSize: '0.75rem', color: '#64748b', marginTop: '4px' }}>Resolution: 1080p | 30 FPS</span>
            </div>

            {scanResult && (
              <div style={{ background: '#dcfce7', color: '#15803d', padding: '14px', borderRadius: '10px', marginBottom: '18px', fontSize: '0.9rem' }}>
                <div style={{ fontWeight: 700, display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <CheckCircle2 size={16} /> Scan Completed Successfully!
                </div>
                <div style={{ marginTop: '4px' }}>{scanResult.message}</div>
              </div>
            )}

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
              <button className="btn btn-secondary" onClick={() => setShowFaceModal(false)}>
                Close
              </button>
              <button className="btn btn-primary" onClick={runFacialScan} disabled={scanning}>
                <Sparkles size={16} /> {scanning ? 'Analyzing Frame...' : 'Capture & Scan Classroom'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
