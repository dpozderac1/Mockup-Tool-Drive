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
import Collaboration from './collaboration';

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

    componentDidMount() {
        this.sakrijMockupTool();
    }

    divStyle = {
        overflowY: 'scroll'
    };

    hideComponent(name) {
        console.log(name);

        switch (name) {
            case "showSignIn":
                this.setState({ aktivni: [true, false, false, false, false, false] });
                break;
            case "showSignUp":
                this.setState({ aktivni: [false, false, true, false, false, false] });
                break;
            case "showUpdateAdmin":
                this.setState({ aktivni: [false, false, false, true, false, false] });
                break;
            case "showUpdateUser":
                this.setState({ aktivni: [false, false, false, true, false, false] });
                break;
            case "showMockupTool":
                this.setState({ aktivni: [false, false, false, false, true, false] });
                break;
            case "showCollaboration":
                this.setState({ aktivni: [false, false, false, false, false, true] });
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

    prikaziMockupTool() {
        document.getElementById('zaglavlje').style.display = "flex";
        document.getElementById('lijevo').style.display = "flex";
        document.getElementById('glavni').style.display = "flex";
        document.getElementById('strelicaToolbara').style.display = "flex";
        document.getElementById('desniToolbar').style.display = "flex";
    }

    sakrijMockupTool() {
        document.getElementById('zaglavlje').style.display = "none";
        document.getElementById('lijevo').style.display = "none";
        document.getElementById('glavni').style.display = "none";
        document.getElementById('strelicaToolbara').style.display = "none";
        document.getElementById('desniToolbar').style.display = "none";
    }

    render() {
        const { showSignIn, showSignUp, showUpdateAdmin, showUpdateUser } = this.state;

        return (
            <div>
                <Navbar color="light" light expand="md">
                    <NavbarBrand href="/">Mockup tool drive</NavbarBrand>
                    <NavbarToggler />
                    <Nav className="mr-auto" navbar>
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignIn") }}>Sign in</NavLink> : ""}
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignUp"); this.setPodaci("showSignUp"); }}>Sign up</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showUpdateAdmin"); this.setPodaci("showUpdateAdmin"); }}>Admin profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showUpdateUser"); this.setPodaci("showUpdateUser"); }}>User profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showMockupTool"); }}>Mockup Tool</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showCollaboration"); }}>Collaborate</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}>Sign out</NavLink> : ""}
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
                    {this.state.aktivni[4] ? this.prikaziMockupTool() : this.sakrijMockupTool()}
                    {this.state.aktivni[5] && <Collaboration />}
                </Form>
            </div>
        );
    }
}

export default Meni;