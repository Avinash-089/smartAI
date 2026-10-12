import React, { useState } from 'react';
import { GraduationCap, Plus, Mail, Phone, Building } from 'lucide-react';
import { api } from '../services/api';

export default function FacultyView({ faculty, setFaculty }) {
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    department: 'Computer Science',
    designation: 'Assistant Professor',
    phone: ''
  });

  const handleAdd = async (e) => {
    e.preventDefault();
    const created = await api.createFaculty(formData);
    setFaculty([...faculty, created]);
    setShowModal(false);
    setFormData({
      name: '',
      email: '',
      department: 'Computer Science',
      designation: 'Assistant Professor',
      phone: ''
    });
  };

  return (
    <div>
      <div className="table-card">
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Faculty Members & Department Allocations</h3>
          <button className="btn btn-primary" onClick={() => setShowModal(true)}>
            <Plus size={16} /> Add Faculty Member
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px', padding: '24px' }}>
          {faculty.map((f) => (
            <div key={f.id} style={{ 
              background: '#ffffff', 
              border: '1px solid var(--border-color)', 
              borderRadius: '12px', 
              padding: '20px',
              boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '14px' }}>
                <div style={{ 
                  width: '44px', 
                  height: '44px', 
                  borderRadius: '50%', 
                  background: '#e0e7ff', 
                  color: '#4338ca', 
                  display: 'flex', 
                  alignItems: 'center', 
                  justifyContent: 'center',
                  fontWeight: 700,
                  fontSize: '1.1rem'
                }}>
                  {f.name.charAt(0)}
                </div>
                <div>
                  <h4 style={{ fontSize: '1rem', fontWeight: 700 }}>{f.name}</h4>
                  <span style={{ fontSize: '0.8rem', color: '#4f46e5', fontWeight: 600 }}>{f.designation}</span>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '0.86rem', color: 'var(--text-secondary)' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <Building size={15} color="#94a3b8" />
                  <span>Department of {f.department}</span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <Mail size={15} color="#94a3b8" />
                  <span>{f.email}</span>
                </div>
                {f.phone && (
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <Phone size={15} color="#94a3b8" />
                    <span>{f.phone}</span>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '16px' }}>Add Faculty Member</h3>
            <form onSubmit={handleAdd}>
              <div className="form-group">
                <label>Faculty Name</label>
                <input 
                  type="text" 
                  required 
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Dr. John Doe"
                />
              </div>
              <div className="form-group">
                <label>Official Email</label>
                <input 
                  type="email" 
                  required 
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  placeholder="e.g. john.doe@example.com"
                />
              </div>
              <div className="form-group">
                <label>Department</label>
                <select 
                  value={formData.department}
                  onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                >
                  <option value="Computer Science">Computer Science</option>
                  <option value="Artificial Intelligence">Artificial Intelligence</option>
                  <option value="Information Technology">Information Technology</option>
                </select>
              </div>
              <div className="form-group">
                <label>Designation</label>
                <input 
                  type="text" 
                  value={formData.designation}
                  onChange={(e) => setFormData({ ...formData, designation: e.target.value })}
                  placeholder="e.g. Professor & HOD"
                />
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <input 
                  type="text" 
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  placeholder="e.g. 9876500004"
                />
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '20px' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  Save Faculty
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
