import React from 'react';
import { BookOpen, Layers, Clock, Award } from 'lucide-react';

export default function CoursesView({ courses, subjects }) {
  return (
    <div>
      {/* Degree Programs */}
      <div className="table-card" style={{ marginBottom: '28px' }}>
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Undergraduate & Graduate Degree Programs</h3>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '20px', padding: '24px' }}>
          {courses.map((c) => (
            <div key={c.id} style={{ 
              background: '#f8fafc', 
              border: '1px solid var(--border-color)', 
              borderRadius: '12px', 
              padding: '20px' 
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                <span className="badge badge-low" style={{ background: '#e0e7ff', color: '#4338ca' }}>{c.code}</span>
                <span style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <Clock size={14} /> {c.duration} Years Full-Time
                </span>
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 700, margin: '8px 0' }}>{c.name}</h4>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', lineHeight: '1.4' }}>{c.description}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Course Curriculum & Subjects */}
      <div className="table-card">
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Curriculum Subjects & Credit Allocations</h3>
        </div>
        <table className="custom-table">
          <thead>
            <tr>
              <th>Subject Code</th>
              <th>Subject Title</th>
              <th>Associated Program</th>
              <th>Credits</th>
            </tr>
          </thead>
          <tbody>
            {subjects.map((s) => (
              <tr key={s.id}>
                <td style={{ fontWeight: 600 }}>{s.code}</td>
                <td style={{ fontWeight: 600 }}>{s.name}</td>
                <td>{s.courseName}</td>
                <td>
                  <span className="badge badge-low" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                    <Award size={12} /> {s.credits} Credits
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
