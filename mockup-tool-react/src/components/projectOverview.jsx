import React, { Component} from 'react';
import {Form, Alert, FormGroup,  Col,  Row, Container, Label, Button, Input, InputGroup, InputGroupAddon,
     InputGroupText, DropdownMenu, DropdownItem, UncontrolledDropdown, DropdownToggle, Nav, NavLink, NavItem,
     Breadcrumb, BreadcrumbItem} from 'reactstrap';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import SideBar from './sidebar';
import axios from "axios";
import "../App.css";
import {UrlContext} from '../urlContext';
import CreateNewVersion from './createNewVersion';
import Collaboration from './collaboration';

class ProjectOverview extends Component {
    constructor(props) {
        super();
        this.state = {
            values: [{value: "Alphabetical", active: true}, {value: "Date created", active: false}, {value: "Date modified", active: false}],
            title : "Alphabetical",
            listaProjekata: [],
            listaProjekataPrethodnoStanje: [],
            searchProjectValue: "",
            listaAktivnih: [true, false, false, false, false, false, false, false],
            indeksKlika: "",
            indeksKlikaVerzija: "",
            verzije: [],
            tipFilea: ['Mockups', 'Galen tests', 'PDFs'],
            mockupi: [],
            pdfs: [],
            gspecs: [], 
            pdfMockupi: [],
            gspecsMockupi: [],
            pretragaProketi: [],
            searchProjectValue: "",
            deleteSuccess: false,
            errorMessage: "",
            errorVisible: false,
            hide: true,
            isProject: "project"
        };

        this.handleClick = this.handleClick.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.backToProjects = this.backToProjects.bind(this);
        this.goBack = this.goBack.bind(this);
    }

    renderDropDownItems() {
        return (
        <DropdownMenu>
            {this.state.values.map(element => 
                <DropdownItem tag="a" href="#" onClick={() => {this.state.listaAktivnih[0] && this.handleClickFilter(element)}}>
                    {element.value}
                </DropdownItem>) }
        </DropdownMenu>
        );
    }

    searchFiles = (s) =>{
        if(s == "") {
            let projects = [...this.state.listaProjekataPrethodnoStanje];
            console.log("projekti: ", projects);
            this.setState({listaProjekata: projects});
        }
        else{
            if(this.state.listaProjekata != null){
                let array = [...this.state.listaProjekataPrethodnoStanje];
                let newArray = []
                array.filter(project => {
                    if(project[3].toUpperCase().includes(s.toUpperCase())) {
                        newArray.push(project);
                    }
                });
                this.setState({listaProjekata: newArray});
            }
        }
    }

    handleClickFilter = (element) => {
        let array = [...this.state.listaProjekata];
        if(element.value == "Alphabetical") {
            array.sort(function(a, b){
                if(a[3].toUpperCase() < b[3].toUpperCase()) { return -1; }
                if(a[3].toUpperCase() > b[3].toUpperCase()) { return 1; }
                return 0;
            });
        }
        else if (element.value == "Date created") {
            array.sort(function(a, b){
                if(Date.parse(a[1]) < Date.parse(b[1])) { return -1; }
                if(Date.parse(a[1]) > Date.parse(b[1])) { return 1; }
                return 0;
            });
        }
        else if (element.value == "Date modified") {
            array.sort(function(a, b){
                if(Date.parse(a[2]) < Date.parse(b[2])) { return -1; }
                if(Date.parse(a[2]) > Date.parse(b[2])) { return 1; }
                return 0;
            });
        }
        this.setState({listaProjekata: array, title: element.value});
    };

    componentDidMount() {
        this.getProjectsOfUser();
    }
 
    handleClick (element) {
        this.setState({listaAktivnih: [false, false, false, false, false, false, true, false]});
    };

    backToProjects() {
        if(this.state.isProject === "project") {
            this.getProjectsOfUser();
            this.setState({listaAktivnih: [true, false, false, false, false, false, false, false], isProject: "project", searchProjectValue: ""});
        }
        else if (this.state.isProject === "version") {
            this.udjiUProjekat(this.state.indeksKlika);
            this.setState({listaAktivnih: [false, true, false, false, false, false, false, false], isProject: "version"});
        }
    }

    goBack () {
        if(this.state.isProject === "project") {
            this.setState({listaAktivnih: [true, false, false, false, false, false, false, false], isProject: "project"});
        }
        else if (this.state.isProject === "version") {
            this.setState({listaAktivnih: [false, true, false, false, false, false, false, false], isProject: "version"});
        }
    }

