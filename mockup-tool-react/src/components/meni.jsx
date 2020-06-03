import React, { useState, Component } from 'react';
import {
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavLink,
    Button,
    Row, Form, FormGroup, Alert, Modal, ModalHeader, ModalBody, ModalFooter, 
    Input, Label, Spinner, Col} from 'reactstrap';
import SignIn from './signIn';
import SignUp from './signUp';
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
            aktivni: [false, false, false, false, false, false, false],
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
            pdfAlertTekst: "",
            password: "",
            mockupId: ""
        };
        this.hideComponent = this.hideComponent.bind(this);
        this.hideAll = this.hideAll.bind(this);
        this.setRole = this.setRole.bind(this);
        this.goBack = this.goBack.bind(this);

        this.snimiFajl = this.snimiFajl.bind(this);
        this.napraviCSS = this.napraviCSS.bind(this);
        this.napraviCSSBefore = this.napraviCSSBefore.bind(this);
        this.napraviCSSAfter = this.napraviCSSAfter.bind(this);
        this.snimiPDF = this.snimiPDF.bind(this);
        this.toggle = this.toggle.bind(this);
        this.togglePDF = this.togglePDF.bind(this);

        this.ucitajMockupFile = this.ucitajMockupFile.bind(this);
        this.dajIdMockupa = this.dajIdMockupa.bind(this);
        this.download = this.download.bind(this);

        this.ucitajPDFFile = this.ucitajPDFFile.bind(this);

        this.ucitajGalenFile = this.ucitajGalenFile.bind(this);
    }

    setPassword = (password) => {
        this.setState({ password });
    };

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
                this.setState({ aktivni: [true, false, false, false, false, false] });
                break;
            case "showSignUp":
                this.setState({ aktivni: [false, false, true, false, false, false, false] });
                break;
            case "showUpdateAdmin":
                this.setState({ aktivni: [false, false, false, true, false, false, false] });
                break;
            case "showUpdateUser":
                this.setState({ aktivni: [false, false, false, true, false, false, false] });
                break;
            case "showMockupTool":
                this.setState({ aktivni: [false, false, false, false, true, false, false] });
                break;
            case "showProjectOverview":
                this.setState({ aktivni: [false, false, false, false, false, false, true] });
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
                this.setState({ aktivni: [false, false, false, false, false, true, false], serverOrBrowser: name, servers, firstServer: servers[0].name });
            });
        }
        else {
            this.setState({ aktivni: [false, false, false, false, false, true, false], serverOrBrowser: name });
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
        //document.getElementById('desniToolbar').style.display = "flex";
        this.ucitajMockupFile();
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

    goBack() {
        this.hideComponent("showUpdateAdmin");
        this.setPodaci("showUpdateAdmin");
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

        //let mockupId = 3;
        let url = this.context;
        axios.get(url.project + "/mockup/" + this.state.mockupId).then(res => {
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
            axios.put(url.project + "/addOrUpdateFile/" + this.state.mockupId, formData, { headers: { 'content-type': 'multipart/form-data' } }).then(res => {
                console.log("Uspjesno");
                this.toggle();
                setTimeout(
                    function () {
                        this.toggle();
                    }
                        .bind(this),
                    1000
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
        //let mockupId = 3;
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
            const input = document.getElementById('glavni');
            html2canvas(input, {
                useCORS: true
            }, { scale: 1 })
                .then(canvas => {
                    document.getElementById("spinnerZaPDF").style.display = "flex";
                    const imgData = canvas.toDataURL('image/png');

                    var formData = new FormData();
                    formData.append('pdfFile', new Blob([imgData]));
                    formData.append('name', this.state.pdfNaziv);
                    /*for (var key of formData.entries()) {
                        console.log(key[0] + ', ' + key[1]);
                    }*/
                    axios.post(url.project + "/addPDFFile/" + this.state.mockupId, formData, { headers: { 'content-type': 'multipart/form-data' } }).then(res => {
                        console.log("Uspjesno");
                        document.getElementById("spinnerZaPDF").style.display = "none";
                        this.setState({
                            pdfAlert: "visible",
                            pdfAlertUspjesno: true,
                            pdfAlertTekst: "PDF file is saved successfully!"
                        })
                    })
                        .catch(error => {
                            document.getElementById("spinnerZaPDF").style.display = "none";
                            this.setState({
                                pdfAlert: "visible",
                                pdfAlertUspjesno: false,
                                pdfAlertTekst: "Error occurred!"
                            })
                            console.log("Greska u POST kod updatePDFFile/{id}");
                            console.log(error);
                        });

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


    ucitajMockupFile(id = 0) {
        let mockupIdTrenutni = id;
        if (mockupIdTrenutni === 0) {
            mockupIdTrenutni = this.state.mockupId;
        }
        let url = this.context;
        axios.get(url.project + "/mockup/file/" + mockupIdTrenutni).then(res => {
            axios.get(url.project + "/mockup/" + mockupIdTrenutni).then(res1 => {
                document.getElementById("nepotrebniPDF").setAttribute("name", res1.data.name);
            })
                .catch((error) => {
                    console.log("Greska u /mockup/{id}");
                });
            document.getElementById("glavni").click();
            var content = res.data;
            //console.log("Content je: ", content);

            if (content !== null && content !== undefined && content !== "" && id === 0) {
                var prviDio = content.split("</style>")[1];
                var procitaniFajl = prviDio.split("</body>")[0];
                if (procitaniFajl != "") {
                    document.getElementById("glavni").innerHTML = procitaniFajl;
                    document.getElementById("importFajlaDugme").click();
                }
            }
            else if (id !== 0) {
                axios.get(url.project + "/mockup/" + mockupIdTrenutni).then(res => {
                    this.download(content, res.data.name + ".html", ".html");
                })
                    .catch((error) => {
                        console.log("Greska u /mockup/{id}");
                    });
            }
            else {
                document.getElementById("glavni").innerHTML = "";
            }
        })
            .catch(error => {
                this.setState({
                    pdfAlert: "visible",
                    pdfAlertUspjesno: false,
                    pdfAlertTekst: "Error occurred!"
                })
                console.log("Greska u GET kod /mockup/file/{id}");
                console.log(error);
            });
    }

    dajIdMockupa(idIzKomponente) {
        console.log("Pozvano dajIdMockupa: ", idIzKomponente);
        this.setState({
            mockupId: idIzKomponente
        });
        this.hideComponent("showMockupTool");
    }


    download(data, filename, type) {
        var file = new Blob([data], { type: type });
        if (window.navigator.msSaveOrOpenBlob) // IE10+
            window.navigator.msSaveOrOpenBlob(file, filename);
        else { // Others
            var a = document.createElement("a"),
                url = URL.createObjectURL(file);
            a.href = url;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            setTimeout(function () {
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            }, 0);
        }
    }

    ucitajPDFFile(id, naziv, snimi = false) {
        let url = this.context;
        let pdfIdTrenutni = id;


        axios.get(url.project + "/pdf_document/file/" + pdfIdTrenutni).then(res => {
            const pdf = new jsPDF('landscape');
            pdf.addImage(res.data, 'PNG', 10, 10);
            //snimanje pdf-a
            if (snimi) {
                pdf.save(naziv);
            }
            else {
                //otvaranje pdf-a u browseru
                const file = new Blob(
                    [pdf.output('blob')],
                    { type: 'application/pdf' });

                //Build a URL from the file
                const fileURL = URL.createObjectURL(file);
                //Open the URL on new Window
                window.open(fileURL);
            }

            //trenutni datum
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
            axios.put(url.project + "/addOrUpdatePDF_Document/" + pdfIdTrenutni, {
                id: pdfIdTrenutni,
                name: naziv,
                accessed_date: date
            }).then(res1 => {

            })
                .catch(error => {
                    console.log("Greska u PUT kod /addOrUpdatePDF_Document/file/{id}");
                    console.log(error);
                });
        })
            .catch(error => {
                console.log("Greska u GET kod /pdf_document/file/{id}");
                console.log(error);
            });
    }

    ucitajGalenFile(id, naziv, snimi = false) {
        let url = this.context;
        axios.get(url.project + "/gspec_document/file/" + id).then(res => {
            //console.log("Content je: ", res.data);
            if (snimi) {
                this.download(res.data, naziv + ".gspec", "txt");
            }
            else {
                const file = new Blob(
                    [res.data],
                    { type: 'text/plain' });

                //Build a URL from the file
                const fileURL = URL.createObjectURL(file);
                //Open the URL on new Window
                window.open(fileURL);
            }

            //trenutni datum
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
            axios.put(url.project + "/addOrUpdateGSPEC_Document/" + id, {
                name: naziv,
                accessed_date: date
            }).then(res2 => {

            })
                .catch(error => {
                    console.log("Greska u PUT kod /addOrUpdatePDF_Document/file/{id}");
                    console.log(error);
                });
        })
            .catch(error => {
                console.log("Greska u GET kod /gspec_document/file/{id}");
                console.log(error);
            });
    }

    velicinaEkrana(velicina) {
        if (velicina === 0) {
            //mobileVersion
            var el = document.getElementById("glavni");
            el.style.height = 70 + "%";
            el.style.width = 25 + "%"
            el.style.cssFloat = "left"
            el.style.overflow = "hidden";
            el.style.border = "thick solid #E8E8E8";
            el.style.borderRadius = 50 + "px";
        }
        else if (velicina === 1) {
            //tabletVersion
            var el = document.getElementById("glavni");
            el.style.height = 79 + "%";
            el.style.width = 50 + "%"
            el.style.cssFloat = "left"
            el.style.overflow = "hidden";
            el.style.border = "thick solid #E8E8E8";
            el.style.borderRadius = 50 + "px";
        }
        else {
            //desktopVersion
            var el = document.getElementById("glavni");
            el.style.height = "calc(100% - 66px)";
            el.style.width = 75 + "%";
            el.style.cssFloat = "right"
            el.style.overflow = "scroll";
            el.style.border = "none";
            el.style.borderRadius = 0 + "px";
        }
    }


    render() {
        const { showSignIn, showSignUp, showUpdateAdmin, showUpdateUser } = this.state;

        return (
            <div>
                <Navbar color="light" light expand="md">
                    <Col xs = {2}>
                    <NavbarBrand style={{fontSize:"150%"}} href="/">Mockup tool drive</NavbarBrand>
                    </Col>
                    <NavbarToggler />
                    <Nav className="mr-auto" navbar>
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignIn") }}>Sign in</NavLink> : ""}
                        {localStorage.getItem("token") === null || localStorage.getItem("token") === "" ? <NavLink onClick={() => { this.hideComponent("showSignUp"); this.setPodaci("showSignUp"); }}>Sign up</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "ADMIN" ? <NavLink onClick={() => { this.hideComponent("showUpdateAdmin"); this.setPodaci("showUpdateAdmin"); }}>Admin profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.role == "USER" ? <NavLink onClick={() => { this.hideComponent("showUpdateUser"); this.setPodaci("showUpdateUser"); }}>User profile</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { this.hideComponent("showProjectOverview"); }}>Project Overview</NavLink> : ""}
                        {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" ? <NavLink onClick={() => { localStorage.removeItem('token'); this.hideComponent("showSignIn") }}>Sign out</NavLink> : ""}
                    </Nav>
                    {localStorage.getItem("token") !== null && localStorage.getItem("token") !== "" && this.state.aktivni[4] ?
                        <Navbar className="navbar-right" style={{ padding: "0px 0px" }} color="light" light expand="md">
                            <Nav className="mr-auto" navbar>
                                <Button outline color="secondary" style={{ width: "8em", height: "2em", padding: "0px" }} onClick={this.snimiFajl}>Save Mockup</Button>
                                <Button outline color="secondary" style={{ width: "8em", height: "2em", padding: "0px" }} onClick={(e) => { this.snimiFajl(e); this.togglePDF(e) }}>Save PDF</Button>
                            </Nav>
                        </Navbar> : ""}
                </Navbar>
                
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
                        <Spinner id="spinnerZaPDF" color="success" style={{ display: "none" }} />
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
                    {this.state.aktivni[5] && <CreateNewServer data={this} />}
                    {this.state.aktivni[6] && <ProjectOverview data={this} />}
                </Form>

                <div id="nepotrebniPDF" name="nepotrebni" style={{ display: "none" }}></div>
            </div>
        );
    }
}
Meni.contextType = UrlContext;
export default Meni;