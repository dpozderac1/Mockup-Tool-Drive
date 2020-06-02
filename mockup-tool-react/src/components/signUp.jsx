import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, Col, Container, Row, Alert } from 'reactstrap';
import axios from 'axios';
import AdvancedOptions from './advancedOptions';
import UpdateAndDeleteUser from './updateAndDeleteUser';
import { UrlContext } from '../urlContext';


class SignUp extends React.Component {
    constructor(props) {
        super();
        this.state = {
            firstName: "",
            lastName: "",
            username: "",
            email: "",
            password: "",
            repeatPassword: "",
            greskaVisible: "hidden",
            nepodudaranPassword: "hidden",
            errorZahtjev: "",
            uspjesanZahtjev: false,
            firstChange : true,
            previousUsername: ""
        }
        this.posaljiZahtjev = this.posaljiZahtjev.bind(this);
        this.dobaviPodatkeKorisnika = this.dobaviPodatkeKorisnika.bind(this);
        this.uradiPut = this.uradiPut.bind(this);
    }

    componentDidMount() {
        console.log("Mount!");
        console.log(this.props.podaci.state.forma);
        if (this.props.podaci.state.forma === "admin" || this.props.podaci.state.forma === "user") {
            console.log("Usao");
            this.dobaviPodatkeKorisnika();
        }
    }

