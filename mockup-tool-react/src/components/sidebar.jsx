import React, {Component} from 'react';
import {Form, FormGroup,  Col,  Row, Container} from 'reactstrap';
import SideNav, { Toggle, Nav, NavItem, NavIcon, NavText } from '@trendmicro/react-sidenav';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import { render } from '@testing-library/react';

class SideBar extends Component{
    constructor(props) {
        super();
        this.state = {
            
        };
    }
    style={
        position: 'absolute', left: '30%', top: '50%',
        transform: 'translate(-50%, -50%)',
        fontSize: '120%'
    };
    render(){
        return (
                <SideNav
                className = "bg-dark"
                style={{
                    paddingTop:'10%',
                    width: '100%'
                }}
                onSelect={(selected) => {
                    const to = '/' + selected;
                    if(selected === "project"){
                        this.props.data.setState({
                            listaAktivnih: [true, false, false, false, false, false, false, false, false]
                        });
                        this.props.data.getProjectsOfUser();
                    }
                    else if(selected === "priority"){
                        this.props.data.setState({
                            listaAktivnih: [true, false, false, false, false, false, false, false, false]
                        });
                        this.props.data.ucitajPrioritetne();
                    }
                    else if(selected === "shared"){
                        this.props.data.setState({
                            listaAktivnih: [true, false, false, false, false, false, false, false, false]
                        });
                        this.props.data.sharedProjects();
                    }
                    else if(selected === "recent")
                    {
                        this.props.data.setState({
                            listaAktivnih: [false, false, false, false, false, false, false, false, true]
                        });
                        this.props.data.recentProjects();
                    }
                }}
                >
                    <SideNav.Nav defaultSelected="project" 
                    
                    >
                        <NavItem eventKey="project" >
                            <NavText 
                                style={this.style} 
                            >
                                Project
                            </NavText>
                        </NavItem>
                        <NavItem eventKey="priority">
                            <NavText
                                style={this.style}  
                            >
                                Priority
                            </NavText>
                        </NavItem>
                        <NavItem eventKey="shared">
                            <NavText
                                style={this.style}  
                            >
                                Shared
                            </NavText>
                        </NavItem>
                        <NavItem eventKey="recent">
                            <NavText
                            style={this.style}  
                            >
                                Recent
                            </NavText>
                        </NavItem>
                    </SideNav.Nav>
                </SideNav>
    );
    }
}

export default SideBar;