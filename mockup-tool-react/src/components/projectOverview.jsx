import React, { Component} from 'react';
import {Form, FormGroup,  Col,  Row, Container, Label, Button, Input, InputGroup, InputGroupAddon,
     InputGroupText, DropdownMenu, DropdownItem, UncontrolledDropdown, DropdownToggle, Nav, NavLink, NavItem} from 'reactstrap';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import SideBar from './sidebar';
import axios from "axios";
import SignUp from './signUp';
import {UrlContext} from '../urlContext';

class ProjectOverview extends Component {
    constructor(props) {
        super();
        this.state = {
            values: [{value: "Alphabetical", active: true}, {value: "Druga opcija", active: false}],
            title : "Alphabetical",
            listaProjekata: [],
            searchProjectValue: ""
        };

        this.handleClick = this.handleClick.bind(this);
    }

    renderDropDownItems() {
        return <DropdownMenu>{ this.state.values.map(element => <DropdownItem tag="a" href="#" onClick={() => this.handleClick(element)}>{ element.value }</DropdownItem>) }</DropdownMenu>;

    }

    getProjectsOfUser = () => {
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(userData => {
            console.log("user", userData.data.id);
            axios.get(url.user + "/users/projects/" + userData.data.id).then(projectData => {
                console.log("project", projectData.data);
                var lista = [];
                for(var i = 0; i<projectData.data.length; i++){
                    axios.get(url.project + "/project/" + projectData.data[i].id).then(oneProject => {
                        var element = [oneProject.data.id, oneProject.data.date_created, oneProject.data.date_modified, oneProject.data.name, oneProject.data.priority];
                        lista.push(element);
                        this.setState(previousState => ({
                            listaProjekata: [...previousState.listaProjekata, element]
                        }));
                    });
                    
                }
                console.log("state", this.state.listaProjekata);
            });
        });

        
    };


    componentDidMount() {
        this.getProjectsOfUser();
    }

    
    handleClick () {
        this.props.data.hideComponent("showCreateProject");
        this.props.data.setIsProjectCreated();
        this.getProjectsOfUser();
    };

    render() {
        return (
            <Row>
                <Col xs='2' style={{height:'100vh'}}>
                    <SideBar></SideBar>
                </Col>
                <Col xs='10'>
                    <br/>
                    <Form>
                        <FormGroup/>
                        <Row form>
                            <Col md={4}>
                                <FormGroup
                                style={
                                    {
                                        paddingLeft:'3%',
                                    }
                                }
                                >
                                    <Label type="serverL" name="serverL" id="serverL">
                                    <h4 class="text-secondary">Projects</h4>
                                    </Label>
                                </FormGroup>
                            </Col>
                            <Col md={8}>
                                <FormGroup className="float-sm-right"
                                style={
                                    {
                                        paddingRight:'5%',
                                    }
                                }>
                                    <Button 
                                        onClick = {this.handleClick}
                                        className="bg-dark btn form-control"
                                        style={
                                            {
                                                width:'150px'
                                            }
                                        }
                                    >
                                        Create project
                                    </Button>
                                
                                </FormGroup>
                            </Col>
                        </Row>
                        <hr className="my-2"></hr>
                        <Row>
                            <Col md={4}>
                                <InputGroup>
                                    <Input placeholder="Search projects" 
                                        onChange={(e) =>
                                        this.setState({ searchProjectValue: e.target.value})
                                    } 
                                    />
                                    <InputGroupAddon addonType="prepend">
                                    <InputGroupText>
                                    <svg class="bi bi-search" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"/>
                                    <path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
                                    </svg>
                                    </InputGroupText>
                                    </InputGroupAddon>
                                </InputGroup>
                            </Col>
                            <Col md={8}>
                                <FormGroup className="float-sm-right"
                                style={
                                    {
                                        paddingRight:'5%',
                                    }
                                }>
                                    <UncontrolledDropdown>
                                            <DropdownToggle id = "toggle" tag="a" className="nav-link" caret className = "align-left">
                                                {this.state.title}
                                            </DropdownToggle>
                                            {this.renderDropDownItems()}
                                    </UncontrolledDropdown>
                                
                                </FormGroup>
                            </Col>
                        </Row>
                        <br/>
                        <br/>
                        <Row>
                            {this.state.listaProjekata.map((el) => (
                            <Col
                                className="justify-content-center"
                                md={2}
                                style={
                                    {
                                        paddingLeft:'3%',
                                    }
                                }
                            >
                            <Input addon type="checkbox" aria-label="Priority"  defaultChecked={el[4]} disabled/>
                            <br/>
                            <svg class="bi bi-folder" width="7em" height="7em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                            <path d="M9.828 4a3 3 0 0 1-2.12-.879l-.83-.828A1 1 0 0 0 6.173 2H2.5a1 1 0 0 0-1 .981L1.546 4h-1L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3v1z"/>
                            <path fill-rule="evenodd" d="M13.81 4H2.19a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91h10.348a1 1 0 0 0 .995-.91l.637-7A1 1 0 0 0 13.81 4zM2.19 3A2 2 0 0 0 .198 5.181l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H2.19z"/>
                            </svg>
                            <Nav  
                                style={
                                    {
                                        paddingLeft:'10%',
                                    }
                                }
                            vertical>
                                <NavItem>
                                    <NavLink className="" href="#">Delete</NavLink>
                                </NavItem>
                                <NavItem>
                                    <Label 
                                    
                                    
                                >{el[3]}</Label>
                                </NavItem>
                            </Nav>
                            
                            </Col>
                            ))}
                        </Row>
                    </Form>
                </Col>
            </Row>
          );
    }
}
 
ProjectOverview.contextType = UrlContext;


export default ProjectOverview;