import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form, FormGroup, Input, Container, Row, Col, ButtonGroup } from "reactstrap";
import { Label } from 'reactstrap';

class AdvancedOptions extends Component {
    state = { 
        
     }
    render() { 
        return (  
                <Form>
                    <hr className="my-2"></hr>
                    <h5 class="text-secondary">Advanced options</h5>
                    <FormGroup/>
                    <Row form>
                        <Col md={6}>
                            <FormGroup>
                                <Label type="serverL" name="serverL" id="serverL">Create new server</Label>
                                
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup className="float-sm-right">
                                <Button 
                                    className="bg-dark btn form-control"
                                    style={
                                        {
                                            width:'200px'
                                        }
                                    }
                                >
                                    Create server
                                </Button>
                               
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row form>
                        <Col md={6}>
                            <FormGroup>
                                <Label type="browserL" name="browserL" id="browserL">Create new browser</Label>
                            </FormGroup>
                        </Col>
                        <Col md={6}>
                            <FormGroup className="float-sm-right">
                                <Button 
                                    className="bg-dark btn form-control"
                                    style={
                                        {
                                            width:'200px'
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