    dobaviPodatkeKorisnika() {
        const idKorisnika = "1";
        const AuthStr = 'Bearer '.concat(localStorage.getItem('token'));
        console.log("token", AuthStr);
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            axios.get(url.user + "/users/" + res.data.id/*, { headers: { Authorization: AuthStr } }*/).then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({
                    firstName: res.data.name,
                    lastName: res.data.surname,
                    username: res.data.username,
                    email: res.data.email,
                    password: res.data.password
                });
                document.getElementById("firstNameLabel").value = this.state.firstName;
                document.getElementById("lastNameLabel").value = this.state.lastName;
                document.getElementById("usernameLabel").value = this.state.username;
                document.getElementById("emailLabel").value = this.state.email;
            })
                .catch((error) => {
                    console.log("Greska u GET!");
                    console.log(error);
                });
        });
    }

    posaljiZahtjev(e) {
        e.preventDefault();
        let url = this.context;
        const pass = this.state.password;
        const repeatPass = this.state.repeatPassword;
        const dobarPassword = pass === repeatPass;
        if (dobarPassword || this.props.podaci.state.forma !== "signup") {
            this.setState({
                greskaVisible: "hidden",
                nepodudaranPassword: "hidden",
                errorZahtjev: ""
            })
            const user = {
                name: this.state.firstName,
                surname: this.state.lastName,
                username: this.state.username,
                password: this.state.password,
                email: this.state.email
            }
            if (user.name === "" || user.surname === "" || user.username === "" || (this.props.podaci.state.forma === "signup" && user.password === "") || user.email === "") {
                this.setState({
                    greskaVisible: "visible"
                })
            }
            else {
                console.log("Zahtjev je: ");
                console.log(user);
                axios.post(url.user + "/user", {
                    roleID: {
                        id: 2,
                        role_name: "USER"
                    },
                    name: this.state.firstName,
                    surname: this.state.lastName,
                    username: this.state.username,
                    password: this.state.password,
                    email: this.state.email
                }).then(res => {
                    console.log("Odgovor!");
                    console.log(res);
                    console.log(res.data);
                    this.props.podaci.hideComponent("showSignIn");
                })
                    .catch((error) => {
                        console.log("Greska!");
                        console.log(error.response.status);
                        if (error.response.status === 403) {
                            this.setState({
                                errorZahtjev: "You are not authorized!"
                            });
                        }
                        else {
                            console.log(error.response.data.errors[0]);
                            this.setState({
                                errorZahtjev: error.response.data.errors
                            });
                        }
                    })

            }
        }
        else {
            console.log("Tu sam");
            this.setState({
                greskaVisible: "hidden",
                nepodudaranPassword: "visible",
                errorZahtjev: ""
            })
        }
    }

    uradiPut(e) {
        e.preventDefault();
        const idKorisnika = "1";
        let url = this.context;
        const AuthStr = 'Bearer '.concat(localStorage.getItem('token'));
        let readPass = document.getElementById("passwordLabel").value;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            console.log("idddd: ", res.data.id, AuthStr);
            axios.put(url.user + "/updateUser/" + res.data.id, {
                name: this.state.firstName,
                surname: this.state.lastName,
                username: this.state.username,
                password: this.state.password,
                email: this.state.email
            }).then(res => {
                console.log("Odgovor!");
                console.log(res);
                console.log(res.data);
                this.setState({
                    errorZahtjev: "",
                    uspjesanZahtjev: true,
                    firstChange: true
                });
                if(this.state.previousUsername !== "" && (this.state.username !== this.state.previousUsername)) {
                    localStorage.setItem('token', "");
                    setTimeout(() => this.props.podaci.hideComponent("showSignIn"), 2000); 
                }
                if(readPass !== "") {
                    localStorage.setItem('token', "");
                    setTimeout(() => this.props.podaci.hideComponent("showSignIn"), 2000); 
                }
            })
                .catch((error) => {
                    console.log("Greska!");
                    console.log(error.response);
                    let err = "";
                    if(error.response == undefined) {
                        err = "Unknown error!";
                        this.setState({
                            errorZahtjev: err,
                            uspjesanZahtjev: false
                        });
                    }
                    else {
                        if (error.response.status === 403) {
                            this.setState({
                                errorZahtjev: "You are not authorized!",
                                uspjesanZahtjev: false
                            });
                        }
                        else {
                            this.setState({
                                errorZahtjev: error.response.data.errors,
                                uspjesanZahtjev: false
                            });
                        }
                    }
                })
        });
    }

    render() {
        return (
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '52%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>{(this.props.podaci.state.forma === "signup") ? "Sign Up" : this.props.podaci.state.forma === "admin" ? "Good afternoon, ".concat(this.state.username) : "Good afternoon, ".concat(this.state.username)}</h1>
                <hr className="my-2" />
                <Form onSubmit={this.props.podaci.state.forma === "signup" ? this.posaljiZahtjev : this.uradiPut}>
                    <Row>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="firstNameLabel">First name</Label>
                                <Input type="text" name="firstName" id="firstNameLabel" minLength={3} maxLength={255}
                                    onChange={(e) =>
                                        this.setState({ firstName: e.target.value })
                                    } />
                            </FormGroup>
                        </Col>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="lastNameLabel">Last name</Label>
                                <Input type="text" name="lastName" id="lastNameLabel" minLength={3} maxLength={255}
                                    onChange={(e) =>
                                        this.setState({ lastName: e.target.value })
                                    } />
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col xs="6">
                            <FormGroup>
                                <Label for="usernameLabel">Username</Label>
                                <Input type="text" name="username" id="usernameLabel" minLength={5} maxLength={50}
                                    onChange={(e) => {
                                        if(this.state.firstChange === true) {
                                            this.setState({previousUsername: this.state.username, firstChange: false}); 
                                        }
                                        console.log("username: ", this.state.previousUsername, e.target.value);
                                        this.setState({ username: e.target.value });
                                    }} />
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
                                <Input type="password" name="password" id="passwordLabel" minLength={this.props.podaci.state.forma === "signup" ? 8 : 0}
                                    onChange={(e) =>
                                        this.setState({ password: e.target.value })
                                    } />
                            </FormGroup>
                        </Col>
                        <Col xs="6">
                            <FormGroup style={{ display: (this.props.podaci.state.forma === "signup") ? "block" : (this.props.podaci.state.forma === "admin") ? "none" : "none" }}>
                                <Label for="passwordRepeatLabel" minLength={8}>Repeat password</Label>
                                <Input type="password" name="passwordRepeat" id="passwordRepeatLabel" onChange={(e) =>
                                    this.setState({ repeatPassword: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6"></Col>
                        <Col xs="6">
                            {this.state.nepodudaranPassword === "visible" ? <Alert color="danger" style={{ visibility: this.state.nepodudaranPassword }}>
                                Passwords do not match!
                            </Alert> : this.state.greskaVisible === "visible" ? <Alert color="danger" style={{ visibility: this.state.greskaVisible }}>
                                    Fields cannot be empty!
                            </Alert> : this.state.errorZahtjev !== "" ? <Alert color="danger" style={{ visibility: "visible" }}>
                                        {this.state.errorZahtjev}
                                    </Alert> : ""}
                            {this.state.uspjesanZahtjev ? <Alert color="success" style={{ visibility: "visible" }}>
                                Changes are successfully saved!
                                    </Alert> : ""}
                        </Col>
                    </Row>
                    <Row style={{ paddingRight: "15px" }}>
                        <Button type="submit" id="submitButton" className="secondary px-3 bg-dark" style={{ marginLeft: "auto" }}>{(this.props.podaci.state.forma === "signup") ? "Sign Up" : (this.props.podaci.state.forma === "admin") ? "Save changes" : "Save changes"}</Button>
                    </Row>
                    <Row style={{ display: (this.props.podaci.state.forma === "signup") ? "none" : (this.props.podaci.state.forma === "admin") ? "block" : "none" }}>
                        <AdvancedOptions handler={this.props.podaci.hideAll} />
                    </Row>
                    <Row style={{ display: (this.props.podaci.state.forma === "signup") ? "none" : (this.props.podaci.state.forma === "admin") ? "block" : "block" }}>
                        <UpdateAndDeleteUser handler={this.props.podaci.hideComponent} />
                    </Row>
                </Form>

            </Container >
        );
    }
}
SignUp.contextType = UrlContext;

export default SignUp;