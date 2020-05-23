import React, { Component } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Meni from './components/meni';
import axios from 'axios';

axios.interceptors.request.use(function (config) {
  const token = 'Bearer '.concat(localStorage.getItem('token'));
  config.headers.Authorization =  token;

  return config;
});

class App extends Component {
  render() {
    return (
      <div>
        <Meni/>
        {/*<SignIn />*/}
        {/*<AdvancedOptions/>*/}
      </div>
    );
  }
}


export default App;
