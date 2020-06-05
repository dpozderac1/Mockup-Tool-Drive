import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert } from 'reactstrap';
import axios from 'axios';
import {UrlContext} from '../urlContext';

class UpdateAndDeleteUser extends Component {

    constructor(props) {
        super();
        this.state = {  }
    }
    

    handleClick = () => {
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            axios.delete(url.user + "/deleteUser/" + res.data.id).then(res => {
                console.log("odg: ", res);
                localStorage.setItem('token', "")
                this.props.handler("showSignIn");
            })
            .catch((error) => {
                console.log("Greska!");
                let err = "";
                if(error.response.data.errors === undefined) {
                    err = "Unknown error!";
                }
                else {
                    err = error.response.data.errors[0];
                }
                console.log(err);
            });
            ;
        });
    };

    render() { 
        return (  
                <Form >
                    <hr className="my-2" />
                    <Row>
                        <Col>
                            <h5 style = {{ float: 'left' }} className = "text-secondary">Delete account</h5>
                        </Col>
                    </Row>
                    <Row style={{ paddingRight: "0px" }}>
                        <Col xs = {9}>
                            <Label style = {{float: 'left', paddingTop: '1%'}}>If you delete your account, you will lose all saved data</Label>
                        </Col>
                        <Col>
                            <Button onClick = {this.handleClick} 
                            className = "row justify-content-center" 
                            color = "danger" 
                            style={
                                {
                                    width:'170px'
                                }
                            }>Delete</Button>
                        </Col>
                    </Row>
                </Form>
        );
    }
}
UpdateAndDeleteUser.contextType = UrlContext;
 
export default UpdateAndDeleteUser;