import React, { Component } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Meni from './components/meni';

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
