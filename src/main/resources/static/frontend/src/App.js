import "./App.css";
import api from "./api/axiosConfig";
import { useState, useEffect } from "react";
import Layout from "./components/Layout";
import { Routes, Route } from "react-router-dom";
import Home from "./components/Home/Home";

function App() {
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const getPosts = async () => {
    try {
      const response = await api.get("/api/posts");
      setPosts(response.data);
    } catch (err) {
      setError(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getPosts();
  }, []);

  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/" element={<Home posts={posts} isLoading={isLoading} error={error} />}></Route>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
