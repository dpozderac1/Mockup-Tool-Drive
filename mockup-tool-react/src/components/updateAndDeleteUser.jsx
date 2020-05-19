import React, { Component } from 'react';
import { Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert } from 'reactstrap';
import axios from 'axios';

class UpdateAndDeleteUser extends Component {

    constructor(props) {
        super();
        this.state = {  
            token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkcG96ZGVyYWMxIiwiZXhwIjoxNTg5OTM3Nzg2LCJpYXQiOjE1ODk5MDE3ODZ9.e32N7CC6Xze9JcciJs1ZkZXvXXJLyrSaCFNZUzDTrw0"
        }
    }
    

    handleClick = () => {
        console.log("kliknuto");
        const AuthStr = 'Bearer '.concat(this.state.token);
        console.log("token", AuthStr);
        axios.get("http://localhost:8080/user/users/1", { headers: { Authorization: AuthStr } });
    };

    render() { 
        //console.log("props: ", this.props.children);
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