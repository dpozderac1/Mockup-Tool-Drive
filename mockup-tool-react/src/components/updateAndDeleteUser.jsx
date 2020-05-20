import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert } from 'reactstrap';
import axios from 'axios';

class UpdateAndDeleteUser extends Component {

    constructor(props) {
        super();
        this.state = {  }
    }
    

    handleClick = () => {
        const token = 'Bearer '.concat(localStorage.getItem('token'));
        axios.get("http://localhost:8080/getUser/" + localStorage.getItem('token')).then(res => {
            axios.delete("http://localhost:8080/user/deleteUser/" + res.data.id, { headers: { Authorization: token } }).then(res => {console.log("odg: ", res)});
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
                    <Row>
                        <Col xs = {9}>
                            <Label style = {{float: 'left', paddingTop: '1%'}}>If you delete your account, you will lose all saved data</Label>
                        </Col>
                        <Col>
                            <Button onClick = {this.handleClick} className = "row justify-content-center" color = "danger" style = { {width: '100%'} }>Delete</Button>
                        </Col>
                    </Row>
                </Form>
        );
    }
}
 
export default UpdateAndDeleteUser;