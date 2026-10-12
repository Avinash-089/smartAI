import React from 'react';
import { CreditCard, CheckCircle2, Clock, AlertCircle } from 'lucide-react';
import { api } from '../services/api';

export default function FeesView({ fees, setFees, students }) {
  const handlePay = async (id) => {
    await api.payFee(id);
    setFees(fees.map(f => f.id === id ? { ...f, status: 'PAID', paidDate: new Date().toISOString().split('T')[0] } : f));
  };

  const getStudentName = (studentId) => {
    const s = students.find(st => st.id === studentId);
    return s ? s.name : `Student #${studentId}`;
  };

  return (
    <div>
      <div className="table-card">
        <div className="table-header-bar">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Institutional Tuition Invoices & Payment Ledger</h3>
        </div>

        <table className="custom-table">
          <thead>
            <tr>
              <th>Invoice #</th>
              <th>Student Name</th>
              <th>Fee Category</th>
              <th>Amount Due</th>
              <th>Due Date</th>
              <th>Payment Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {fees.map((f) => (
              <tr key={f.id}>
                <td style={{ fontWeight: 600 }}>INV-2026-{1000 + f.id}</td>
                <td style={{ fontWeight: 600 }}>{getStudentName(f.studentId)}</td>
                <td>{f.description}</td>
                <td style={{ fontWeight: 700 }}>₹{(f.amount || 0).toLocaleString()}</td>
                <td>{f.dueDate || '2026-11-15'}</td>
                <td>
                  {f.status === 'PAID' ? (
                    <span className="badge badge-paid" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                      <CheckCircle2 size={13} /> Paid on {f.paidDate || 'Recently'}
                    </span>
                  ) : (
                    <span className="badge badge-pending" style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                      <Clock size={13} /> Pending Payment
                    </span>
                  )}
                </td>
                <td>
                  {f.status !== 'PAID' && (
                    <button 
                      className="btn btn-sm btn-primary"
                      onClick={() => handlePay(f.id)}
                    >
                      Mark Paid
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
