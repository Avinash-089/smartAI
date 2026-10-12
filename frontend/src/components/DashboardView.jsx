import React from 'react';
import { 
  Users, 
  GraduationCap, 
  BookOpen, 
  CalendarCheck, 
  AlertTriangle, 
  CreditCard,
  TrendingUp,
  BrainCircuit,
  ArrowRight
} from 'lucide-react';

export default function DashboardView({ stats, onNavigate }) {
  return (
    <div>
      {/* KPI Cards Grid */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#e0e7ff', color: '#4338ca' }}>
            <Users size={24} />
          </div>
          <div className="stat-info">
            <h3>{stats.totalStudents || 0}</h3>
            <p>Enrolled Students</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#dbeafe', color: '#1d4ed8' }}>
            <GraduationCap size={24} />
          </div>
          <div className="stat-info">
            <h3>{stats.totalFaculty || 0}</h3>
            <p>Faculty Members</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#fef3c7', color: '#b45309' }}>
            <BookOpen size={24} />
          </div>
          <div className="stat-info">
            <h3>{stats.totalCourses || 0}</h3>
            <p>Active Programs</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#dcfce7', color: '#15803d' }}>
            <CalendarCheck size={24} />
          </div>
          <div className="stat-info">
            <h3>{stats.averageAttendance || 0}%</h3>
            <p>Avg. Attendance</p>
          </div>
        </div>

        <div className="stat-card" style={{ borderLeft: '4px solid #ef4444' }}>
          <div className="stat-icon" style={{ background: '#fee2e2', color: '#b91c1c' }}>
            <AlertTriangle size={24} />
          </div>
          <div className="stat-info">
            <h3 style={{ color: '#b91c1c' }}>{stats.atRiskCount || 0}</h3>
            <p>At-Risk Students (AI Flagged)</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon" style={{ background: '#f3e8ff', color: '#7e22ce' }}>
            <CreditCard size={24} />
          </div>
          <div className="stat-info">
            <h3>₹{(stats.totalFeesCollected || 0).toLocaleString()}</h3>
            <p>Fees Collected</p>
          </div>
        </div>
      </div>

      {/* Highlights & AI Callout */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '24px', marginBottom: '28px' }}>
        {/* AI Predictive Insight Banner */}
        <div style={{ 
          background: 'linear-gradient(135deg, #1e1b4b 0%, #312e81 100%)', 
          color: '#ffffff', 
          padding: '24px', 
          borderRadius: '14px',
          boxShadow: '0 8px 24px rgba(49, 46, 129, 0.25)',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'space-between'
        }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '12px' }}>
              <div style={{ background: '#4f46e5', padding: '6px', borderRadius: '8px' }}>
                <BrainCircuit size={22} color="#ffffff" />
              </div>
              <h3 style={{ fontSize: '1.15rem', fontWeight: 700 }}>AI Early Intervention Engine</h3>
            </div>
            <p style={{ color: '#c7d2fe', fontSize: '0.9rem', lineHeight: '1.5', marginBottom: '16px' }}>
              The system analyzes student attendance velocity, internal test distributions, and course complexity. Currently 
              <strong style={{ color: '#f87171' }}> {stats.atRiskCount || 1} student(s)</strong> require immediate academic counseling to prevent exam detention.
            </p>
          </div>
          <button 
            className="btn btn-primary"
            style={{ alignSelf: 'flex-start', background: '#6366f1' }}
            onClick={() => onNavigate('ai-predictor')}
          >
            Open AI Risk Center <ArrowRight size={16} />
          </button>
        </div>

        {/* Course Enrollment Breakdown */}
        <div className="table-card" style={{ marginBottom: 0, padding: '24px' }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <TrendingUp size={18} color="#4f46e5" />
            Program Distributions
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            {(stats.courseDistribution || []).map((c, i) => (
              <div key={i}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.86rem', marginBottom: '4px', fontWeight: 500 }}>
                  <span>{c.course}</span>
                  <span style={{ fontWeight: 700 }}>{c.count} students</span>
                </div>
                <div style={{ height: '8px', background: '#f1f5f9', borderRadius: '4px', overflow: 'hidden' }}>
                  <div style={{ 
                    width: `${Math.min(100, (c.count / (stats.totalStudents || 1)) * 100)}%`, 
                    height: '100%', 
                    background: i === 0 ? '#4f46e5' : i === 1 ? '#06b6d4' : '#8b5cf6',
                    borderRadius: '4px' 
                  }} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Quick Action Pills */}
      <div className="table-card" style={{ padding: '20px 24px' }}>
        <h3 style={{ fontSize: '0.95rem', fontWeight: 700, marginBottom: '14px', color: 'var(--text-secondary)' }}>
          QUICK ADMINISTRATION SHORTCUTS
        </h3>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          <button className="btn btn-secondary" onClick={() => onNavigate('students')}>
            <Users size={16} /> Manage Students
          </button>
          <button className="btn btn-secondary" onClick={() => onNavigate('attendance')}>
            <CalendarCheck size={16} /> Mark Facial Attendance
          </button>
          <button className="btn btn-secondary" onClick={() => onNavigate('grades')}>
            <GraduationCap size={16} /> Input Marks
          </button>
          <button className="btn btn-secondary" onClick={() => onNavigate('fees')}>
            <CreditCard size={16} /> Review Pending Fees
          </button>
          <button className="btn btn-primary" onClick={() => onNavigate('ai-predictor')}>
            <BrainCircuit size={16} /> Run ML Predictions
          </button>
        </div>
      </div>
    </div>
  );
}
