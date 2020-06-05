import React, { Component } from 'react';
import {
    Form, Alert, FormGroup, Col, Row, Container, Label, Button, Input, InputGroup, InputGroupAddon,
    InputGroupText, DropdownMenu, DropdownItem, UncontrolledDropdown, DropdownToggle, Nav, NavLink, NavItem,
    Breadcrumb, BreadcrumbItem
} from 'reactstrap';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import SideBar from './sidebar';
import axios from "axios";
import "../App.css";
import { UrlContext } from '../urlContext';
import CreateNewVersion from './createNewVersion';
import Collaboration from './collaboration';

class ProjectOverview extends Component {
    constructor(props) {
        super();
        this.state = {
            values: [{ value: "Alphabetical", active: true }, { value: "Date created", active: false }, { value: "Date modified", active: false }],
            title: "Alphabetical",
            listaProjekata: [],
            listaProjekataPrethodnoStanje: [],
            searchProjectValue: "",
            listaAktivnih: [true, false, false, false, false, false, false, false, false],
            indeksKlika: "",
            indeksKlikaVerzija: "",
            verzije: [],
            tipFilea: ['Mockups', 'Galen tests', 'PDFs'],
            mockupi: [],
            mockupiPrethodnoStanje: [],
            pdfs: [],
            pdfsPrethodnoStanje: [],
            gspecs: [],
            gspecsPrethodnoStanje: [],
            pdfMockupi: [],
            gspecsMockupi: [],
            pretragaProketi: [],
            searchProjectValue: "",
            deleteSuccess: false,
            errorMessage: "",
            errorVisible: false,
            hide: true,
            isProject: "project",
            filterTitle: "project",
            recentFiles: false,
            listaRecent: [], 
        };

        this.handleDelete = this.handleDelete.bind(this);
        this.backToProjects = this.backToProjects.bind(this);
        this.goBack = this.goBack.bind(this);
    }

    renderDropDownItems() {
        return (
            <DropdownMenu>
                {this.state.values.map(element =>
                    <DropdownItem tag="a" href="#" onClick={() => { (this.state.listaAktivnih[0] || this.state.listaAktivnih[3] || this.state.listaAktivnih[4] || this.state.listaAktivnih[5]) && this.handleClickFilter(element, this.state.filterTitle) }}>
                        {element.value}
                    </DropdownItem>)}
            </DropdownMenu>
        );
    }

    searchFiles = (s, filterTitle) => {
        if (s === "") {
            let projects = [];
            if (filterTitle === "project") {
                projects = [...this.state.listaProjekataPrethodnoStanje];
                this.setState({ listaProjekata: projects });
            }
            else if (filterTitle === "mockup") {
                projects = [...this.state.mockupiPrethodnoStanje];
                this.setState({ mockupi: projects });
            }
            else if (filterTitle === "gspec") {
                projects = [...this.state.gspecsPrethodnoStanje];
                this.setState({ gspecs: projects });
            }
            else if (filterTitle === "pdf") {
                projects = [...this.state.pdfsPrethodnoStanje];
                this.setState({ pdfs: projects });
            }
        }
        else {
            let array = []
            let index = 0;
            if (filterTitle === "project") {
                array = [...this.state.listaProjekataPrethodnoStanje];
                index = 3;
            }
            else if (filterTitle === "mockup") {
                array = [...this.state.mockupiPrethodnoStanje];
                index = "name";
            }
            else if (filterTitle === "gspec") {
                array = [...this.state.gspecsPrethodnoStanje];
                index = "name";
            }
            else if (filterTitle === "pdf") {
                array = [...this.state.pdfsPrethodnoStanje];
                index = "name";
            }
            let newArray = []
            array.filter(project => {
                if (project[index].toUpperCase().includes(s.toUpperCase())) {
                    newArray.push(project);
                }
            });
            if (filterTitle === "project") {
                this.setState({ listaProjekata: newArray });
            }
            else if (filterTitle === "mockup") {
                this.setState({ mockupi: newArray });
            }
            else if (filterTitle === "gspec") {
                this.setState({ gspecs: newArray });
            }
            else if (filterTitle === "pdf") {
                this.setState({ pdfs: newArray });
            }
        }
    }

