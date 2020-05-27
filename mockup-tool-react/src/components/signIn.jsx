import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Input, Container, Nav, NavItem, NavLink, Alert } from "reactstrap";
import { getAllByAltText } from '@testing-library/react';
import axios from "axios";
import SignUp from './signUp';
import {UrlContext} from '../urlContext';

class SignIn extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: " ",
            password: " ",
            goodParameters: "hidden", 
            userRole: "", 
            forma: "signin",
            aktivni: [true, false]
        };
    }
    componentDidMount = () => {

    };

    handleChange = event => {
        var id = event.target.id;
        this.setState({
            goodParameters: "hidden"
        });
        switch (id) {
            case "username": {
                this.setState({ username: document.getElementById("username").value });
                break;
            }
            case "password": {
                this.setState({ password: document.getElementById("password").value });
                break;
            }
        }
    };

    handleClick = () => {
        console.log(this.state.username);
        console.log(this.state.password);
        let url = this.context;
        axios.post(url.gateway + "/authenticate", {
            username: this.state.username,
            password: this.state.password
        }).then(res => {
            localStorage.setItem('token', res.data.jwt);
            console.log("Odgovor!")
            console.log(res);
            axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(userData => {
                this.setState({
                    userRole: userData.data.roleID.role_name,
                    //aktivni: [false, true]
                });
                if(userData.data.roleID.role_name == "ADMIN"){
                    this.setState({
                        forma: "admin"
                    });
                }
                else if(userData.data.roleID.role_name){
                    this.setState({
                        forma: "user"
                    });
                }
                
                console.log(userData.data.roleID.role_name);
            });
            console.log(res.data);
            this.setState({
                goodParameters: "hidden"
            });
        })
            .catch((error) => {
                console.log("Status");
                console.log(error.response.status);
                console.log("Greska!");
                console.log(error);
                this.setState({
                    goodParameters: "visible"
                });
            });
    };

    render() {
        return (
        <Form>
            {this.state.aktivni[0] && 
            <Container className="col-log-6" style={
                {
                    position: 'absolute', left: '50%', top: '50%',
                    transform: 'translate(-50%, -50%)'
                }
            }>
                <Form className="row align-items-center ">
                    <Form className="col-md-5 my-auto">
                        <h1 class="row justify-content-center text-secondary">Sign In </h1>
                        <hr className="my-2"></hr>
                        <FormGroup />
                        <FormGroup>
                            <Input type="username" name="username" id="username" placeholder="username"
                                onChange={this.handleChange} />
                        </FormGroup>
                        <FormGroup>
                            <Input type="password" name="password" id="password" placeholder="password"
                                onChange={this.handleChange} />
                        </FormGroup >
                        <FormGroup>
                        <Alert color="danger" style={{ visibility: this.state.goodParameters}}>
                            Username or password are invalid!
                        </Alert>
                        </FormGroup>
                        <b class="row justify-content-center">
                            <Button
                                className="bg-dark"
                                onClick={this.handleClick}
                                style={
                                    {
                                        width: '30%'
                                    }
                                }
                            >
                                Sign in
                                </Button>
                        </b>
                        <FormGroup className="row justify-content-center">
                            <Nav>
                                {/*<NavItem>
                                    <NavLink className="text-secondary" href="#">Forgot password?</NavLink>
                                </NavItem>*/}
                                <NavItem>
                                    <NavLink style = {{float: "right"}} className="text-secondary" href="#">Not member yet?</NavLink>
                                </NavItem>
                            </Nav>
                        </FormGroup>
                    </Form>
                </Form>
            </Container>
            }
            {
                this.state.aktivni[1] && <SignUp podaci = {this}/>
            }
        </Form>
        );
    }
}
SignIn.contextType = UrlContext;



export default SignIn;