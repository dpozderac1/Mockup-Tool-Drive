import React, { Component } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Meni from './components/meni';
import axios from 'axios';
import './components/css/diplomski.css';

axios.interceptors.request.use(function (config) {
  let token = "";
  if(localStorage.getItem('token') !== ""){
    token = 'Bearer '.concat(localStorage.getItem('token'));
  }
  config.headers.Authorization = token;

  return config;
});

class App extends Component {
  render() {
    return (
      <div>
        <Meni />
        {/*<SignIn />*/}
        {/*<AdvancedOptions/>*/}
      </div>
    );
  }
}


export default App;
