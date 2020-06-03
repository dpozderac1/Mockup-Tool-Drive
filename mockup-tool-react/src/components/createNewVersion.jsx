import React, { Component } from 'react';
import { UncontrolledButtonDropdown, Navbar, Nav, NavItem, NavLink, Button, Form, FormGroup, Label, Input, FormText, Col, Container, Row, ButtonGroup, Alert, UncontrolledDropdown, Dropdown, DropdownItem, DropdownToggle, DropdownMenu } from 'reactstrap';
import axios from 'axios';
import { UrlContext } from '../urlContext';

class CreateNewVersion extends Component {
    constructor(props) {
        super(props);
        this.state = {
            versions: [{
                name: "Desktop", icon: <svg class="bi bi-display" width="5em" height="4em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path d="M5.75 13.5c.167-.333.25-.833.25-1.5h4c0 .667.083 1.167.25 1.5H11a.5.5 0 0 1 0 1H5a.5.5 0 0 1 0-1h.75z" />
                    <path fill-rule="evenodd" d="M13.991 3H2c-.325 0-.502.078-.602.145a.758.758 0 0 0-.254.302A1.46 1.46 0 0 0 1 4.01V10c0 .325.078.502.145.602.07.105.17.188.302.254a1.464 1.464 0 0 0 .538.143L2.01 11H14c.325 0 .502-.078.602-.145a.758.758 0 0 0 .254-.302 1.464 1.464 0 0 0 .143-.538L15 9.99V4c0-.325-.078-.502-.145-.602a.757.757 0 0 0-.302-.254A1.46 1.46 0 0 0 13.99 3zM14 2H2C0 2 0 4 0 4v6c0 2 2 2 2 2h12c2 0 2-2 2-2V4c0-2-2-2-2-2z" />
                </svg>
            },
            {
                name: "Tablet", icon: <svg class="bi bi-tablet" width="5em" height="4em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd" d="M12 1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM4 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H4z" />
                    <path fill-rule="evenodd" d="M8 14a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
                </svg>
            },
            {
                name: "Mobile", icon: <svg class="bi bi-phone" width="5em" height="4em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd" d="M11 1H5a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM5 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H5z" />
                    <path fill-rule="evenodd" d="M8 14a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
                </svg>
            }],
            checked: {
                name: "Desktop", icon: <svg class="bi bi-display" width="5em" height="4em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path d="M5.75 13.5c.167-.333.25-.833.25-1.5h4c0 .667.083 1.167.25 1.5H11a.5.5 0 0 1 0 1H5a.5.5 0 0 1 0-1h.75z" />
                    <path fill-rule="evenodd" d="M13.991 3H2c-.325 0-.502.078-.602.145a.758.758 0 0 0-.254.302A1.46 1.46 0 0 0 1 4.01V10c0 .325.078.502.145.602.07.105.17.188.302.254a1.464 1.464 0 0 0 .538.143L2.01 11H14c.325 0 .502-.078.602-.145a.758.758 0 0 0 .254-.302 1.464 1.464 0 0 0 .143-.538L15 9.99V4c0-.325-.078-.502-.145-.602a.757.757 0 0 0-.302-.254A1.46 1.46 0 0 0 13.99 3zM14 2H2C0 2 0 4 0 4v6c0 2 2 2 2 2h12c2 0 2-2 2-2V4c0-2-2-2-2-2z" />
                </svg>
            },
            projectName: "",
            project: this.props.data.state.isProject,
            date_created: "",
            date_modified: "",
            priority: 0,
            errorVisible: false,
            successProject: false,
            successVersion: false,
            errorMessage: "",
            mockupName: "",
            successMockup: false
        }
        this.addProject = this.addProject.bind(this);
        this.addVersion = this.addVersion.bind(this);

        this.addMockup = this.addMockup.bind(this);
    }

    handleClick = (element) => {
        const e = { name: element.name, icon: element.icon };
        this.setState({ checked: e });
    };

    renderDropDownItems() {
        return <DropdownMenu>{this.state.versions.map(element => <DropdownItem className="block-example border-bottom border-light" style={{ width: '20em' }} tag="a" href="#" onClick={() => this.handleClick(element)}>{element.icon} {element.name}</DropdownItem>)}</DropdownMenu>;
    }

