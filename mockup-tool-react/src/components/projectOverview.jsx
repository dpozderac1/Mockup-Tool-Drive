import React, { Component} from 'react';
import {Form, Alert, FormGroup,  Col,  Row, Container, Label, Button, Input, InputGroup, InputGroupAddon,
     InputGroupText, DropdownMenu, DropdownItem, UncontrolledDropdown, DropdownToggle, Nav, NavLink, NavItem,
     Breadcrumb, BreadcrumbItem} from 'reactstrap';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import SideBar from './sidebar';
import axios from "axios";
import "../App.css";
import {UrlContext} from '../urlContext';

class ProjectOverview extends Component {
    constructor(props) {
        super();
        this.state = {
            values: [{value: "Alphabetical", active: true}, {value: "Date created", active: false}, {value: "Date modified", active: false}],
            title : "Alphabetical",
            listaProjekata: [],
            searchProjectValue: "",
            listaAktivnih: [true, false, false, false, false, false],
            indeksKlika: "",
            indeksKlikaVerzija: "",
            verzije: [],
            tipFilea: ['Mockups', 'Galen tests', 'PDFs'],
            mockupi: [],
            pdfs: [],
            gspecs: [], 
            pdfMockupi: [],
            gspecsMockupi: [],
            searchProjectValue: "",
            deleteSuccess: false,
            errorMessage: "",
            errorVisible: false,
            hide: true
        };

        this.handleClick = this.handleClick.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
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
 
    handleClick () {
        this.props.data.hideComponent("showCreateProject");
        this.props.data.setIsProjectCreated();
        this.getProjectsOfUser();
    };

    handleDelete (id) {
        let url = this.context;
        axios.delete(url.project + "/delete/project/" + id).then(res => {
            let array = [...this.state.listaProjekata];
            let index = "";
            array.forEach(element => {
                if (id == element[0]) {
                    index = array.indexOf(element);
                }
            });
            array.splice(index, 1);
            this.setState({listaProjekata: array, deleteSuccess: true, errorVisible: false, hide: true});
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
                            listaProjekata: [...previousState.listaProjekata, element]
                        }));
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
            listaAktivnih: [false, false, false, true, false, false],
            indeksKlika: indeks
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
            listaAktivnih: [false, false, false, false, false, true]
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
            listaAktivnih: [false, false, false, false, true, false]
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

                console.log(this.state.verzije[0]);
            });
        }

        this.setState({
            listaAktivnih: [false, true, false, false, false, false],
            indeksKlika: indeks
        });
    }

    udjiUVerziju = (indeks) => {
        this.setState({
            listaAktivnih: [false, false, true, false, false, false],
            indeksKlikaVerzija: indeks
        });
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
                    <NavLink  href="#">Delete</NavLink>
                </NavItem>}
                <NavItem>
                <Label>
                    {a[0] && el[3]}
                    {a[1] && el.version_name}
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
                        <Row form>
                            <Col md={!a[2] ? 9 : 12}>         
                                <Breadcrumb tag="nav" listTag="div" className="breadcrumb">
                                    <BreadcrumbItem tag="a"
                                        onClick = {(e) =>{
                                            this.setState({
                                                listaAktivnih: [true, false, false, false, false, false],
                                            });
                                        }}>
                                        <h4 className="bkItem text-secondary">Projects</h4>
                                    </BreadcrumbItem>
                                    {(!a[0]) && 
                                    <BreadcrumbItem tag="a"
                                    onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, true, false, false, false, false],
                                        });
                                    }}> 
                                    <h4 className="bkItem text-secondary"> {this.vratiNaziv(this.state.listaProjekata[this.state.indeksKlika])}</h4>
                                    </BreadcrumbItem>}
                                    {(a[2] || a[3] || a[5] || a[4]) && 
                                    <BreadcrumbItem tag="a"
                                    onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, false, true, false, false, false],
                                        });
                                    }}> 
                                    <h4 className="bkItem text-secondary"> {this.state.verzije[this.state.indeksKlikaVerzija].version_name}</h4>
                                    </BreadcrumbItem>}
                                    {(a[3]) && 
                                    <BreadcrumbItem tag="a"
                                    onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, false, false, true, false, false],
                                        });
                                    }}> 
                                    <h4 className="bkItem text-secondary"> {this.state.tipFilea[0]}</h4>
                                    </BreadcrumbItem>}
                                    {(a[5]) && 
                                    <BreadcrumbItem tag="a"
                                    onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, false, false, false, false, true],
                                        });
                                    }}> 
                                    <h4 className="bkItem text-secondary"> {this.state.tipFilea[2]}</h4>
                                    </BreadcrumbItem>}
                                    {(a[4]) && 
                                    <BreadcrumbItem tag="a"
                                    onClick = {(e) =>{
                                        this.setState({
                                            listaAktivnih: [false, false, false, false, true, false],
                                        });
                                    }}> 
                                    <h4 className="bkItem text-secondary"> {this.state.tipFilea[1]}</h4>
                                    </BreadcrumbItem>}
                                    
                                </Breadcrumb>
                            </Col>
                            {a[1] && <Col md={1}>
                            <Button 
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
                                    onClick = {a[0] ? this.handleClick: ""}
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
                                    <Input placeholder = {a[0] ? "Search projects" : a[3] ? "Search mockups": a[5] ? "Search pdfs" : a[4] ? "Search gspecs": " "} 
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
          );
    }
}
 
ProjectOverview.contextType = UrlContext;

export default ProjectOverview;