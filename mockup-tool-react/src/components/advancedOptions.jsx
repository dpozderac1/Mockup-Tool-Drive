import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Input, Container, Row, Col, ButtonGroup } from "reactstrap";
import { Label } from 'reactstrap';
import CreateNewServer from './createNewServer';

class AdvancedOptions extends Component {
    state = { 
        clicked : ""
    }

    handleClick = (name) => {
        console.log(name);
        this.props.handler(name);
        this.setState({clicked: name});
    };

    render() { 
        return (  
                <Form>
                    <hr className="my-2"></hr>
                    <h5 class="text-secondary">Advanced options</h5>
                    <FormGroup/>
                    <Row form style={{ paddingRight: "15px" }}>
                        <Col md={6}>
                            <FormGroup>
                                <Label type="serverL" name="serverL" id="serverL">Create new server</Label>
                                
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup className="float-sm-right">
                                <Button 
                                    onClick = {() => this.handleClick("server")}
                                    className="bg-dark btn form-control"
                                    style={
                                        {
                                            marginLeft: "auto", width:"170px" 
                                        }
                                    }
                                >
                                    Create server
                                </Button>
                               
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row form style={{ paddingRight: "15px" }}>
                        <Col md={6}>
                            <FormGroup>
                                <Label type="browserL" name="browserL" id="browserL">Create new browser</Label>
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup className="float-sm-right">
                                <Button 
                                    onClick = {() => this.handleClick("browser")}
                                    className="bg-dark btn form-control"
                                    style={
                                        {
                                            marginLeft: "auto", width:"170px" 
                                        }
                                    }
                                >
                                    Create browser
                                </Button>
                            </FormGroup>
                        </Col>
                    </Row>   
                </Form>            
        );
    }
}
 
export default AdvancedOptions;