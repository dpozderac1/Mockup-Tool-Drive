import React, { useState, Component } from 'react';
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    NavbarText,
    ButtonGroup,
    Button,
    Row, ListGroup, ListGroupItem, Col, Form, FormGroup
} from 'reactstrap';
import SignIn from './signIn';
import SignUp from './signUp';

class Meni extends Component {
    constructor(props) {
        super();
        this.state = {
            name: "React",
            aktivni: [false, false, false, false],
            forma: "signup"
        };
        this.hideComponent = this.hideComponent.bind(this);
    }

    divStyle = {
        overflowY: 'scroll'
    };

    hideComponent(name) {
        console.log(name);

        switch (name) {
            case "showSignIn":
                this.setState({ aktivni: [true, false, false, false] });
                break;
            case "showSignUp":
                this.setState({ aktivni: [false, true, false, false] });
                break;
            case "showUpdateAdmin":
                this.setState({ aktivni: [false, false, true, false] });
                break;
            case "showUpdateUser":
                this.setState({ aktivni: [false, false, false, true] });
                break;
        }
    }

    setPodaci = (param) => {
        if (param == "showSignUp") {
            this.setState({
                forma: "signup"
            });
        }
        if (param == "showUpdateAdmin") {
            this.setState({
                forma: "admin"
            });
        }
        if (param == "showUpdateUser") {
            this.setState({
                forma: "user"
            });
        }

    };

    render() {
        const { showSignIn, showSignUp, showUpdateAdmin, showUpdateUser } = this.state;

        return (
            <div>
                <Navbar color="light" light expand="md">
                    <NavbarBrand href="/">Mockup tool drive</NavbarBrand>
                    <NavbarToggler />
                    <Nav className="mr-auto" navbar>
                        {localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignIn") }}>Sign in</NavLink> : ""}
                        {localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignUp"); this.setPodaci("showSignUp"); }}>Sign up</NavLink> : ""}
                        {localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showUpdateAdmin"); this.setPodaci("showUpdateAdmin"); }}>Admin profile</NavLink> : ""}
                        {localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showUpdateUser"); this.setPodaci("showUpdateUser"); }}>User profile</NavLink> : ""}
                    </Nav>
                </Navbar>

                <Form>
                    {this.state.aktivni[0] && <SignIn />}
                    {this.state.aktivni[1] && <SignUp podaci={this} />}
                    {(this.state.aktivni[2] || this.state.aktivni[3]) &&
                        <div>
                            <Row>
                            </Row>
                            <Row>
                                <SignUp
                                    podaci={this}
                                />
                            </Row>
                        </div>}
                </Form>
            </div>
        );
    }
}

export default Meni;