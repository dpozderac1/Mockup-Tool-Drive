import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Input, Container, Nav, NavItem, NavLink } from "reactstrap";
import { getAllByAltText } from '@testing-library/react';
import axios from "axios";

class SignIn extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: " ",
            password: " "
        };
    }
    componentDidMount = () => {

    };

    handleChange = event => {
        var id = event.target.id;
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
        axios.post("http://localhost:8080/authenticate", {
            username: this.state.username,
            password: this.state.password
        }).then(res => {
            localStorage.setItem('token', res.data.jwt);
            console.log("Odgovor!")
            console.log(res);
            console.log(res.data);
        })
            .catch((error) => {
                console.log("Status");
                console.log(error.response.status);
                console.log("Greska!");
                console.log(error);
            })
    };

    render() {
        return (
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
                                <NavItem>
                                    <NavLink className="text-secondary" href="#">Forgot password?</NavLink>
                                </NavItem>
                                <NavItem>
                                    <NavLink className="text-secondary" href="#">Not member yet?</NavLink>
                                </NavItem>
                            </Nav>
                        </FormGroup>
                    </Form>
                </Form>
            </Container>

        );
    }
}




export default SignIn;