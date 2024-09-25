import React from "react";
import PropTypes from "prop-types";

function Home({ posts, isLoading, error }) {
  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error loading posts: {error.message}</div>;
  }

  return (
    <div>
      {posts?.map((post) => (
        <div key={post.id} className="post">
          <h2>{post.title}</h2>
          <p>Written by {post.account.firstName}</p>
          <p>{post.body}</p>
        </div>
      ))}
    </div>
  );
}

Home.propTypes = {
  posts: PropTypes.array.isRequired,
  isLoading: PropTypes.bool.isRequired,
  error: PropTypes.object,
};

export default Home;