import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8080", // Ensure this is correct
  headers: { "ngrok-skip-browser-warning": "true" },
});