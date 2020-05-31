import React, { useState, Component } from 'react';
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    NavbarText,
    ButtonGroup,
    Button,
    Row, ListGroup, ListGroupItem, Col, Form, FormGroup, Alert, Modal, ModalHeader, ModalBody, ModalFooter, Input, Label
} from 'reactstrap';
import SignIn from './signIn';
import SignUp from './signUp';
import Collaboration from './collaboration';
import CreateNewServer from './createNewServer';
import axios from 'axios';
import { UrlContext } from '../urlContext';
import ProjectOverview from './projectOverview';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

class Meni extends Component {
    constructor(props) {
        super();
        this.state = {
            name: "React",
            aktivni: [false, false, false, false, false, false, false, false],
            forma: "signup",
            serverOrBrowser: "",
            servers: [],
            firstServer: "",
            role: "",
            vrijednost: "",
            modal: false,
            modalPDF: false,
            pdfNaziv: "",
            pdfAlert: "hidden",
            pdfAlertUspjesno: false,
            pdfAlertTekst: ""
        };
        this.hideComponent = this.hideComponent.bind(this);
        this.hideAll = this.hideAll.bind(this);
        this.setRole = this.setRole.bind(this);

        this.snimiFajl = this.snimiFajl.bind(this);
        this.napraviCSS = this.napraviCSS.bind(this);
        this.napraviCSSBefore = this.napraviCSSBefore.bind(this);
        this.napraviCSSAfter = this.napraviCSSAfter.bind(this);
        this.snimiPDF = this.snimiPDF.bind(this);
        this.toggle = this.toggle.bind(this);
        this.togglePDF = this.togglePDF.bind(this);
    }

