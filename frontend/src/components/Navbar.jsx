import React from 'react';
import { UserCheck, ShieldCheck, School, Sparkles } from 'lucide-react';

export default function Navbar({ user, onSwitchRole, currentTab }) {
  const titles = {
    dashboard: { title: 'Institutional Overview', subtitle: 'Real-time metrics, analytics & student indicators' },
    students: { title: 'Student Management', subtitle: 'Manage student profiles, enrollments and risk flags' },
    faculty: { title: 'Faculty Directory', subtitle: 'Academic staff, departmental allocations and designations' },
    courses: { title: 'Courses & Curriculum', subtitle: 'Degree programs, course structures and credit hours' },
    attendance: { title: 'Smart Attendance Register', subtitle: 'Automated facial recognition & manual session tracking' },
    grades: { title: 'Examinations & Performance', subtitle: 'Semester results, grade point averages and marks entry' },
    fees: { title: 'Tuition & Fee Accounts', subtitle: 'Invoices, payment reconciliation and dues tracking' },
    'ai-predictor': { title: 'AI Academic Risk Predictor', subtitle: 'Machine learning performance forecasting & early intervention' }
  };

  const headerInfo = titles[currentTab] || { title: 'Dashboard', subtitle: 'Manage your institution' };

  return (
    <header className="top-header">
      <div className="header-title">
        <h1>{headerInfo.title}</h1>
        <p>{headerInfo.subtitle}</p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
        <div style={{ display: 'flex', gap: '6px' }}>
          <button 
            className={`btn btn-sm ${user.role === 'ADMIN' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => onSwitchRole('ADMIN')}
          >
            Admin
          </button>
          <button 
            className={`btn btn-sm ${user.role === 'FACULTY' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => onSwitchRole('FACULTY')}
          >
            Faculty
          </button>
          <button 
            className={`btn btn-sm ${user.role === 'STUDENT' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => onSwitchRole('STUDENT')}
          >
            Student
          </button>
        </div>

        <div className="role-badge">
          {user.role === 'ADMIN' && <ShieldCheck size={14} />}
          {user.role === 'FACULTY' && <School size={14} />}
          {user.role === 'STUDENT' && <UserCheck size={14} />}
          <span>{user.role}</span>
        </div>
      </div>
    </header>
  );
}
