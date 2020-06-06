import React, { Component } from 'react';

export const urls = {
    url : {
        user: "http://localhost:8080/user",
        onlineTesting: "http://localhost:8080/online-testing",
        project: "http://localhost:8080/project-client-service",
        gateway: "http://localhost:8080"
    }
  };
  
  export const UrlContext = React.createContext(
    urls.url
  );