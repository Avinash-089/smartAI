import React, { useState } from 'react';
import { MessageSquareCode, X, Send, Bot, Sparkles } from 'lucide-react';
import { api } from '../services/api';

export default function AiChatModal({ user }) {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    {
      sender: 'bot',
      text: `Hello ${user.name}! I am your Smart AI Academic Assistant. How can I help you with your attendance, fee dues, exam dates, or performance today?`
    }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const suggestions = [
    "What is the minimum attendance requirement?",
    "How can I check my pending fees?",
    "When do semester exams begin?",
    "How does the AI flag at-risk students?"
  ];

  const handleSend = async (textToSend) => {
    const text = textToSend || input;
    if (!text.trim()) return;

    const userMsg = { sender: 'user', text };
    setMessages(prev => [...prev, userMsg]);
    if (!textToSend) setInput('');
    setLoading(true);

    try {
      const res = await api.chat(text, user.role, user.email);
      setMessages(prev => [...prev, { sender: 'bot', text: res.reply }]);
    } catch (err) {
      setMessages(prev => [...prev, { sender: 'bot', text: "I'm having trouble processing that right now. Please try again." }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* Floating Trigger Button */}
      <div 
        className="ai-chat-bubble"
        onClick={() => setIsOpen(!isOpen)}
        title="Chat with AI Academic Assistant"
      >
        <MessageSquareCode size={26} />
      </div>

      {/* Floating Chat Window */}
      {isOpen && (
        <div className="ai-chat-modal">
          <div className="chat-header">
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <div style={{ background: 'rgba(255,255,255,0.2)', padding: '6px', borderRadius: '8px' }}>
                <Bot size={18} />
              </div>
              <div>
                <h4 style={{ fontSize: '0.95rem', fontWeight: 700 }}>AI Academic Assistant</h4>
                <span style={{ fontSize: '0.72rem', color: '#c7d2fe' }}>Online • NLP Query Support</span>
              </div>
            </div>
            <button 
              onClick={() => setIsOpen(false)} 
              style={{ background: 'transparent', border: 'none', color: '#fff', cursor: 'pointer' }}
            >
              <X size={18} />
            </button>
          </div>

          <div className="chat-messages">
            {messages.map((m, i) => (
              <div key={i} className={`chat-msg ${m.sender}`}>
                {m.text}
              </div>
            ))}
            {loading && (
              <div className="chat-msg bot" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                <Sparkles size={14} /> Generating answer...
              </div>
            )}
          </div>

          <div className="chip-group">
            {suggestions.map((s, idx) => (
              <span key={idx} className="chip" onClick={() => handleSend(s)}>
                {s}
              </span>
            ))}
          </div>

          <div className="chat-input-area">
            <input 
              type="text" 
              placeholder="Ask anything about campus, exams, fees..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSend()}
              style={{ flex: 1, padding: '8px 12px', border: '1px solid var(--border-color)', borderRadius: '8px', fontSize: '0.85rem', outline: 'none' }}
            />
            <button 
              className="btn btn-primary" 
              style={{ padding: '8px 12px' }}
              onClick={() => handleSend()}
              disabled={loading}
            >
              <Send size={15} />
            </button>
          </div>
        </div>
      )}
    </>
  );
}
