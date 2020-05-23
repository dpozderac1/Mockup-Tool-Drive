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
            title : "1 - Active",
            errorMessage: "", 
            errorVisible: false,
            success: false
        }
        this.addServer = this.addServer.bind(this);
    }

    handleClick = (element) => {
        this.setState({title: element.value});
    };

    renderDropDownItems() {
        return <DropdownMenu>{ this.state.values.map(element => <DropdownItem tag="a" href="#" onClick={() => this.handleClick(element)}>{ element.value }</DropdownItem>) }</DropdownMenu>;

    }

    addServer(e) {
        e.preventDefault();
        const status = this.state.title == "1 - Active" ? 1 : 0;
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
            this.setState({url: "", port: "", title: this.state.values[0].value, errorVisible: false, success: true});
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
            this.setState({errorMessage: err, errorVisible: true, success: false});
            console.log(err);
        });
    }

    render() { 
        return (  
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>Create new server</h1>
                <hr className="my-2" />
                <Form onSubmit={this.addServer}>
                    <Row>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="urlLabel">URL</Label>
                                <Input type="text" name="url" id="url" onChange={(e) =>
                                    this.setState({ url: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="portLabel">Port</Label>
                                <Input type="text" name="port" id="port" onChange={(e) =>
                                    this.setState({ port: e.target.value })
                                } />
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col xs = {6}>
                            <FormGroup>
                                <Label for="statusLabel">Status</Label>
                                <Navbar color="light" light expand="md">
                                    <Nav className="ml-auto" navbar className = "align-left">
                                        <UncontrolledDropdown>
                                            <DropdownToggle id = "toggle" tag="a" className="nav-link" caret className = "align-left">
                                                {this.state.title}
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
                            {this.state.success == true ? <Alert color = "success">Server successfully added!</Alert> : ""}
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