    addProject(e) {
        console.log("kliknuto");
        e.preventDefault();
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            const today = new Date();
            let month = today.getMonth().toString();
            month.length == 1 ? month = "0" + month : month = month;
            let day = today.getDate().toString();
            day.length == 1 ? day = "0" + day : day = day;
            let hours = today.getHours().toString();
            hours.length == 1 ? hours = "0" + hours : hours = hours;
            let minutes = today.getMinutes().toString();
            minutes.length == 1 ? minutes = "0" + minutes : minutes = minutes;
            let seconds = today.getSeconds().toString();
            seconds.length == 1 ? seconds = "0" + seconds : seconds = seconds;
            const date = today.getFullYear() + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds + "." + today.getMilliseconds() + "Z";
            axios.post(url.project + "/addProject/" + res.data.id, {
                name: this.state.projectName,
                date_created: date,
                date_modified: date,
                priority: this.state.priority,
                userID: res.data.id
            })
                .then(res => {
                    axios.post(url.project + "/addVersion", {
                        projectId: {
                            date_created: date,
                            date_modified: date,
                            id: res.data.id,
                            name: this.state.projectName,
                            priority: this.state.priority,
                            userID: res.data.id
                        },
                        versionName: this.state.checked.name.toLocaleUpperCase()
                    })
                        .then(res => {
                            console.log("odg verzija: ", res.data);
                            document.getElementById("projectName").value = "";
                            this.setState({ projectName: "", errorVisible: false, successProject: true, checked: this.state.versions[0] });
                            setTimeout(() => this.props.data.backToProjects(), 2000);
                        })
                        .catch((error) => {
                            console.log("Greska!");
                            let err = "";
                            if (error.response.data.errors == undefined) {
                                err = "Unknown error!";
                            }
                            else {
                                err = error.response.data.errors[0];
                            }
                            this.setState({ errorMessage: err, errorVisible: true, successProject: false });
                            console.log(error.response);
                        });
                })
                .catch((error) => {
                    console.log("Greska!");
                    let err = "";
                    if (error.response.data.errors == undefined) {
                        err = "Unknown error!";
                    }
                    else {
                        err = error.response.data.errors[0];
                    }
                    this.setState({ errorMessage: err, errorVisible: true, successProject: false });
                    console.log(error.response);
                });
        });
    }

    addVersion(e) {
        e.preventDefault();
        let url = this.context;
        const project = this.props.data.state.listaProjekata[this.props.data.state.indeksKlika];
        axios.post(url.project + "/addVersion", {
            projectId: {
                date_created: project[1],
                date_modified: project[2],
                id: project[0],
                name: project[3],
                priority: project[4],
                userID: project[5]
            },
            versionName: this.state.checked.name.toLocaleUpperCase()
        })
            .then(res => {
                console.log("odg verzija: ", res.data);
                this.setState({ errorVisible: false, successVersion: true, checked: this.state.versions[0] });
                setTimeout(() => this.props.data.backToProjects(), 2000);
            })
            .catch((error) => {
                console.log("Greska!");
                let err = "";
                if (error.response.data.errors == undefined) {
                    err = "Unknown error!";
                }
                else {
                    err = error.response.data.errors[0];
                }
                this.setState({ errorMessage: err, errorVisible: true, successVersion: false });
                console.log(error.response);
            });
    }


    addMockup(e) {
        e.preventDefault();
        let url = this.context;
        const project = this.props.data.state.listaProjekata[this.props.data.state.indeksKlika];
        const version = this.props.data.state.verzije[this.props.data.state.indeksKlikaVerzija];
        const today = new Date();
        let month = today.getMonth().toString();
        month.length == 1 ? month = "0" + month : month = month;
        let day = today.getDate().toString();
        day.length == 1 ? day = "0" + day : day = day;
        let hours = today.getHours().toString();
        hours.length == 1 ? hours = "0" + hours : hours = hours;
        let minutes = today.getMinutes().toString();
        minutes.length == 1 ? minutes = "0" + minutes : minutes = minutes;
        let seconds = today.getSeconds().toString();
        seconds.length == 1 ? seconds = "0" + seconds : seconds = seconds;
        const date = today.getFullYear() + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds + "." + today.getMilliseconds() + "Z";
        console.log("Projekat je: ", project);
        console.log("Verzija je: ", version);
        axios.post(url.project + "/addMockup", {
            versionId: {
                projectId: {
                    date_created: project[1],
                    date_modified: project[2],
                    id: project[0],
                    name: project[3],
                    priority: project[4],
                    userID: project[5]
                },
                versionName: version.versionName,
                id: version.id
            },
            name: this.state.mockupName,
            date_created: date,
            date_modified: date,
            accessed_date: date
        })
            .then(res => {
                console.log("odg verzija: ", res.data);
                this.setState({ errorVisible: false, successVersion: true, checked: this.state.versions[0] });
                setTimeout(() => this.props.data.backToProjects(), 2000);
            })
            .catch((error) => {
                console.log("Greska!");
                let err = "";
                if (error.response.data.errors == undefined) {
                    err = "Unknown error!";
                }
                else {
                    err = error.response.data.errors[0];
                }
                this.setState({ errorMessage: err, errorVisible: true, successVersion: false });
                console.log(error.response);
            });
    }

    goBack = () => {
        this.props.data.goBack();
    }

    render() {
        return (
            <Form onSubmit={this.state.project == "project" ? this.addProject : this.state.project == "version" ? this.addVersion : this.addMockup}>
                <Container className="col-log-6" style={
                    {
                        position: 'absolute', left: '50%', top: '50%',
                        transform: 'translate(-50%, -50%)'
                    }
                }>
                    <Form className="row align-items-center row justify-content-center">
                        <Form className="col-md-5 my-auto">
                            <h1 className="text-secondary" style={{ textAlign: "left" }}>{this.state.project == "project" ? "Create project" : this.state.project == "version" ? "Create new version" : "Create mockup"}</h1>
                            <hr className="my-2" />
                            <FormGroup style={{ display: (this.state.project == "project") ? "block" : "none" }}>
                                <Label for="projectNameLabel">Project name</Label>
                                <Input type="text" name="projectName" id="projectName" placeholder="Enter project name" onChange={(e) =>
                                    this.setState({ projectName: e.target.value })
                                } />
                            </FormGroup>
                            <FormGroup style={{ height: '6em', display: (this.state.project == "project" || this.state.project == "version") ? "block" : "none" }}>
                                <Label for="versionLable">Project version</Label>
                                <Navbar color="faded" light className="block-example border border-secondary" expand="md" style={{ height: '100%' }}>
                                    <Nav className="ml-auto" navbar className="align-left">
                                        <UncontrolledDropdown>
                                            <DropdownToggle id="toggle" tag="a" className="nav-link" caret className="align-left">
                                                {this.state.checked.icon}
                                                {this.state.checked.name}
                                            </DropdownToggle>
                                            {this.renderDropDownItems()}
                                        </UncontrolledDropdown>
                                    </Nav>
                                </Navbar>
                            </FormGroup>


                            <FormGroup style={{ height: '6em', display: (this.state.project == "mockup") ? "block" : "none" }}>
                                <Label for="mockupNameLabel">Mockup name</Label>
                                <Input type="text" name="mockupName" id="mockupName" placeholder="Enter mockup name" onChange={(e) =>
                                    this.setState({ mockupName: e.target.value })
                                } />
                            </FormGroup>


                            <Row>
                                <Col>
                                    {this.state.successProject == true && this.state.project == "project" ? <Alert style={{ marginTop: '8%' }} color="success">Project successfully added!</Alert> : ""}
                                    {this.state.successVersion == true && this.state.project == "version" ? <Alert style={{ marginTop: '8%' }} color="success">New project version successfully added!</Alert> : ""}
                                    {this.state.successMockup == true && this.state.project == "mockup" ? <Alert style={{ marginTop: '8%' }} color="success">New mockup successfully added!</Alert> : ""}
                                    {this.state.errorVisible == true ? <Alert style={{ marginTop: '8%' }} color="danger">{this.state.errorMessage}</Alert> : ""}
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <Button outline color="danger" style={{ float: "left", width: "30%", marginTop: '8%', marginLeft: '0' }} onClick={(e) => {
                                        this.goBack()
                                    }} >Cancel</Button>
                                    <Button type="submit" id="submitButton" style={{ float: "right", width: "30%", marginTop: '8%' }} className="bg-dark">Create</Button>
                                </Col>
                            </Row>
                        </Form>
                    </Form>
                </Container>
            </Form>
        );
    }
}
CreateNewVersion.contextType = UrlContext;

export default CreateNewVersion;