import React, { Component } from 'react';
import { UncontrolledButtonDropdown, Navbar, Nav, NavItem, NavLink, Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert, UncontrolledDropdown, Dropdown, DropdownItem, DropdownToggle, DropdownMenu } from 'reactstrap';

class CreateNewServer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            url: "",
            port: "",
            values: [{value: "1 - Active", active: true}, {value: "0 - Inactive", active: false}],
            title : "1 - Active"
        }
    }

    handleClick = (element) => {
        this.setState({title: element.value});
    };

    renderDropDownItems() {
        return <DropdownMenu>{ this.state.values.map(element => <DropdownItem tag="a" href="#" onClick={() => this.handleClick(element)}>{ element.value }</DropdownItem>) }</DropdownMenu>;

    }

    render() { 
        return (  
            <Container className="col-lg-6" style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>Create new server</h1>
                <hr className="my-2" />
                <Form>
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
                                            <DropdownToggle tag="a" className="nav-link" caret className = "align-left">
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
                        <Col>
                            <Button style = {{float: "right", width: "15%"}} className="bg-dark">Create</Button>
                        </Col>
                    </Row>
                </Form>
            </Container>
        );
    }
}
 
export default CreateNewServer;