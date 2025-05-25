import React, { useState, useEffect } from "react";
import { db } from "../config/firebase";
import { ref, push, onValue } from "firebase/database";

const Chat = ({ room = "default-room", user }) => {
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");

  useEffect(() => {
    const chatRef = ref(db, "chats/" + room);
    onValue(chatRef, (snapshot) => {
      const data = snapshot.val();
      setMessages(data ? Object.values(data) : []);
    });
  }, [room]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!newMsg.trim()) return;
    const chatRef = ref(db, "chats/" + room);
    await push(chatRef, {
      user: user || "Ẩn danh",
      text: newMsg,
      time: new Date().toLocaleTimeString()
    });
    setNewMsg("");
  };

  return (
    <div style={{ border: "1px solid #ccc", borderRadius: 8, padding: 16, width: 350 }}>
      <h5>Chat real-time</h5>
      <div style={{ height: 250, overflowY: "auto", background: "#f9f9f9", marginBottom: 8, padding: 8 }}>
        {messages.map((msg, i) => (
          <div key={i} style={{ margin: "8px 0" }}>
            <strong>{msg.user}:</strong> {msg.text} <span style={{ fontSize: 10, color: "#888" }}>{msg.time}</span>
          </div>
        ))}
      </div>
      <form onSubmit={sendMessage} className="d-flex">
        <input
          type="text"
          className="form-control"
          value={newMsg}
          onChange={e => setNewMsg(e.target.value)}
          placeholder="Nhập tin nhắn..."
        />
        <button className="btn btn-primary ms-2" type="submit">Gửi</button>
      </form>
    </div>
  );
};

export default Chat;