    handleClickFilter = (element, filterTitle) => {
        let array = [];
        let index_name = 0;
        let index_date_created = 0;
        let index_date_modified = 0;
        if (filterTitle === "project") {
            array = [...this.state.listaProjekata];
            index_name = 3;
            index_date_created = 1;
            index_date_modified = 2;
        }
        else if (filterTitle === "mockup") {
            array = [...this.state.mockupi];
            index_name = "name";
            index_date_created = "date_created";
            index_date_modified = "date_modified";
        }
        else if (filterTitle === "pdf") {
            array = [...this.state.pdfs];
            index_name = "name";
            index_date_created = "date_created";
            index_date_modified = "date_modified";
        }
        else if (filterTitle === "gspec") {
            array = [...this.state.gspecs];
            index_name = "name";
            index_date_created = "date_created";
            index_date_modified = "date_modified";
        }
        if (element.value === "Alphabetical") {
            array.sort(function (a, b) {
                if (a[index_name].toUpperCase() < b[index_name].toUpperCase()) { return -1; }
                if (a[index_name].toUpperCase() > b[index_name].toUpperCase()) { return 1; }
                return 0;
            });
        }
        else if (element.value === "Date created") {
            array.sort(function (a, b) {
                if (Date.parse(a[index_date_created]) > Date.parse(b[index_date_created])) { return -1; }
                if (Date.parse(a[index_date_created]) < Date.parse(b[index_date_created])) { return 1; }
                return 0;
            });
        }
        else if (element.value === "Date modified") {
            array.sort(function (a, b) {
                if (Date.parse(a[index_date_modified]) > Date.parse(b[index_date_modified])) { return -1; }
                if (Date.parse(a[index_date_modified]) < Date.parse(b[index_date_modified])) { return 1; }
                return 0;
            });
        }
        if (filterTitle === "project") {
            this.setState({ listaProjekata: array, title: element.value });
        }
        else if (filterTitle === "mockup") {
            this.setState({ mockupi: array, title: element.value });
        }
        else if (filterTitle === "pdf") {
            this.setState({ pdfs: array, title: element.value });
        }
        else if (filterTitle === "gspec") {
            this.setState({ gspecs: array, title: element.value });
        }
    };

    componentDidMount() {
        this.getProjectsOfUser();
    }

    backToProjects() {
        if (this.state.isProject === "project") {
            this.getProjectsOfUser();
            this.setState({ listaAktivnih: [true, false, false, false, false, false, false, false, false], isProject: "project", searchProjectValue: "", filterTitle: "project" });
        }
        else if (this.state.isProject === "version") {
            this.udjiUProjekat(this.state.indeksKlika);
            this.setState({ listaAktivnih: [false, true, false, false, false, false, false, false, false], isProject: "version" });
        }
    }

    goBack() {
        if (this.state.isProject === "project") {
            this.setState({ listaAktivnih: [true, false, false, false, false, false, false, false, false], isProject: "project", filterTitle: "project" });
        }
        else if (this.state.isProject === "version") {
            this.setState({ listaAktivnih: [false, true, false, false, false, false, false, false, false], isProject: "version" });
        }
        else if (this.state.isProject === "mockup") {
            this.udjiUFile(this.state.indeksKlikaVerzija);
            this.setState({ listaAktivnih: [false, false, false, true, false, false, false, false, false], isProject: "mockup" });
        }
    }

