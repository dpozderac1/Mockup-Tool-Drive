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
import CreateNewServer from './createNewServer';
import axios from 'axios';
import {UrlContext} from '../urlContext';
import CreateNewVersion from './createNewVersion';
import ProjectOverview from './projectOverview';

class Meni extends Component {
    constructor(props) {
        super();
        this.state = {
            name: "React",
            aktivni: [false, false, false, false, false, false, false],
            forma: "signup",
            serverOrBrowser: "",
            servers: [],
            firstServer: "", 
            role: ""
        };
        this.hideComponent = this.hideComponent.bind(this);
        this.hideAll = this.hideAll.bind(this);
        this.setRole = this.setRole.bind(this);
    }

    componentDidMount() {
        console.log("desilo se");
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            this.setState({role: res.data.roleID.role_name});
        });
        this.sakrijMockupTool();
    }

    divStyle = {
        overflowY: 'scroll'
    };

    hideComponent(name) {
        console.log(name);

        switch (name) {
            case "showSignIn":
                this.setState({ aktivni: [true, false, false, false, false, false, false] });
                break;
            case "showSignUp":
                this.setState({ aktivni: [false, false, true, false, false, false, false] });
                break;
            case "showUpdateAdmin":
                this.setState({ aktivni: [false, false, false, true, false, false, false] });
                break;
            case "showUpdateUser":
                this.setState({ aktivni: [false, false, false, true, false, false, false] });
                break;
            case "showMockupTool":
                this.setState({ aktivni: [false, false, false, false, true, false, false] });
                break;
            case "showCollaboration":
                this.setState({ aktivni: [false, false, false, false, false, true, false] });
                break;
        }
    }

    hideAll(name) {
        console.log("meni naziv: ", name);
        if(name == "browser"){
            let url = this.context;
            console.log("contekst: ", url);
            axios.get(url.onlineTesting + "/servers").then(res => {
                let servers = [];
                res.data.map(server => 
                    {
                        let s = {"id": server.id, "name": server.url};
                        servers.push(s);
                    }
                );
                console.log("serveriii: ", servers);
                this.setState({ aktivni: [false, false, false, false, false, false, true], serverOrBrowser: name, servers, firstServer: servers[0].name});
            });
        }   
        else {
            this.setState({ aktivni: [false, false, false, false, false, false, true], serverOrBrowser: name});
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

    setRole (role) {
        this.setState({role});
        console.log("meni role: ", this.state.role);
    }

    render() {
        const { showSignIn, showSignUp, showUpdateAdmin, showUpdateUser } = this.state;

        return (
            <div>
                <Row xs={12}>
                    <Col>
                    <Navbar color="light" light expand="md">
                        <NavbarBrand href="/">Mockup tool drive</NavbarBrand>
                        <NavbarToggler />
                        <Nav className="mr-auto" navbar>
                            {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignIn") }}>Sign in</NavLink> : ""}
                            {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignUp"); this.setPodaci("showSignUp"); }}>Sign up</NavLink> : ""}
                            {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "ADMIN" ? <NavLink onClick={() => { this.hideComponent("showUpdateAdmin"); this.setPodaci("showUpdateAdmin"); }}>Admin profile</NavLink> : ""}
                            {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "USER" ? <NavLink onClick={() => { this.hideComponent("showUpdateUser"); this.setPodaci("showUpdateUser"); }}>User profile</NavLink> : ""}
                            {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showMockupTool"); }}>Mockup Tool</NavLink> : ""}
                            {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showCollaboration"); }}>Collaborate</NavLink> : ""}
                            {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}>Sign out</NavLink> : ""}
                        </Nav>
                    </Navbar>
                    </Col>
                </Row>
                <ProjectOverview></ProjectOverview>

                <Form>
                    {this.state.aktivni[0] && <SignIn data = {this}/>}
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
                    {this.state.aktivni[6] && <CreateNewServer data ={this}/> }
                </Form>
            </div>
        );
    }
}
Meni.contextType = UrlContext;
export default Meni;