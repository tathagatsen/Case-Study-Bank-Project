import React, { useState } from "react";
import axios from "axios";
import { X } from "lucide-react";

export default function Chatbot({ onClose }) {
  const [messages, setMessages] = useState([
    { sender: "bot", text: "Hi! 👋 How can I help you today?" },
  ]);
  const [input, setInput] = useState("");

  const sendMessage = async () => {
    if (!input.trim()) return;

    // Add user message
    setMessages((prev) => [...prev, { sender: "user", text: input }]);
    const userMessage = input;
    setInput("");

    try {
     const res = await axios.post("http://localhost:8082/customer/ask-chatbot", {
  question: userMessage,
});


      console.log("Backend response:", res.data);

      // Extract the actual chatbot text safely
      const botText =
        res.data?.candidates?.[0]?.content?.parts?.[0]?.text ||
        "⚠️ No response from chatbot";

      setMessages((prev) => [...prev, { sender: "bot", text: botText }]);
    } catch (err) {
      console.error("Error:", err);
      setMessages((prev) => [
        ...prev,
        { sender: "bot", text: "⚠️ Error connecting to chatbot." },
      ]);
    }
  };

  return (
    <div className="fixed bottom-4 right-4 w-80 h-96 bg-white shadow-xl rounded-2xl flex flex-col border border-gray-200">
      {/* Header */}
      <div className="flex justify-between items-center bg-blue-600 text-white p-3 rounded-t-2xl">
        <h2 className="font-semibold">Chatbot</h2>
        <button onClick={onClose}>
          <X size={20} />
        </button>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-3 space-y-2 text-sm">
        {messages.map((msg, i) => (
          <div
            key={i}
            className={`p-2 rounded-lg max-w-[80%] ${
              msg.sender === "user"
                ? "bg-blue-500 text-white self-end ml-auto"
                : "bg-gray-200 text-black self-start mr-auto"
            }`}
          >
            {msg.text}
          </div>
        ))}
      </div>

      {/* Input */}
      <div className="p-3 border-t flex space-x-2">
        <input
          type="text"
          className="flex-1 border rounded-lg px-3 py-2 text-sm focus:outline-none"
          placeholder="Type your message..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button
          className="bg-blue-600 text-white px-3 rounded-lg"
          onClick={sendMessage}
        >
          Send
        </button>
      </div>
    </div>
  );
}
