import React, { Component } from 'react';
import { Button, Form, Col, Container, Row, Alert, Card, CardBody, CardTitle, CardSubtitle, CardHeader } from 'reactstrap';
import axios from 'axios';
import { UrlContext } from '../urlContext';
import { Icon } from '@opuscapita/react-icons';


class Collaboration extends Component {
    constructor(props) {
        super();
        this.state = {
            idProjekta: "2",
            korisnici: [],
            postojeKorisnici: false,
            idKorisnika: "",
            usernameNovogKorisnika: "",
            nepostojeciUsername: false,
            uspjesnoDodan: false
        };
        this.dajSharedKorisnike = this.dajSharedKorisnike.bind(this);
        this.dodajSharedKorisnika = this.dodajSharedKorisnika.bind(this);
    }

    componentDidMount() {
        this.dajSharedKorisnike();
    }

    dajSharedKorisnike() {
        let url = this.context;
        axios.get(url.gateway + "/getUser/" + localStorage.getItem('token')).then(res => {
            this.setState({
                idKorisnika: res.data.id
            });
            console.log("Moj id je: ");
            console.log(res.data.id);
            axios.get(url.user + "/users/sharedProjects/" + this.state.idProjekta).then(res => {
                this.setState({
                    korisnici: res.data,
                });
                if (this.state.korisnici.length != 0) {
                    this.setState({ postojeKorisnici: true });
                }
                else {
                    this.setState({ postojeKorisnici: false });
                }
                console.log(res.data);
            })
                .catch((error) => {
                    console.log("Greska u GET!");
                    console.log(error);
                });
        })
            .catch((error) => {
            });
    }

    dodajSharedKorisnika(e) {
        e.preventDefault();
        let url = this.context;
        if (this.state.usernameNovogKorisnika !== "") {
            axios.get(url.user + "/users/username/" + this.state.usernameNovogKorisnika).then(res => {
                this.setState({
                    nepostojeciUsername: false
                });
                axios.post(url.user + "/addProjectToUser/" + res.data.id, {
                    id: this.state.idProjekta
                }).then(res1 => {
                    this.setState({
                        uspjesnoDodan: true
                    });
                })
                    .catch((error) => {
                        console.log("Greska u GET!");
                        console.log(error);
                    });
            })
                .catch((error) => {
                    this.setState({
                        nepostojeciUsername: true,
                        uspjesnoDodan: false
                    });
                });
        }
    }

    render() {
        return (
            <Container className="col-lg-4" style={{
                position: 'absolute', left: '50%', top: '52%',
                transform: 'translate(-50%, -50%)'
            }}>
                <h1 className="text-secondary" style={{ textAlign: "left" }}>Add people to collaborate</h1>
                <hr className="my-2" />
                <Form onSubmit={this.dodajSharedKorisnika}>
                    <Row>
                        <Col sm="12">
                            <div className="input-group md-form form-sm form-2 pl-0">
                                <input className="form-control my-0 py-1 amber-border" type="text" placeholder="Find people" aria-label="Search"
                                    onChange={(e) =>
                                        this.setState({ usernameNovogKorisnika: e.target.value })
                                    } />
                                <div className="input-group-append">
                                    <span className="input-group-text amber lighten-3" id="basic-text1"><i className="fas fa-search text-grey"
                                        aria-hidden="true"><Icon type="indicator" name="search" height={20} /></i></span>
                                </div>
                            </div>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm="12">
                            {this.state.nepostojeciUsername ? <Alert color="danger" style={{ marginTop: "5px" }}>Username does not exist!</Alert> : ""}
                            {this.state.uspjesnoDodan ? <Alert color="success" style={{ marginTop: "5px" }}>User is successfully added!</Alert> : ""}
                        </Col>
                    </Row>
                    <Row>
                        <Col sm="12">
                            <br />
                            {this.state.postojeKorisnici ? "" : "You didn't add people to collaborate"}
                        </Col>
                    </Row>
                    {this.state.korisnici.map(korisnik => {
                        return (
                            <Row key={korisnik.id}>
                                <Col sm="12">
                                    <Card className="flex-row flex-wrap">
                                        <CardHeader className="border-0">
                                            <Icon type="product" name="user" />
                                        </CardHeader>
                                        <CardBody className="flex-row flex-wrap">
                                            <CardTitle>{korisnik.name + " " + korisnik.surname}{korisnik.id === this.state.idKorisnika ? " (you)" : ""} </CardTitle>
                                            <CardSubtitle style={{ fontStyle: "italic", fontSize: "14px" }}>{korisnik.username}</CardSubtitle>
                                        </CardBody>
                                    </Card>
                                </Col>
                            </Row>
                        )
                    })}
                    <br />
                    <Row style={{ paddingRight: "15px" }}>
                        <Button type="submit" id="submitButton" className="secondary px-3 bg-dark" style={{ marginLeft: "auto" }}>Done</Button>
                    </Row>
                </Form>
            </Container >
        );
    }
}

Collaboration.contextType = UrlContext;
export default Collaboration;