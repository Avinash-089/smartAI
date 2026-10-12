import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import Navbar from './components/Navbar';
import DashboardView from './components/DashboardView';
import StudentsView from './components/StudentsView';
import FacultyView from './components/FacultyView';
import CoursesView from './components/CoursesView';
import AttendanceView from './components/AttendanceView';
import GradesView from './components/GradesView';
import FeesView from './components/FeesView';
import AiPredictorView from './components/AiPredictorView';
import AiChatModal from './components/AiChatModal';
import { api } from './services/api';

export default function App() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const [user, setUser] = useState({
    id: 1,
    name: 'System Administrator',
    email: 'admin@example.com',
    role: 'ADMIN',
    token: 'token-admin'
  });

  const [stats, setStats] = useState({});
  const [students, setStudents] = useState([]);
  const [faculty, setFaculty] = useState([]);
  const [courses, setCourses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [fees, setFees] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    try {
      const [sData, stData, fData, cData, subData, feeData] = await Promise.all([
        api.getDashboardStats(),
        api.getStudents(),
        api.getFaculty(),
        api.getCourses(),
        api.getSubjects(),
        api.getFees()
      ]);
      setStats(sData || {});
      setStudents(stData || []);
      setFaculty(fData || []);
      setCourses(cData || []);
      setSubjects(subData || []);
      setFees(feeData || []);
    } catch (e) {
      console.error("Data load failed:", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSwitchRole = (newRole) => {
    if (newRole === 'ADMIN') {
      setUser({ id: 1, name: 'System Administrator', email: 'admin@example.com', role: 'ADMIN', token: 'token-admin' });
    } else if (newRole === 'FACULTY') {
      setUser({ id: 2, name: 'Prof. Ada Lovelace', email: 'faculty@example.com', role: 'FACULTY', token: 'token-faculty' });
    } else {
      setUser({ id: 3, name: 'Avinash', email: 'avinash.student@example.com', role: 'STUDENT', token: 'token-student' });
    }
  };

  const handleLogout = () => {
    handleSwitchRole('ADMIN');
    setCurrentTab('dashboard');
  };

  return (
    <div className="app-container">
      <Sidebar 
        currentTab={currentTab} 
        setCurrentTab={setCurrentTab} 
        user={user} 
        onLogout={handleLogout}
      />

      <main className="main-content">
        <Navbar 
          user={user} 
          onSwitchRole={handleSwitchRole} 
          currentTab={currentTab}
        />

        {currentTab === 'dashboard' && (
          <DashboardView stats={stats} onNavigate={setCurrentTab} />
        )}

        {currentTab === 'students' && (
          <StudentsView 
            students={students} 
            setStudents={setStudents} 
            onRefresh={loadData}
          />
        )}

        {currentTab === 'faculty' && (
          <FacultyView faculty={faculty} setFaculty={setFaculty} />
        )}

        {currentTab === 'courses' && (
          <CoursesView courses={courses} subjects={subjects} />
        )}

        {currentTab === 'attendance' && (
          <AttendanceView students={students} />
        )}

        {currentTab === 'grades' && (
          <GradesView students={students} />
        )}

        {currentTab === 'fees' && (
          <FeesView fees={fees} setFees={setFees} students={students} />
        )}

        {currentTab === 'ai-predictor' && (
          <AiPredictorView students={students} />
        )}
      </main>

      <AiChatModal user={user} />
    </div>
  );
}