    componentDidMount() {
        console.log("desilo se");
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            this.setState({ role: res.data.roleID.role_name });
        })
            .catch((error) => {
                console.log("Greska u GET /getUser");
                localStorage.setItem("token", "");
                this.setState({
                    name: "React"
                });
            });
        this.sakrijMockupTool();
    }

    divStyle = {
        overflowY: 'scroll'
    };

    hideComponent(name) {
        console.log(name);

        switch (name) {
            case "showSignIn":
                this.setState({ aktivni: [true, false, false, false, false, false, false] });
                break;
            case "showSignUp":
                this.setState({ aktivni: [false, false, true, false, false, false, false, false] });
                break;
            case "showUpdateAdmin":
                this.setState({ aktivni: [false, false, false, true, false, false, false, false] });
                break;
            case "showUpdateUser":
                this.setState({ aktivni: [false, false, false, true, false, false, false, false] });
                break;
            case "showMockupTool":
                this.setState({ aktivni: [false, false, false, false, true, false, false, false] });
                break;
            case "showCollaboration":
                this.setState({ aktivni: [false, false, false, false, false, true, false, false] });
                break;
            case "showProjectOverview":
                this.setState({ aktivni: [false, false, false, false, false, false, false, true] });
                break;
        }
    }

    hideAll(name) {
        if (name == "browser") {
            let url = this.context;
            axios.get(url.onlineTesting + "/servers").then(res => {
                let servers = [];
                res.data.map(server => {
                    let s = { "id": server.id, "name": server.url };
                    servers.push(s);
                }
                );
                this.setState({ aktivni: [false, false, false, false, false, false, true, false], serverOrBrowser: name, servers, firstServer: servers[0].name });
            });
        }
        else {
            this.setState({ aktivni: [false, false, false, false, false, false, true, false], serverOrBrowser: name });
        }
    }

    setPodaci = (param) => {
        if (param == "showSignUp") {
            this.setState({
                forma: "signup"
            });
        }
        if (param == "showUpdateAdmin") {
            this.setState({
                forma: "admin"
            });
        }
        if (param == "showUpdateUser") {
            this.setState({
                forma: "user"
            });
        }

    };

    prikaziMockupTool() {
        //document.getElementById('zaglavlje').style.display = "flex";
        document.getElementById('lijevo').style.display = "flex";
        document.getElementById('glavni').style.display = "flex";
        document.getElementById('strelicaToolbara').style.display = "flex";
        document.getElementById('desniToolbar').style.display = "flex";
    }

    sakrijMockupTool() {
        //document.getElementById('zaglavlje').style.display = "none";
        document.getElementById('lijevo').style.display = "none";
        document.getElementById('glavni').style.display = "none";
        document.getElementById('strelicaToolbara').style.display = "none";
        document.getElementById('desniToolbar').style.display = "none";
    }

    setRole(role) {
        this.setState({ role });
    }


    snimiFajl(e) {
        e.preventDefault();
        console.log("Pozvana je snimiFajl funkcija!");
        document.getElementById("glavni").click();
        var fajl = `<!DOCTYPE html><html><head></head><meta charset="utf-8"><body>`;
        var sviElementi = document.getElementById("glavni").getElementsByTagName("*");
        var stil = ``;
        var element = ``;

        for (var i = 0; i < sviElementi.length; i++) {
            if (sviElementi[i].id != "") {

                let vrijednost = this.napraviCSS(sviElementi[i], true);
                stil += `#` + sviElementi[i].id + ` {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSAfter(sviElementi[i]);
                stil += `#` + sviElementi[i].id + `::after {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSBefore(sviElementi[i]);
                stil += `#` + sviElementi[i].id + `::before {` + vrijednost + `}\n`;
                if (sviElementi[i].tagName != "LI") {
                    element = sviElementi[i];
                }
            }
            else if (sviElementi[i].className != "") {
                var klasa = sviElementi[i].className.split(" ")[0];
                let vrijednost = this.napraviCSS(sviElementi[i], true);
                stil += `.` + klasa + ` {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSAfter(sviElementi[i]);
                stil += `.` + klasa + `::after {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSBefore(sviElementi[i]);
                stil += `.` + klasa + `::before {` + vrijednost + `}\n`;
            }
            else {
                var nazivTaga = sviElementi[i].tagName;
                let vrijednost = this.napraviCSS(sviElementi[i], false);
                stil += `#` + element.id + ` ` + nazivTaga + ` {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSAfter(sviElementi[i]);
                stil += `#` + element.id + ` ` + nazivTaga + `::after {` + vrijednost + `}\n`;
                vrijednost = this.napraviCSSBefore(sviElementi[i]);
                stil += `#` + element.id + ` ` + nazivTaga + `::before {` + vrijednost + `}\n`;
            }
        }
        fajl += `<style>[data-oznacen="1"]{
          background: #3D5A75 !important;
        }\n`+ stil + `</style>`;
        fajl += document.getElementById("glavni").innerHTML;
        fajl += `</body></html>`;

        let mockupId = 3;
        let url = this.context;
        axios.get(url.project + "/mockup/" + mockupId).then(res => {
            var trenutniMockup = res.data;
            var formData = new FormData();
            console.log("Trenutni mockup je: ", trenutniMockup);

            const byteNumbers = new Array(fajl.length);
            for (let i = 0; i < fajl.length; i++) {
                byteNumbers[i] = fajl.charCodeAt(i);
            }
            formData.append('file', new Blob([fajl]));
            /*for (var key of formData.entries()) {
                console.log(key[0] + ', ' + key[1]);
            }*/
            axios.put(url.project + "/addOrUpdateFile/" + mockupId, formData, { headers: { 'content-type': 'multipart/form-data' } }).then(res => {
                console.log("Uspjesno");
                this.toggle();
                setTimeout(
                    function () {
                        this.toggle();
                    }
                        .bind(this),
                    2000
                );
            })
                .catch(error => {
                    console.log("Greska u PUT kod addOrUpdateFile{id}");
                    console.log(error);
                });
        })
            .catch(error => {
                console.log("Greska u GET kod mockup/{id}");
                console.log(error);
            });
    }

    napraviCSS(element, vazno) {
        var s = '';
        var o = getComputedStyle(element);
        for (var i = 0; i < o.length; i++) {
            if (vazno) {
                s += o[i] + ':' + o.getPropertyValue(o[i]) + ' !important;';
            }
            else
                s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
        }
        this.setState({
            vrijednost: s
        });
        return s;
    }

    napraviCSSAfter(element) {
        var s = '';
        var o = getComputedStyle(element, "::after");
        for (var i = 0; i < o.length; i++) {
            s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
        }
        this.setState({
            vrijednost: s
        });
        return s;
    };

    napraviCSSBefore(element) {
        var s = '';
        var o = getComputedStyle(element, "::before");
        for (var i = 0; i < o.length; i++) {
            s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
        }
        this.setState({
            vrijednost: s
        });
        return s;
    };

    snimiPDF(e) {
        e.preventDefault();
        var quality = 1;
        let pdf = new jsPDF('landscape');
        html2canvas(document.querySelector('#glavni'), { scale: quality }).then(function (canvas) {
            pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 10, 30, 250, 130);
        });
        let mockupId = 3;
        let url = this.context;
        console.log("Naziv PDF-a: ", this.state.pdfNaziv);
        if (this.state.pdfNaziv === null || this.state.pdfNaziv.length < 2 || this.state.pdfNaziv.length > 255) {
            this.setState({
                pdfAlert: "visible",
                pdfAlertUspjesno: false,
                pdfAlertTekst: "Name has to be 2 to 255 characters long!"
            })
        }
        else {
            axios.get(url.project + "/mockup/" + mockupId).then(res => {
                var trenutniMockup = res.data;
                var formData = new FormData();
                console.log("Trenutni mockup je: ", trenutniMockup);

                formData.append('pdfFile', new Blob([pdf]));
                formData.append('name', this.state.pdfNaziv);
                for (var key of formData.entries()) {
                    console.log(key[0] + ', ' + key[1]);
                }
                axios.post(url.project + "/addPDFFile/" + mockupId, formData, { headers: { 'content-type': 'multipart/form-data' } }).then(res => {
                    console.log("Trenutno je modal: ", this.state.modal);
                    console.log("Uspjesno");
                    this.setState({
                        pdfAlert: "visible",
                        pdfAlertUspjesno: true,
                        pdfAlertTekst: "PDF file is saved successfully!"
                    })
                })
                    .catch(error => {

                        console.log("Greska u POST kod updatePDFFile/{id}");
                        console.log(error);
                    });
            })
                .catch(error => {
                    this.setState({
                        pdfAlert: "visible",
                        pdfAlertUspjesno: false,
                        pdfAlertTekst: "Error occurred!"
                    })
                    console.log("Greska u GET kod mockup/{id}");
                    console.log(error);
                });
        }
    }

    toggle() {
        var trenutnoModal = !this.state.modal;
        this.setState({
            modal: trenutnoModal
        });
    }

    togglePDF() {
        var trenutnoModalPDF = !this.state.modalPDF;
        this.setState({
            pdfAlert: "hidden",
            pdfAlertUspjesno: false,
            modalPDF: trenutnoModalPDF
        });
    }


    render() {
        const { showSignIn, showSignUp, showUpdateAdmin, showUpdateUser } = this.state;

        return (
            <div>
                {/*<Row xs={12}>
                    <Col>*/}
                <Navbar color="light" light expand="md">
                    <NavbarBrand href="/">Mockup tool drive</NavbarBrand>
                    <NavbarToggler />
                    <Nav className="mr-auto" navbar>
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignIn") }}>Sign in</NavLink> : ""}
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignUp"); this.setPodaci("showSignUp"); }}>Sign up</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "ADMIN" ? <NavLink onClick={() => { this.hideComponent("showUpdateAdmin"); this.setPodaci("showUpdateAdmin"); }}>Admin profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "USER" ? <NavLink onClick={() => { this.hideComponent("showUpdateUser"); this.setPodaci("showUpdateUser"); }}>User profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showMockupTool"); }}>Mockup Tool</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showCollaboration"); }}>Collaborate</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showProjectOverview"); }}>Project Overview</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}>Sign out</NavLink> : ""}
                    </Nav>
                    {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.aktivni[4] ?
                        <Navbar className="navbar-right" style={{ padding: "0px 0px" }} color="light" light expand="md">
                            <Nav className="mr-auto" navbar>
                                <Button outline color="secondary" style={{ width: "8em", height: "2em", padding: "0px" }} onClick={this.snimiFajl}>Save Mockup</Button>
                                <Button outline color="secondary" style={{ width: "8em", height: "2em", padding: "0px" }} onClick={this.togglePDF}>Save PDF</Button>
                            </Nav>
                        </Navbar> : ""}
                </Navbar>
                {/*</Col>
                </Row>*/}


                <Modal isOpen={this.state.modal} toggle={this.state.toggle} className="modal-sm text-center">
                    <Alert style={{ marginBottom: "0px" }}>All changes are saved successfully!</Alert>
                </Modal>

                <Modal isOpen={this.state.modalPDF} toggle={this.togglePDF} className="modal-m">
                    <ModalHeader toggle={this.toggle.PDF}>Save PDF</ModalHeader>
                    <ModalBody>
                        <FormGroup>
                            <Label for="pdfFileNameId">PDF file name:</Label>
                            <Input type="text" name="pdfFileName" id="pdfFileNameId" minLength={2} maxLength={255}
                                onChange={(e) =>
                                    this.setState({ pdfNaziv: e.target.value })
                                } />
                        </FormGroup>
                    </ModalBody>
                    <ModalFooter>
                        {this.state.pdfAlert === "visible" && !this.state.pdfAlertUspjesno ? <Alert color="danger">{this.state.pdfAlertTekst}</Alert> : ""}
                        {this.state.pdfAlert === "visible" && this.state.pdfAlertUspjesno ? <Alert color="success">{this.state.pdfAlertTekst}</Alert> : ""}
                        <Button color="secondary" onClick={this.snimiPDF}>Save</Button>{' '}
                        <Button outline color="danger" onClick={this.togglePDF}>Cancel</Button>
                    </ModalFooter>
                </Modal>



                <Form>
                    {this.state.aktivni[0] && <SignIn data={this} />}
                    {this.state.aktivni[1] && <SignUp podaci={this} />}
                    {(this.state.aktivni[2] || this.state.aktivni[3]) &&
                        <div>
                            <Row>
                            </Row>
                            <Row>
                                <SignUp
                                    podaci={this}
                                />
                            </Row>
                        </div>}
                    {this.state.aktivni[4] ? this.prikaziMockupTool() : this.sakrijMockupTool()}
                    {this.state.aktivni[5] && <Collaboration />}
                    {this.state.aktivni[6] && <CreateNewServer data={this} />}
                    {this.state.aktivni[7] && <ProjectOverview data={this} />}
                </Form>
            </div>
        );
    }
}
Meni.contextType = UrlContext;
export default Meni;