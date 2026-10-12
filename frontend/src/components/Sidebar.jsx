import React from 'react';
import { 
  LayoutDashboard, 
  Users, 
  GraduationCap, 
  BookOpen, 
  CalendarCheck, 
  Award, 
  CreditCard, 
  BrainCircuit, 
  MessageSquareCode,
  LogOut
} from 'lucide-react';

export default function Sidebar({ currentTab, setCurrentTab, user, onLogout }) {
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'students', label: 'Students', icon: Users },
    { id: 'faculty', label: 'Faculty', icon: GraduationCap },
    { id: 'courses', label: 'Courses & Subjects', icon: BookOpen },
    { id: 'attendance', label: 'Attendance', icon: CalendarCheck },
    { id: 'grades', label: 'Grades & GPA', icon: Award },
    { id: 'fees', label: 'Fees & Invoicing', icon: CreditCard },
    { id: 'ai-predictor', label: 'AI Risk Predictor', icon: BrainCircuit, badge: 'AI' },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <div style={{ background: '#4f46e5', padding: '8px', borderRadius: '10px', display: 'flex' }}>
          <BrainCircuit size={22} color="#fff" />
        </div>
        <div>
          <h2>Smart AI SMS</h2>
          <span style={{ fontSize: '0.72rem', color: '#94a3b8' }}>Academic Enterprise</span>
        </div>
      </div>

      <nav className="sidebar-nav">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = currentTab === item.id;
          return (
            <div
              key={item.id}
              className={`nav-item ${isActive ? 'active' : ''}`}
              onClick={() => setCurrentTab(item.id)}
            >
              <Icon size={18} />
              <span style={{ flex: 1 }}>{item.label}</span>
              {item.badge && (
                <span style={{ 
                  background: 'rgba(255,255,255,0.2)', 
                  padding: '2px 6px', 
                  borderRadius: '6px', 
                  fontSize: '0.7rem',
                  fontWeight: 700 
                }}>
                  {item.badge}
                </span>
              )}
            </div>
          );
        })}
      </nav>

      <div className="sidebar-footer">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div>
            <div style={{ fontSize: '0.85rem', fontWeight: 600, color: '#f8fafc' }}>{user.name}</div>
            <div style={{ fontSize: '0.75rem', color: '#94a3b8' }}>{user.role}</div>
          </div>
          <button 
            onClick={onLogout} 
            title="Logout" 
            style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }}
          >
            <LogOut size={18} />
          </button>
        </div>
      </div>
    </aside>
  );
}
