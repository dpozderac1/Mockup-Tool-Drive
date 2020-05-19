import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert } from 'reactstrap';
import axios from 'axios';
import AdvancedOptions from './advancedOptions';
import UpdateAndDeleteUser from './updateAndDeleteUser';


class SignUp extends Component {
    constructor(props) {
        super();
        this.state = {
            trenutnoLogovaniToken: "",
            firstName: "",
            lastName: "",
            username: "",
            email: "",
            password: "",
            repeatPassword: "",
            greskaVisible: "hidden"
        }
        this.posaljiZahtjev = this.posaljiZahtjev.bind(this);
    }

    posaljiZahtjev(e) {
        e.preventDefault();
        console.log("Usao1");
        console.log(this.state);
        const pass = this.state.password;
        const repeatPass = this.state.repeatPassword;
        const dobarPassword = pass === repeatPass;
        if (dobarPassword) {
            this.setState({
                greskaVisible: "visible"
            })
            const user = {
                name: this.state.firstName,
                surname: this.state.lastName,
                username: this.state.username,
                password: this.state.password,
                email: this.state.email
            }
            axios.post("http://localhost:8080/user/user", { user }).then(res => {
                console.log(res);
                console.log(res.data);
            })
        }
        else {
            console.log("Tu sam");
            this.setState({
                greskaVisible: "visible"
            })
        }
    }

    render() {
        return (
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '52%',
                transform: 'translate(-50%, -50%)'
            }}>
            <h1 className="text-secondary" style={{ textAlign: "left" }}>{(this.props.podaci.state.forma == "signup") ? "Sign Up" : this.props.podaci.state.forma == "admin" ? "Good afternoon": "Good afternoon"}</h1>
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
                            <FormGroup  style={{display : (this.props.podaci.state.forma == "signup") ? "block" : (this.props.podaci.state.forma == "admin") ? "none": "none"}}>
                                <Label for="passwordRepeatLabel">Repeat password</Label>
                                <Input type="password" name="passwordRepeat" id="passwordRepeatLabel" onChange={(e) =>
                                    this.setState({ repeatPassword: e.target.value } )
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs="6"></Col>
                        <Col xs="6">
                            <Alert color="danger" style={{ visibility: this.state.greskaVisible }}>
                                Passwords do not match!
                            </Alert>
                        </Col>
                    </Row>
                    <Row style={{ paddingRight: "15px" }}>
                            <Button type="submit" id="submitButton" className="secondary px-3 bg-dark" style={{ marginLeft: "auto" }}>{(this.props.podaci.state.forma == "signup") ? "Sign Up" : (this.props.podaci.state.forma == "admin") ? "Save changes": "Save changes"}</Button>
                    </Row>
                    <Row style={{display : (this.props.podaci.state.forma == "signup") ? "none" : (this.props.podaci.state.forma == "admin") ? "block": "none"}}>
                        <AdvancedOptions/>
                    </Row>
                    <Row style={{display : (this.props.podaci.state.forma == "signup") ? "none" : (this.props.podaci.state.forma == "admin") ? "block": "block"}}>
                        <UpdateAndDeleteUser/>
                    </Row>
                </Form>
                
            </Container >
        );
    }
}

export default SignUp;