    handleDelete (id) {
        let url = this.context;
        axios.delete(url.project + "/delete/project/" + id).then(res => {
            let array = [...this.state.listaProjekataPrethodnoStanje];
            let index = "";
            array.forEach(element => {
                if (id == element[0]) {
                    index = array.indexOf(element);
                }
            });
            array.splice(index, 1);
            console.log("daj array: ", array);
            this.setState({listaProjekata: array, deleteSuccess: true, errorVisible: false, hide: true, listaProjekataPrethodnoStanje: array, searchProjectValue: ""});
            setTimeout(() => {this.setState({hide: false})}, 3000);
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
            this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
            setTimeout(() => {this.setState({hide: false})}, 3000);
        });
    }

    getProjectsOfUser = () => {
        this.setState({
            listaProjekata: []
        });
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
                            listaProjekata: [...previousState.listaProjekata, element],
                            listaProjekataPrethodnoStanje: [...previousState.listaProjekata, element]
                        }));
                        console.log("projekti iz state", this.state.listaProjekata);
                    });
                    
                }
                console.log("state", this.state.listaProjekata);
            });
        });  
    };

    vratiNaziv(el)
    {
        if(el!=null)
            return el[3];
    };

    udjiUFile = (indeks) => {
        let url = this.context;
        if(this.state.listaProjekata[indeks] != null){
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {
                console.log(mockups.data);
                this.setState({
                    mockupi: mockups.data
                });

                console.log(this.state.mockupi);
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, true, false, false, false, false]
        });
    };

    udjiUPDF = () => {
        this.setState({
            pdfs: [],
            pdfMockupi: [],
        });

        let url = this.context;
        if(this.state.verzije[this.state.indeksKlikaVerzija] != null){
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {
                mockups.data.map((mockup) => {
                    console.log(mockup.id);
                    axios.get(url.project + "/PDF_Documents/mockup/" + mockup.id).then(pdf => {
                        pdf.data.map((p) => {
                            this.setState(previousState => ({
                                pdfs: [...previousState.pdfs, p],
                                pdfMockupi:  [...previousState.pdfMockupi, mockup],
                            }));
                            console.log(p);
                        });
                    });
                }); 
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, false, false, true, false, false]
        });

    }

    udjiUGSPEC = () => {
        this.setState({
            gspecs: [],
            gspecsMockupi: [],
        });

        let url = this.context;
        if(this.state.verzije[this.state.indeksKlikaVerzija] != null){
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {
                
                mockups.data.map((mockup) => {
                    console.log("mockup id ", mockup.id);
                    axios.get(url.project + "/GSPEC_Documents/mockup/" + mockup.id).then(gspec => {
                        gspec.data.map((p) => {
                            this.setState(previousState => ({
                                gspecs: [...previousState.gspecs, p],
                                gspecsMockupi:  [...previousState.gspecsMockupi, mockup],
                            }));
                            console.log("gspcp ", p);
                        });
                    });
                }); 
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, false, true, false, false, false]
        });
    }

    udjiUProjekat = (indeks) => {
        console.log(indeks);
        let url = this.context;
        if(this.state.listaProjekata[indeks] != null){
            axios.get(url.project + "/versions/project/" + this.state.listaProjekata[indeks][0]).then(versions => {
                console.log(versions.data);
                this.setState({
                    verzije: versions.data
                });

                console.log("ta verzija: ", this.state.verzije[0]);
            });
        }

        this.setState({
            listaAktivnih: [false, true, false, false, false, false, false, false],
            indeksKlika: indeks,
            isProject: "version"
        });
    }

    udjiUVerziju = (indeks) => {
        this.setState({
            listaAktivnih: [false, false, true, false, false, false, false, false],
            indeksKlikaVerzija: indeks
        });
    }

    deleteVersion = (indeks) => {
        let url = this.context;
        if(this.state.verzije != null){
            console.log(this.state.verzije)
            axios.delete(url.project + "/delete/version/" + this.state.verzije[indeks].id).then(res => { 
                let array = [...this.state.verzije];
                array.splice(indeks, 1)
                this.setState({ 
                    verzije: array,
                    listaAktivnih: [false, true, false, false, false, false, false, false],
                    deleteSuccess: true, 
                    errorVisible: false, 
                    hide: true,
                    isProject:"version"
                });
                setTimeout(() => {this.setState({hide: false})}, 3000);
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
                this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
            });
        }
    }

    deleteMockup = (indeks) => {
        let url = this.context;
        if(this.state.mockupi != null){
            console.log(this.state.verzije)
            axios.delete(url.project + "/delete/mockup/" + this.state.mockupi[indeks].id).then(res => { 
                let array = [...this.state.mockupi];
                array.splice(indeks, 1)
                this.setState({ 
                    mockupi: array,
                    listaAktivnih: [false, false, false, true, false, false, false, false],
                    deleteSuccess: true, 
                    errorVisible: false, 
                    hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
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
                this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
            });
        }
    
    }

    deletePDF = (indeks) => {
        let url = this.context;
        if(this.state.pdfs != null){
            console.log(this.state.pdfs)
            axios.delete(url.project + "/delete/pdf_document/" + this.state.pdfs[indeks].id).then(res => { 
                let array = [...this.state.pdfs];
                array.splice(indeks, 1)
                this.setState({ 
                    pdfs: array,
                    listaAktivnih: [false, false, false, false, false, true, false, false],
                    deleteSuccess: true, 
                    errorVisible: false, 
                    hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
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
                this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
            });
        }
    
    }

    deleteGSPEC = (indeks) => {
        let url = this.context;
        if(this.state.gspecs != null){
            console.log(this.state.gspecs)
            axios.delete(url.project + "/delete/gspec_document/" + this.state.gspecs[indeks].id).then(res => { 
                let array = [...this.state.gspecs];
                array.splice(indeks, 1)
                this.setState({ 
                    gspecs: array,
                    listaAktivnih: [false, false, false, false, true, false, false, false],
                    deleteSuccess: true, 
                    errorVisible: false, 
                    hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
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
                this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
            });
        }
    
    }

    dinamickiDodajElementeNiza = (niz, a) => {
        return(
            <Row>
            {niz.map((el, indeks) => (
            <Col 
            style={{textAlign:'center'}}
                 md={2}
                >
                {a[0] && <Input addon type="checkbox" aria-label="Priority"  defaultChecked={el[4]} disabled/>}
                <br/>
                <Button 
                
                onClick={(e) => {
                    e.preventDefault();
                    a[0] && this.udjiUProjekat(indeks);
                    a[1] && this.udjiUVerziju(indeks);
                    a[2] && indeks == 0 && this.udjiUFile(indeks);
                    a[2] && indeks == 1 && this.udjiUGSPEC();
                    a[2] && indeks == 2 && this.udjiUPDF();
                    }} 
                color="white">
                <svg class="bi bi-folder" width="7em" height="7em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path d="M9.828 4a3 3 0 0 1-2.12-.879l-.83-.828A1 1 0 0 0 6.173 2H2.5a1 1 0 0 0-1 .981L1.546 4h-1L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3v1z"/>
                <path fill-rule="evenodd" d="M13.81 4H2.19a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91h10.348a1 1 0 0 0 .995-.91l.637-7A1 1 0 0 0 13.81 4zM2.19 3A2 2 0 0 0 .198 5.181l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H2.19z"/>
                </svg>
                </Button>
                <Nav vertical>

                {a[3] && <NavItem>
                    <NavLink  href="#">Download</NavLink>
                </NavItem>}

                {!a[2] && <NavItem>
                    <NavLink id = {a[0] ? el[0] : " "} 
                            onClick = {(e) =>{
                            if(a[0])  
                                this.handleDelete(e.target.id);
                            if(a[1])
                                this.deleteVersion(indeks);
                            if(a[3])
                                this.deleteMockup(indeks);
                            if(a[4])
                                this.deleteGSPEC(indeks);
                            if(a[5])
                                this.deletePDF(indeks);
                            }} 
                            className="" href="#">
                    Delete
                    </NavLink>
                </NavItem>}
                <NavItem>
                <Label>
                    {a[0] && el[3]}
                    {a[1] && el.versionName}
                    {a[2] && el}
                    {a[3] && el.name}
                    {a[5] && el.name}
                    {a[4] && el.name}
                    {a[4] && " - " + this.state.gspecsMockupi[indeks].name}
                    {a[5] && " - " + this.state.pdfMockupi[indeks].name}
                </Label>
                </NavItem>
            </Nav>
            </Col>
            ))}
        </Row>
        );
    };

    render() {
        var a = this.state.listaAktivnih;
        return (
            <Form>{!a[6] && !a[7] &&
            <Row>
                {a[0] &&
                <Col xs='2' style={{height:'100vh'}}>
                    <SideBar></SideBar>
                </Col>
                }
                <Col xs={a[0] ? '10' : '12'}
                    style = { {paddingLeft:'3%',
                    paddingRight:'3%'
                }}   
                >
                    <br/>
                    <Form>
                        <FormGroup/>
                        <Row>
                            <Col md={!a[2] ? 9 : 12}>        
                                <Breadcrumb  className="breadcrumb">
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [true, false, false, false, false, false, false, false],
                                                isProject: "project"
                                            });
                                        }}>
                                        <h4 className="bkItem text-secondary">Projects</h4>
                                    </BreadcrumbItem>
                                    {(!a[0]) && 
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [false, true, false, false, false, false, false, false],
                                                isProject: "version"
                                            });
                                        }}> 
                                        <h4 className="bkItem text-secondary"> {this.vratiNaziv(this.state.listaProjekata[this.state.indeksKlika])}</h4>
                                    </BreadcrumbItem>}
                                    {(a[2] || a[3] || a[5] || a[4]) && 
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [false, false, true, false, false, false, false, false],
                                            });
                                        }}> 
                                        <h4 className="bkItem text-secondary"> {this.state.verzije[this.state.indeksKlikaVerzija].versionName}</h4>
                                    </BreadcrumbItem>}
                                    {(a[3]) && 
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [false, false, false, true, false, false, false, false],
                                            });
                                        }}> 
                                        <h4 className="bkItem text-secondary"> {this.state.tipFilea[0]}</h4>
                                    </BreadcrumbItem>}
                                    {(a[5]) && 
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [false, false, false, false, false, true, false, false],
                                            });
                                        }}> 
                                        <h4 className="bkItem text-secondary"> {this.state.tipFilea[2]}</h4>
                                    </BreadcrumbItem>}
                                    {(a[4]) && 
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [false, false, false, false, true, false, false, false],
                                            });
                                        }}> 
                                        <h4 className="bkItem text-secondary"> {this.state.tipFilea[1]}</h4>
                                    </BreadcrumbItem>}
                                </Breadcrumb>
                            </Col>
                            {a[1] && <Col md={1}>
                            <Button onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, false, false, false, false, false, false, true]
                                        });
                                    }}

                                outline color="success"
                                style={{width:'100px'}}>
                                Share
                            </Button>
                            </Col>}
                            {(!a[2] && !a[5] && !a[4]) &&
                            <Col md={a[1] ? 2 : 3}>
                                <FormGroup className="float-sm-right"
                                style={{paddingRight:'15%'}}>
                                    <Button 
                                    onClick = {(e) =>{
                                        if (a[0]) {
                                            this.setState({
                                                listaAktivnih: [false, false, false, false, false, false, true, false],
                                                isProject: "project"
                                            });
                                        }
                                        else if (a[1]) {
                                            this.setState({
                                                listaAktivnih: [false, false, false, false, false, false, true, false],
                                                isProject: "version"
                                            });
                                        }
                                    }}
                                    className="bg-dark btn form-control">
                                    {a[0] ? "Create project" : a[1] ? "Create new version" : "Create new mockup"}
                                    </Button>
                                </FormGroup>
                            </Col>
                            }
                        </Row>
                        <hr className="my-2"></hr>
                        {(a[0] || a[3] || a[5] || a[4]) && <Row>
                            <Col md={4}>
                                <InputGroup>
                                    <Input value = {this.state.searchProjectValue} placeholder = {a[0] ? "Search projects" : a[3] ? "Search mockups": a[5] ? "Search pdfs" : a[4] ? "Search gspecs": " "} 
                                        onChange={(e) =>{
                                        this.setState({ searchProjectValue: e.target.value})
                                        this.searchFiles(e.target.value);
                                    }
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
                                style={{ paddingRight:'5%'}}>
                                    <UncontrolledDropdown>
                                            <DropdownToggle id = "toggle" tag="a" className="nav-link" caret className = "align-left">
                                                {this.state.title}
                                            </DropdownToggle>
                                            {this.renderDropDownItems()}
                                    </UncontrolledDropdown>
                                </FormGroup>
                            </Col>
                        </Row>}
                        <br/>
                        
                            <Col xs = {12} style = {{padding: '0'}}>
                                {this.state.deleteSuccess == true ? <Alert style={{ display: (this.state.hide === true) ? "block" : "none"}} color = "success">Successfully deleted!</Alert> : ""}
                                {this.state.errorVisible == true ? <Alert style={{ display: (this.state.hide === true) ? "block" : "none"}} color = "danger">{this.state.errorMessage}</Alert> : ""}
                            </Col>
                        
                        <br/>
                        {a[0] && this.dinamickiDodajElementeNiza(this.state.listaProjekata, a)}
                        {a[1] && this.dinamickiDodajElementeNiza(this.state.verzije, a)}
                        {a[2] && this.dinamickiDodajElementeNiza(this.state.tipFilea, a)}
                        {a[3] && this.dinamickiDodajElementeNiza(this.state.mockupi, a)}
                        {a[4] && this.dinamickiDodajElementeNiza(this.state.gspecs, a)}
                        {a[5] && this.dinamickiDodajElementeNiza(this.state.pdfs, a)}
                    </Form>
                </Col>
            </Row>
            }
            {a[6] && <CreateNewVersion data = {this}/>}
            {a[7] && <Collaboration data = {this}/>}
            </Form>
          );
    }
}
 
ProjectOverview.contextType = UrlContext;

export default ProjectOverview;