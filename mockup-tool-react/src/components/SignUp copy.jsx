import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert } from 'reactstrap';
import axios from 'axios';
import jQuery from 'jquery';


class SignUp extends React.Component {
    constructor() {
        super();
        this.state = {
            trenutnoLogovaniToken: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkcG96ZGVyYWMxIiwiZXhwIjoxNTg5OTMxNDE1LCJpYXQiOjE1ODk4OTU0MTV9.itcv_BVOEgXuBOb9NTgHzPzKStKpVB9dXVfRBrud6LY",
            firstName: "",
            lastName: "",
            username: "",
            email: "",
            password: "",
            repeatPassword: "",
            greskaVisible: "hidden",
            nepodudaranPassword: false
        }
        this.posaljiZahtjev = this.posaljiZahtjev.bind(this);
    }

    posaljiZahtjev(e) {
        e.preventDefault();
        const pass = this.state.password;
        const repeatPass = this.state.repeatPassword;
        const dobarPassword = pass === repeatPass;
        if (dobarPassword) {
            this.setState({
                greskaVisible: "hidden",
                nepodudaranPassword: false
            })
            const user = {
                name: this.state.firstName,
                surname: this.state.lastName,
                username: this.state.username,
                password: this.state.password,
                email: this.state.email
            }
            if (user.name === "" || user.surname === "" || user.username === "" || user.password === "" || user.email === "") {
                this.setState({
                    greskaVisible: "visible"
                })
            }
            else {
                console.log("Zahtjev je: ");
                console.log(user);
                const AuthStr = 'Bearer '.concat(this.state.trenutnoLogovaniToken);

                const novi = {
                    username: "dpozderac1",
                    password: "Password1!"
                }
                axios.post("http://localhost:8080/authenticate", {
                    username: "dpozderac1",
                    password: "Password1!"
                }).then(res => {
                    localStorage.setItem('token', res.data.jwt);
                    console.log(res);
                    console.log(res.data);
                })
                    /* {
                        name: this.state.firstName,
                        surname: this.state.lastName,
                        username: this.state.username,
                        password: this.state.password,
                        email: this.state.email
                    }*/
                    /*axios.post("http://localhost:8080/user/user", {
                        name: this.state.firstName,
                        surname: this.state.lastName,
                        username: this.state.username,
                        password: this.state.password,
                        email: this.state.email
                    }, { headers: { 'Authorization': AuthStr } }).then(res => {
                        console.log(res);
                        console.log(res.data);
                    })*/
                    /*axios.get("http://localhost:8080/user/users/1", { headers: { Authorization: AuthStr } }).then(res => {
                        console.log(res);
                        console.log(res.data);
                    })*/
                    .catch((error) => {
                        console.log(error);
                    })
            }
        }
        else {
            console.log("Tu sam");
            this.setState({
                greskaVisible: "visible",
                nepodudaranPassword: true
            })
        }
    }

    render() {
        return (
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>Sign Up</h1>
                <hr className="my-2" />
                <Form onSubmit={this.posaljiZahtjev}>
                    <Row>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="firstNameLabel">First name</Label>
                                <Input type="text" name="firstName" id="firstNameLabel" onChange={(e) =>
                                    this.setState({ firstName: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="lastNameLabel">Last name</Label>
                                <Input type="text" name="lastName" id="lastNameLabel" onChange={(e) =>
                                    this.setState({ lastName: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="usernameLabel">Username</Label>
                                <Input type="text" name="username" id="usernameLabel" onChange={(e) =>
                                    this.setState({ username: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="emailLabel">Email</Label>
                                <Input type="email" name="emailName" id="emailLabel" onChange={(e) =>
                                    this.setState({ email: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="passwordLabel">Password</Label>
                                <Input type="password" name="password" id="passwordLabel" onChange={(e) =>
                                    this.setState({ password: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="passwordRepeatLabel">Repeat password</Label>
                                <Input type="password" name="passwordRepeat" id="passwordRepeatLabel" onChange={(e) =>
                                    this.setState({ repeatPassword: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6"></Col>
                        <Col xs="6">
                            <Alert color="danger" style={{ visibility: this.state.greskaVisible }}>
                                {(this.state.nepodudaranPassword && this.state.greskaVisible === "visible") ? "Passwords do not match!" : "Fields cannot be empty!"}
                            </Alert>
                        </Col>
                    </Row>
                    <Row style={{ paddingRight: "15px" }}>
                        <Button type="submit" id="submitButton" className="px-3 bg-dark" style={{ marginLeft: "auto" }}>Sign Up</Button>
                    </Row>
                </Form>
            </Container >
        );
    }
}

export default SignUp;