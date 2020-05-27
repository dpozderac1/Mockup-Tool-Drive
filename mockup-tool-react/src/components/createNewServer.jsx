import React, { Component } from 'react';
import { UncontrolledButtonDropdown, Navbar, Nav, NavItem, NavLink, Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert, UncontrolledDropdown, Dropdown, DropdownItem, DropdownToggle, DropdownMenu } from 'reactstrap';
import axios from 'axios';
import {UrlContext} from '../urlContext';

class CreateNewServer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            url: "",
            port: "",
            values: [{value: "1 - Active", active: true}, {value: "0 - Inactive", active: false}],
            titleServer : "1 - Active",
            titleBrowser: this.props.data.state.firstServer,
            errorMessage: "", 
            errorVisible: false,
            successServer: false,
            successBrowser: false,
            name: "",
            version: "",
            first : true
        }
        this.addServer = this.addServer.bind(this);
        this.addBrowser = this.addBrowser.bind(this);
    }

    handleClick = (element) => {
        this.props.data.state.serverOrBrowser == "server" ? this.setState({titleServer: element.value}) : this.setState({titleBrowser: element});
    };

    renderDropDownItems() {
        if(this.props.data.state.serverOrBrowser == "server"){
            return <DropdownMenu>{ this.state.values.map(element => <DropdownItem tag="a" href="#" onClick={() => this.handleClick(element)}>{ element.value }</DropdownItem>) }</DropdownMenu>;
        }
        else {
            return <DropdownMenu>{ this.props.data.state.servers.map(element => <DropdownItem tag="a" href="#" onClick={() => this.handleClick(element.name)}>{ element.name }</DropdownItem>) }</DropdownMenu>;
        }
    }

    addBrowser(e) {
        e.preventDefault();
        const server = this.state.titleBrowser;
        const found = this.props.data.state.servers.find(function (element) {
            if (element.name == server) {
                return element;
            }
        });
        console.log("nadjen: ", found.id);
        let url = this.context;
        axios.post(url.onlineTesting + "/addBrowser", {
            name: this.state.name,
            version: this.state.version,
            idServer: found.id
        })
        .then(res => {
            console.log("odg: ", res.data); 
            document.getElementById("url").value = "";
            document.getElementById("port").value = "";
            this.setState({name: "", version: "", titleBrowser: this.props.data.state.servers[0].name, errorVisible: false, successBrowser: true});
        })
        .catch((error) => {
            console.log("Greska!");
            let err = "";
            if(error.response.data.errors == undefined) {
                err = "Unknown error!";
            }
            else {
                err = error.response.data.errors[0];
            }
            this.setState({errorMessage: err, errorVisible: true, successBrowser: false});
            console.log(err);
        });
    }

    addServer(e) {
        e.preventDefault();
        const status = this.state.titleServer == "1 - Active" ? 1 : 0;
        console.log(this.state.url, this.state.port, status);
        let url = this.context;
        axios.post(url.onlineTesting + "/addServer", {
            url: this.state.url,
            port: this.state.port,
            status: status
        })
        .then(res => {
            console.log("odg: ", res.data); 
            document.getElementById("url").value = "";
            document.getElementById("port").value = "";
            this.setState({url: "", port: "", titleServer: this.state.values[0].value, errorVisible: false, successServer: true});
        })
        .catch((error) => {
            console.log("Greska!");
            let err = "";
            if(error.response.data.errors == undefined) {
                err = "Unknown error!";
            }
            else {
                err = error.response.data.errors[0];
            }
            this.setState({errorMessage: err, errorVisible: true, successBrowser: false});
            console.log(err);
        });
    }

    render() { 
        return (  
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>{this.props.data.state.serverOrBrowser == "server" ? "Create new server" : "Create new browser"}</h1>
                <hr className="my-2" />
                <Form onSubmit={this.props.data.state.serverOrBrowser == "server" ? this.addServer : this.addBrowser}>
                    <Row>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="urlLabel">{this.props.data.state.serverOrBrowser == "server" ? "URL" : "Name"}</Label>
                                <Input type="text" name="url" id="url" onChange={(e) =>
                                    this.props.data.state.serverOrBrowser == "server" ? this.setState({ url: e.target.value }) : this.setState({ name: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="portLabel">{this.props.data.state.serverOrBrowser == "server" ? "Port" : "Version"}</Label>
                                <Input type="text" name="port" id="port" onChange={(e) =>
                                    this.props.data.state.serverOrBrowser == "server" ? this.setState({ port: e.target.value }) : this.setState({ version: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="statusLabel">{this.props.data.state.serverOrBrowser == "server" ? "Status" : "Server"}</Label>
                                <Navbar color="light" light expand="md">
                                    <Nav className="ml-auto" navbar className = "align-left" style = {{margin: "auto"}}>
                                        <UncontrolledDropdown>
                                            <DropdownToggle id = "toggle" tag="a" className="nav-link" caret className = "align-left">
                                                {this.props.data.state.serverOrBrowser == "server" ? this.state.titleServer : this.state.titleBrowser}
                                            </DropdownToggle>
                                            {this.renderDropDownItems()}
                                        </UncontrolledDropdown>
                                    </Nav>
                                </Navbar>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col xs = {6}>
                        </Col>
                        <Col>   
                            {this.state.errorVisible == true ? <Alert color = "danger">{this.state.errorMessage}</Alert> : ""}
                            {this.state.successServer == true && this.state.successBrowser == false ? <Alert color = "success">Server successfully added!</Alert> : ""}
                            {this.state.successBrowser == true && this.state.successServer == false ? <Alert color = "success">Browser successfully added!</Alert> : ""}
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Button type="submit" id="submitButton" style = {{float: "right", width: "15%"}} className="bg-dark">Create</Button>
                        </Col>
                    </Row>
                </Form>
            </Container>
        );
    }
}
CreateNewServer.contextType = UrlContext;
 
export default CreateNewServer;