    handleDelete(indeks) {
        let url = this.context;
        const elementToDelete = this.state.listaProjekata[indeks];
        axios.delete(url.project + "/delete/project/" + elementToDelete[0]).then(res => {
            let array = [...this.state.listaProjekataPrethodnoStanje];
            const index = array.indexOf(elementToDelete);
            array.splice(index, 1);
            this.setState({ listaProjekata: array, deleteSuccess: true, errorVisible: false, hide: true, listaProjekataPrethodnoStanje: array, searchProjectValue: "" });
            setTimeout(() => { this.setState({ hide: false }) }, 3000);
        })
            .catch((error) => {
                console.log("Greska!");
                let err = "";
                if (error.response.data.errors === undefined) {
                    err = "Unknown error!";
                }
                else {
                    err = error.response.data.errors[0];
                }
                this.setState({ deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true });
                setTimeout(() => { this.setState({ hide: false }) }, 3000);
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
                for (var i = 0; i < projectData.data.length; i++) {
                    axios.get(url.project + "/project/" + projectData.data[i].id).then(oneProject => {
                        var element = [oneProject.data.id, oneProject.data.date_created, oneProject.data.date_modified, oneProject.data.name, oneProject.data.priority, oneProject.data.userID];
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

    vratiNaziv(el) {
        if (el != null)
            return el[3];
    };

    udjiUFile = (indeks) => {
        let url = this.context;
        if (this.state.listaProjekata[indeks] != null) {
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {
                console.log(mockups.data);
                this.setState({
                    mockupi: mockups.data,
                    mockupiPrethodnoStanje: mockups.data
                });

                console.log(this.state.mockupi);
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, true, false, false, false, false, false],
            filterTitle: "mockup",
            title: this.state.values[0].value,
            searchProjectValue: ""
        });
    };

    udjiUPDF = () => {
        this.setState({
            pdfs: [],
            pdfMockupi: [],
        });

        let url = this.context;
        if (this.state.verzije[this.state.indeksKlikaVerzija] != null) {
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {
                mockups.data.map((mockup) => {
                    console.log(mockup.id);
                    axios.get(url.project + "/PDF_Documents/mockup/" + mockup.id).then(pdf => {
                        pdf.data.map((p) => {
                            this.setState(previousState => ({
                                pdfs: [...previousState.pdfs, p],
                                pdfsPrethodnoStanje: [...previousState.pdfs, p],
                                pdfMockupi: [...previousState.pdfMockupi, mockup],
                            }));
                            console.log(p);
                        });
                    });
                });
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, false, false, true, false, false, false],
            filterTitle: "pdf",
            title: this.state.values[0].value,
            searchProjectValue: ""
        });

    }

    udjiUGSPEC = () => {
        this.setState({
            gspecs: [],
            gspecsMockupi: [],
        });

        let url = this.context;
        if (this.state.verzije != null) {
            axios.get(url.project + "/mockups/version/" + this.state.verzije[this.state.indeksKlikaVerzija].id).then(mockups => {

                mockups.data.map((mockup) => {
                    console.log("mockup id ", mockup.id);
                    axios.get(url.project + "/GSPEC_Documents/mockup/" + mockup.id).then(gspec => {
                        gspec.data.map((p) => {
                            this.setState(previousState => ({
                                gspecs: [...previousState.gspecs, p],
                                gspecsPrethodnoStanje: [...previousState.gspecs, p],
                                gspecsMockupi: [...previousState.gspecsMockupi, mockup],
                            }));
                            console.log("gspcp ", p);
                        });
                    });
                });
            });
        }

        this.setState({
            listaAktivnih: [false, false, false, false, true, false, false, false, false],
            filterTitle: "gspec",
            title: this.state.values[0].value,
            searchProjectValue: ""
        });
    }

    udjiUProjekat = (indeks) => {
        console.log(indeks);
        let url = this.context;
        if (this.state.listaProjekata[indeks] != null) {
            axios.get(url.project + "/versions/project/" + this.state.listaProjekata[indeks][0]).then(versions => {
                console.log(versions.data);
                this.setState({
                    verzije: versions.data
                });

                console.log("ta verzija: ", this.state.verzije[0]);
            });
        }

        this.setState({
            listaAktivnih: [false, true, false, false, false, false, false, false, false],
            indeksKlika: indeks,
            isProject: "version"
        });
    }

    udjiUVerziju = (indeks) => {
        this.setState({
            listaAktivnih: [false, false, true, false, false, false, false, false, false],
            indeksKlikaVerzija: indeks
        });
    }

    deleteVersion = (indeks) => {
        let url = this.context;
        if (this.state.verzije != null) {
            console.log(this.state.verzije)
            axios.delete(url.project + "/delete/version/" + this.state.verzije[indeks].id).then(res => {
                let array = [...this.state.verzije];
                array.splice(indeks, 1)
                this.setState({
                    verzije: array,
                    listaAktivnih: [false, true, false, false, false, false, false, false, false],
                    deleteSuccess: true,
                    errorVisible: false,
                    hide: true,
                    isProject: "version"
                });
                setTimeout(() => { this.setState({ hide: false }) }, 3000);
            })
                .catch((error) => {
                    console.log("Greska!");
                    let err = "";
                    if (error.response.data.errors === undefined) {
                        err = "Unknown error!";
                    }
                    else {
                        err = error.response.data.errors[0];
                    }
                    this.setState({ deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true });
                    setTimeout(() => { this.setState({ hide: false }) }, 3000);
                });
        }
    }

    deleteMockup = (indeks) => {
        console.log("indeks za obrisat: ", indeks);
        let url = this.context;
        if (this.state.mockupi != null) {
            console.log(this.state.verzije)
            const elementToDelete = this.state.mockupi[indeks];
            axios.delete(url.project + "/delete/mockup/" + this.state.mockupi[indeks].id).then(res => {
                let array = [...this.state.mockupi];
                array.splice(indeks, 1);
                let newArray = [...this.state.mockupiPrethodnoStanje];
                const index = newArray.indexOf(elementToDelete);
                newArray.splice(index, 1);
                this.setState({
                    mockupi: newArray,
                    mockupiPrethodnoStanje: newArray,
                    listaAktivnih: [false, false, false, true, false, false, false, false, false],
                    deleteSuccess: true,
                    errorVisible: false,
                    hide: true,
                    filterTitle: "mockup",
                    searchProjectValue: ""
                });
                setTimeout(() => { this.setState({ hide: false }) }, 3000);
            })
                .catch((error) => {
                    console.log("Greska!");
                    let err = "";
                    if (error.response.data.errors === undefined) {
                        err = "Unknown error!";
                    }
                    else {
                        err = error.response.data.errors[0];
                    }
                    this.setState({ deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true });
                    setTimeout(() => { this.setState({ hide: false }) }, 3000);
                });
        }

    }

    deletePDF = (indeks) => {
        let url = this.context;
        if (this.state.pdfs != null) {
            const elementToDelete = this.state.pdfs[indeks];
            console.log("pdfovi delete: ", elementToDelete);
            axios.delete(url.project + "/delete/pdf_document/" + this.state.pdfs[indeks].id).then(res => {
                let array = [...this.state.pdfs];
                array.splice(indeks, 1)
                let newArray = [...this.state.pdfsPrethodnoStanje];
                const index = newArray.indexOf(elementToDelete);
                newArray.splice(index, 1);
                this.setState({
                    pdfs: newArray,
                    pdfsPrethodnoStanje: newArray,
                    listaAktivnih: [false, false, false, false, false, true, false, false, false],
                    deleteSuccess: true,
                    errorVisible: false,
                    hide: true,
                    filterTitle: "pdf",
                    searchProjectValue: ""
                });
                setTimeout(() => { this.setState({ hide: false }) }, 3000);
            })
                .catch((error) => {
                    console.log("Greska!");
                    let err = "";
                    if (error.response.data.errors === undefined) {
                        err = "Unknown error!";
                    }
                    else {
                        err = error.response.data.errors[0];
                    }
                    this.setState({ deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true });
                    setTimeout(() => { this.setState({ hide: false }) }, 3000);
                });
        }

    }

    deleteGSPEC = (indeks) => {
        let url = this.context;
        if (this.state.gspecs != null) {
            console.log(this.state.gspecs)
            const elementToDelete = this.state.gspecs[indeks];
            axios.delete(url.project + "/delete/gspec_document/" + this.state.gspecs[indeks].id).then(res => {
                let array = [...this.state.gspecs];
                array.splice(indeks, 1)
                let newArray = [...this.state.gspecsPrethodnoStanje];
                const index = newArray.indexOf(elementToDelete);
                newArray.splice(index, 1);
                this.setState({
                    gspecs: newArray,
                    gspecsPrethodnoStanje: newArray,
                    listaAktivnih: [false, false, false, false, true, false, false, false, false],
                    deleteSuccess: true,
                    errorVisible: false,
                    hide: true,
                    filterTitle: "gspec",
                    searchProjectValue: ""
                });
                setTimeout(() => { this.setState({ hide: false }) }, 3000);
            })
                .catch((error) => {
                    console.log("Greska!");
                    let err = "";
                    if (error.response.data.errors === undefined) {
                        err = "Unknown error!";
                    }
                    else {
                        err = error.response.data.errors[0];
                    }
                    this.setState({ deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true });
                    setTimeout(() => { this.setState({ hide: false }) }, 3000);
                });
        }

    }

    datumUSpringDatum = () => {
        const today = new Date();
        let month = today.getMonth().toString();
        month.length === 1 ? month = "0" + month : month = month;
        let day = today.getDate().toString();
        day.length === 1 ? day = "0" + day : day = day;
        let hours = today.getHours().toString();
        hours.length === 1 ? hours = "0" + hours : hours = hours;
        let minutes = today.getMinutes().toString();
        minutes.length === 1 ? minutes = "0" + minutes : minutes = minutes;
        let seconds = today.getSeconds().toString();
        seconds.length === 1 ? seconds = "0" + seconds : seconds = seconds;
        const date = today.getFullYear() + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds + "." + today.getMilliseconds() + "Z";
        return date;
    }

    promijeniPrioritet = (indeks, event) => {
        
        let url = this.context;
        if(this.state.listaProjekata != null){
            console.log(this.state.listaProjekata)
            axios.put(url.project + "/addOrUpdateProject/" + this.state.listaProjekata[indeks][0], {
                date_created: this.state.listaProjekata[indeks][1],
                date_modified:  this.datumUSpringDatum(),
                name: this.state.listaProjekata[indeks][3],
                priority: event.target.checked ? 1 : 0,
                userID: this.state.listaProjekata[indeks][5]
            }).then(res => {
                console.log("Odgovor!");
                console.log(res);
                console.log(res.data);
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
                this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
                setTimeout(() => {this.setState({hide: false})}, 3000);
            });
        }
    }

    ucitajPrioritetne = () => {
        this.setState({
            listaProjekata: [],
            listaProjekataPrethodnoStanje: []
        });

        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(userData => {
            console.log("user", userData.data.id);
            axios.get(url.user + "/users/projects/" + userData.data.id).then(projectData => {
                console.log("project", projectData.data);
                var lista = [];
                for(var i = 0; i<projectData.data.length; i++){
                    axios.get(url.project + "/project/" + projectData.data[i].id).then(oneProject => {
                        if(oneProject.data.priority === 1){
                            var array = "";
                            var array2 = ""
                            var zapamti = -1
                            for(var k = 0; k< oneProject.data.date_created.length; k++){
                                if(oneProject.data.date_created[k] === "+"){
                                    zapamti = k
                                }
                                if(zapamti === -1)
                                    array += oneProject.data.date_created[k];
                            }
                            zapamti = -1;
                            for(var k = 0; k< oneProject.data.date_modified.length; k++){
                                if(oneProject.data.date_modified[k] == "+"){
                                    zapamti = k
                                }
                                if(zapamti === -1)
                                    array2 += oneProject.data.date_modified[k];
                            }
                            array += "Z";
                            array2 += "Z";
                            var element = [oneProject.data.id, array, array2, oneProject.data.name, oneProject.data.priority, oneProject.data.userID];
                            lista.push(element);
                            console.log(element);
                            this.setState(previousState => ({
                                listaProjekata: [...previousState.listaProjekata, element],
                                listaProjekataPrethodnoStanje: [...previousState.listaProjekataPrethodnoStanje, element]
                            }));
                            console.log("projekti iz state", oneProject);
                        }
                    });
                    
                }
            });
        }); 
    }

    sharedProjects = () => {
        this.setState({
            listaProjekata: [],
            listaProjekataPrethodnoStanje: []
        });

        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(userData => {
            console.log("user", userData.data.id);
            axios.get(url.user + "/users/projects/" + userData.data.id).then(projectData => {
                console.log("project", projectData.data);
                var lista = [];
                for(var i = 0; i<projectData.data.length; i++){
                    axios.get(url.project + "/project/" + projectData.data[i].id).then(oneProject => {
                        if(oneProject.data.userID != userData.data.id){
                            var array = "";
                            var array2 = ""
                            var zapamti = -1
                            for(var k = 0; k< oneProject.data.date_created.length; k++){
                                if(oneProject.data.date_created[k] === "+"){
                                    zapamti = k
                                }
                                if(zapamti === -1)
                                    array += oneProject.data.date_created[k];
                            }
                            zapamti = -1;
                            for(var k = 0; k< oneProject.data.date_modified.length; k++){
                                if(oneProject.data.date_modified[k] === "+"){
                                    zapamti = k
                                }
                                if(zapamti === -1)
                                    array2 += oneProject.data.date_modified[k];
                            }
                            array += "Z";
                            array2 += "Z";
                            var element = [oneProject.data.id, array, array2, oneProject.data.name, oneProject.data.priority, oneProject.data.userID];
                            lista.push(element);
                            console.log(element);
                            this.setState(previousState => ({
                                listaProjekata: [...previousState.listaProjekata, element],
                                listaProjekataPrethodnoStanje: [...previousState.listaProjekataPrethodnoStanje, element]
                            }));
                            console.log("projekti iz state", oneProject);
                        }
                    });
                    
                }
            });
        }); 
    }

    recentProjects = () => {
        console.log("tutut")
        this.setState({
            listaAktivnih: [false, false, false, false, false, false, false, false, true],
        });
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(userData => {
            console.log("user", userData.data.id);
            axios.get(url.project + "/recentFiles/" + userData.data.id).then(projectData => {
                console.log(projectData.data.pdf);
                console.log(projectData.data.galen);
                console.log(projectData.data.html);
                var lista = []
                console.log(projectData.data)
                /*projectData.data.map((el) => {
                    console.log(el)
                })*/
                
                if(projectData.data.html.length != 0){
                    let h = projectData.data.html;
                    lista.push({tip:"html", versionId: h.versionId, name: h.name, file: h.file, 
                                date_modified: h.date_modified, date_created: h.date_created, 
                                accessed_date: h.accessed_date, id: h.id})
                }
                if(projectData.data.galen.length != 0){
                    let h = projectData.data.galen;
                    lista.push({tip:"galen", mockupId: h.mockupId, name: h.name, file: h.file, 
                                date_modified: h.date_modified, date_created: h.date_created, 
                                accessed_date: h.accessed_date, id: h.id})
                }
                if(projectData.data.pdf.length != 0){
                    let h = projectData.data.pdf;
                    lista.push({tip:"pdf", mockupId: h.mockupId, name: h.name, file: h.file, 
                                date_modified: h.date_modified, date_created: h.date_created, 
                                accessed_date: h.accessed_date, id: h.id})
                }

                console.log(lista);
                this.setState({
                    listaRecent: lista
                });
            });
        }); 

    }

    azurirajMockup = (m) => {
        let url = this.context;
        axios.put(url.project + "/addOrUpdateMockup/" + m.id, {
            accessed_date: this.datumUSpringDatum(),
            date_created: m.date_created,
            date_modified: m.date_modified,
            file: m.file,
            name: m.name,
            versionId: m.versionId
        }).then(res => {
            console.log("Odgovor!");
            console.log(res);
            console.log(res.data);
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
            this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
            setTimeout(() => {this.setState({hide: false})}, 3000);
        });
    }

    azurirajFile = (name, m) => {
        
        let url = this.context;
        axios.put(url.project + "/addOrUpdate" + name + "/" + m.id, {
            accessed_date: this.datumUSpringDatum(),
            date_created: m.date_created,
            date_modified: m.date_modified,
            file: m.file,
            name: m.name,
            mockupId: m.mockupId
        }).then(res => {
            console.log("Odgovor!");
            console.log(res);
            console.log(res.data);
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
            this.setState({deleteSuccess: false, errorMessage: err, errorVisible: true, hide: true});
            setTimeout(() => {this.setState({hide: false})}, 3000);
        });
    }

    dinamickiDodajElementeNiza = (niz, a) => {
        return (
            <Row>
                {niz.map((el, indeks) => (
                    <Col
                        style={{ textAlign: 'center' }}
                        md={2}
                    >
                    {a[0] && 
                    <Input addon type="checkbox" aria-label="Priority"
                           defaultChecked = {this.state.listaProjekata[indeks][4]} 
                           onChange =  {(event) => this.promijeniPrioritet(indeks, event)}
                    />}
                        <br />
                        
                        <Button
                            onClick={(e) => {
                                e.preventDefault();
                                console.log("pritisnuto");
                                a[0] && this.udjiUProjekat(indeks);
                                a[1] && this.udjiUVerziju(indeks);
                                a[2] && indeks === 0 && this.udjiUFile(indeks);
                                a[2] && indeks === 1 && this.udjiUGSPEC();
                                a[2] && indeks === 2 && this.udjiUPDF();
                                a[3] && this.props.data.dajIdMockupa(this.state.mockupi[indeks].id);
                                if (a[3]) {
                                    this.azurirajMockup(el);
                                    var nazivVerzije = this.state.verzije[this.state.indeksKlikaVerzija].versionName;
                                    var velicina = 2;
                                    if (nazivVerzije === "MOBILE") {
                                        velicina = 0;
                                    }
                                    else if (nazivVerzije === "TABLET") {
                                        velicina = 1;
                                    }
                                    this.props.data.velicinaEkrana(velicina);
                                }
                                if(a[4]){
                                    this.props.data.ucitajGalenFile(this.state.gspecs[indeks].id, this.state.gspecs[indeks].name);
                                    this.azurirajFile("GSPEC_Document", el) 
                                }
                                if(a[5]){ 
                                    this.props.data.ucitajPDFFile(this.state.pdfs[indeks].id, this.state.pdfs[indeks].name);
                                    this.azurirajFile("PDF_Document", el)
                                }
                                if(a[8]){
                                    console.log("pritisnuto 2, hehe", el, indeks)
                                    var velicina = 2;
                                    if(el.tip === "html"){
                                        this.azurirajMockup(el);
                                        this.props.data.dajIdMockupa(el.id);
                                        var nazivVerzije = el.versionId.versionName;
                                        if (nazivVerzije === "MOBILE") {
                                            velicina = 0;
                                        }
                                        else if (nazivVerzije === "TABLET") {
                                            velicina = 1;
                                        }
                                        this.props.data.velicinaEkrana(velicina);                                        
                                    }
                                    else if(e.tip === "galen") {
                                        this.azurirajFile("GSPEC_Document", el) 
                                        this.props.data.ucitajGalenFile(el.id, el.name);
                                    }
                                    else if(e.tip === "pdf"){
                                        this.azurirajFile("PDF_Document", el)
                                        this.props.data.ucitajPDFFile(el.id, el.name);
                                    }
                                }
                            }}
                            color="white">
                            {(a[3] || a[4] || a[5] || a[8]) ?
                                <svg class="bi bi-file-earmark" width="7em" height="7em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M4 1h5v1H4a1 1 0 0 0-1 1v10a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V6h1v7a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2z" />
                                    <path d="M9 4.5V1l5 5h-3.5A1.5 1.5 0 0 1 9 4.5z" />
                                </svg> :
                                <svg class="bi bi-folder" width="7em" height="7em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M9.828 4a3 3 0 0 1-2.12-.879l-.83-.828A1 1 0 0 0 6.173 2H2.5a1 1 0 0 0-1 .981L1.546 4h-1L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3v1z" />
                                    <path fill-rule="evenodd" d="M13.81 4H2.19a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91h10.348a1 1 0 0 0 .995-.91l.637-7A1 1 0 0 0 13.81 4zM2.19 3A2 2 0 0 0 .198 5.181l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H2.19z" />
                                </svg>
                            }
                        </Button>
                        <Nav vertical>

                            {(a[3] || a[4] || a[5] || a[8]) && <NavItem>
                                <NavLink onClick={(e) => {
                                    if (a[3]) {
                                        this.props.data.ucitajMockupFile(this.state.mockupi[indeks].id);
                                    }
                                    if (a[4]) {
                                        this.props.data.ucitajGalenFile(this.state.gspecs[indeks].id, this.state.gspecs[indeks].name, true);
                                    }
                                    if (a[5]) {
                                        this.props.data.ucitajPDFFile(this.state.pdfs[indeks].id, this.state.pdfs[indeks].name, true);
                                    }
                                    if(a[8]){
                                        if(el.tip === "html"){
                                            this.props.data.ucitajMockupFile(el.id);
                                        }
                                        else if(el.tip === "galen"){
                                            this.props.data.ucitajGalenFile(el.id, el.name, true);
                                        }
                                        else if(el.tip === "pdf"){
                                            this.props.data.ucitajPDFFile(el.id, el.name, true);
                                        }
                                    }
                                }} href="#">Download</NavLink>
                            </NavItem>}

                            {(!a[2] && !a[8]) && <NavItem>
                                <NavLink id={a[0] ? el[0] : " "}
                                    onClick={(e) => {
                                        if (a[0])
                                            this.handleDelete(indeks);
                                        if (a[1])
                                            this.deleteVersion(indeks);
                                        if (a[3])
                                            this.deleteMockup(indeks);
                                        if (a[4])
                                            this.deleteGSPEC(indeks);
                                        if (a[5])
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
                                    {a[8] && (el.tip != "html" ? (el.name + " - " + el.mockupId.name): el.name)}
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
                    {(a[0] || a[8]) &&
                    <Col xs='2' style={{ height: '100vh' }}>
                        <SideBar data={this}></SideBar>
                    </Col>}
                    <Col xs={(a[0] ||a[8]) ? '10' : '12'}
                        style={{
                            paddingLeft: '3%',
                            paddingRight: '3%'
                        }}>
                        <br />
                        <Form>
                            <FormGroup />
                            <Row>
                                <Col md={!a[2] ? 9 : 12}>
                                    <Breadcrumb className="breadcrumb">
                                        {!a[8] &&
                                        <BreadcrumbItem tag="a"
                                            onClick={(e) => {
                                                this.setState({
                                                    listaAktivnih: [true, false, false, false, false, false, false, false, false],
                                                    isProject: "project",
                                                    filterTitle: "project",
                                                    title: this.state.values[0].value,
                                                    searchProjectValue: "",
                                                    listaProjekata: this.state.listaProjekataPrethodnoStanje
                                                });
                                            }}>
                                            <h4 className="bkItem text-secondary">Projects</h4>
                                        </BreadcrumbItem>}
                                        {a[8] && <h4 className="bkItem text-secondary">Recent files</h4>}
                                        {(!a[0] && !a[8]) &&
                                            <BreadcrumbItem tag="a"
                                                onClick={(e) => {
                                                    this.setState({
                                                        listaAktivnih: [false, true, false, false, false, false, false, false],
                                                        isProject: "version",
                                                        title: this.state.values[0].value,
                                                        searchProjectValue: ""
                                                    });
                                                }}>
                                                <h4 className="bkItem text-secondary"> {this.vratiNaziv(this.state.listaProjekata[this.state.indeksKlika])}</h4>
                                            </BreadcrumbItem>}
                                        {(a[2] || a[3] || a[5] || a[4]) &&
                                            <BreadcrumbItem tag="a"
                                                onClick={(e) => {
                                                    this.setState({
                                                        listaAktivnih: [false, false, true, false, false, false, false, false, false],
                                                        title: this.state.values[0].value,
                                                        searchProjectValue: ""
                                                    });
                                                }}>
                                                <h4 className="bkItem text-secondary"> {this.state.verzije[this.state.indeksKlikaVerzija].versionName}</h4>
                                            </BreadcrumbItem>}
                                        {(a[3]) &&
                                            <BreadcrumbItem tag="a"
                                                onClick={(e) => {
                                                    this.setState({
                                                        listaAktivnih: [false, false, false, true, false, false, false, false, false],
                                                        filterTitle: "mockup",
                                                        title: this.state.values[0].value,
                                                        searchProjectValue: "",
                                                        mockupi: this.state.mockupiPrethodnoStanje
                                                    });
                                                }}>
                                                <h4 className="bkItem text-secondary"> {this.state.tipFilea[0]}</h4>
                                            </BreadcrumbItem>}
                                        {(a[5]) &&
                                            <BreadcrumbItem tag="a"
                                                onClick={(e) => {
                                                    this.setState({
                                                        listaAktivnih: [false, false, false, false, false, true, false, false, false],
                                                        filterTitle: "pdf",
                                                        title: this.state.values[0].value,
                                                        searchProjectValue: "",
                                                        pdfs: this.state.pdfsPrethodnoStanje
                                                    });
                                                }}>
                                                <h4 className="bkItem text-secondary"> {this.state.tipFilea[2]}</h4>
                                            </BreadcrumbItem>}
                                        {(a[4]) &&
                                            <BreadcrumbItem tag="a"
                                                onClick={(e) => {
                                                    this.setState({
                                                        listaAktivnih: [false, false, false, false, true, false, false, false, false],
                                                        filterTitle: "gspec",
                                                        title: this.state.values[0].value,
                                                        searchProjectValue: "",
                                                        gspecs: this.state.gspecsPrethodnoStanje
                                                    });
                                                }}>
                                                <h4 className="bkItem text-secondary"> {this.state.tipFilea[1]}</h4>
                                            </BreadcrumbItem>}
                                    </Breadcrumb>
                                </Col>
                                {a[1] && <Col md={1}>
                                    <Button onClick={(e) => {
                                        this.setState({
                                            listaAktivnih: [false, false, false, false, false, false, false, true, false]
                                        });
                                    }}

                                        outline color="success"
                                        style={{ width: '100px' }}>
                                        Share
                            </Button>
                                </Col>}
                                {(!a[2] && !a[5] && !a[4] && !a[8]) &&
                                    <Col md={a[1] ? 2 : 3}>
                                        <FormGroup className="float-sm-right"
                                            style={{ paddingRight: '15%' }}>
                                            <Button
                                                onClick={(e) => {
                                                    if (a[0]) {
                                                        this.setState({
                                                            listaAktivnih: [false, false, false, false, false, false, true, false, false],
                                                            isProject: "project"
                                                        });
                                                    }
                                                    else if (a[1]) {
                                                        this.setState({
                                                            listaAktivnih: [false, false, false, false, false, false, true, false, false],
                                                            isProject: "version"
                                                        });
                                                    }
                                                    else if (a[3]) {
                                                        console.log("Kliknuto za kreiranje novog mockupa");
                                                        this.setState({
                                                            listaAktivnih: [false, false, false, false, false, false, true, false, false],
                                                            isProject: "mockup"
                                                        });
                                                    }
                                                }}
                                                className="bg-dark btn form-control">
                                                {a[0] ? "Create project" : a[1] ? "Create new version" : a[3] ? "Create new mockup": ""}
                                            </Button>
                                        </FormGroup>
                                    </Col>
                                }
                            </Row>
                            <hr className="my-2"></hr>
                            {(a[0] || a[3] || a[5] || a[4]) && <Row>
                                <Col md={4}>
                                    <InputGroup>
                                        <Input value={this.state.searchProjectValue} placeholder={a[0] ? "Search projects" : a[3] ? "Search mockups" : a[5] ? "Search pdfs" : a[4] ? "Search gspecs" : " "}
                                            onChange={(e) => {
                                                this.setState({ searchProjectValue: e.target.value })
                                                this.searchFiles(e.target.value, this.state.filterTitle);
                                            }
                                            }
                                        />
                                        <InputGroupAddon addonType="prepend">
                                            <InputGroupText>
                                                <svg class="bi bi-search" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z" />
                                                    <path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z" />
                                                </svg>
                                            </InputGroupText>
                                        </InputGroupAddon>
                                    </InputGroup>
                                </Col>
                                <Col md={8}>
                                    <FormGroup className="float-sm-right"
                                        style={{ paddingRight: '5%' }}>
                                        <UncontrolledDropdown>
                                            <DropdownToggle id="toggle" tag="a" className="nav-link" caret className="align-left">
                                                {this.state.title}
                                            </DropdownToggle>
                                            {this.renderDropDownItems()}
                                        </UncontrolledDropdown>
                                    </FormGroup>
                                </Col>
                            </Row>}
                            <br />

                            <Col xs={12} style={{ padding: '0' }}>
                                {this.state.deleteSuccess === true ? <Alert style={{ display: (this.state.hide === true) ? "block" : "none" }} color="success">Successfully deleted!</Alert> : ""}
                                {this.state.errorVisible === true ? <Alert style={{ display: (this.state.hide === true) ? "block" : "none" }} color="danger">{this.state.errorMessage}</Alert> : ""}
                            </Col>

                            <br />
                            {a[0] && this.dinamickiDodajElementeNiza(this.state.listaProjekata, a)}
                            {a[1] && this.dinamickiDodajElementeNiza(this.state.verzije, a)}
                            {a[2] && this.dinamickiDodajElementeNiza(this.state.tipFilea, a)}
                            {a[3] && this.dinamickiDodajElementeNiza(this.state.mockupi, a)}
                            {a[4] && this.dinamickiDodajElementeNiza(this.state.gspecs, a)}
                            {a[5] && this.dinamickiDodajElementeNiza(this.state.pdfs, a)}
                            {a[8] && this.dinamickiDodajElementeNiza(this.state.listaRecent, a)}
                        </Form>
                    </Col>
                </Row>
            }
                {a[6] && <CreateNewVersion data={this} />}
                {a[7] && <Collaboration data={this} />}
            </Form>
        );
    }
}

ProjectOverview.contextType = UrlContext;

export default ProjectOverview;