Mockup-Tool-Drive
==

## Opis

Mockup-Tool-Drive omogućava pohranu svih dokumenata generisanih putem Mockup toola na jedno mjesto i time osigurava da potrebni podaci neće biti nepovratno izgubljeni. 

Neke od najvažnijih funkcionalnosti u sklopu Mockup-Tool-Drivea su: 
* kreiranje korisničkog računa
* spašavanje datoteka predodređenih tipova
* pregled već spašenih datoteka 
* spašavanje datoteka na lokalnu mašinu
* brisanje datoteka
* dijeljenje projekata sa drugim korisnicima

Mockup-Tool-Drive je baziran na mikroservisnoj arhitekturi, pri čemu postoje tri mikroservisa:
* Upravljanje korisnicima (User management)
* Upravljanje datotekama (File management)
* Online testiranje (Online testing)

## Preduvjeti

Za build i pokretanje aplikacije potrebno vam je:
* [JDK 8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) ili noviji
* [Maven 3](https://maven.apache.org/)

## Konfiguracija baze
U svojoj zadanoj konfiguraciji sva tri mikroservisa koriste MySQL bazu podataka, koja se popunjava podacima prilikom pokretanja. Baze podataka imaju sljedeće nazive:
* online_testing (za mikroservis za online testiranje)
* projectdb (za mikroservis za upravljanje datotekama)
* nwtproba (za mikroservis za upravljanje korisnicima)

## Pokretanje aplikacije lokalno

### Koraci

**U okviru komandnog prozora otkucati:**
```bash
git clone https://github.com/dpozderac1/Mockup-Tool-Drive.git
```
**Otvoriti IntelliJ IDEA:**

U glavnom meniju izabrati ```
                          File->Open```, te odabrati željeni projekat.

**Pokrenuti eureka-server:**

Jedan od načina jeste pokretanje main metode u:
* com.example.eurekaserver.EurekaServerApplication

Alternativno, moguće je koristiti [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins.html#build-tool-plugins-maven-plugin):
```bash
mvn spring-boot:run
```

Eureka serveru je moguće pristupiti na lokaciji [http://localhost:8761/](http://localhost:8761/).

**Pokrenuti projekat:**

Jedan od načina jeste pokretanje main metode u:
* com.example.demo.DemoApplication kod mikroservisa za upravljanje korisnicima
* com.example.demo.DemoApplication kod mikroservisa za upravljanje datotekama
* com.example.online_testing.OnlineTestingApplication kod mikroservisa za online testiranje

Alternativno, moguće je koristiti [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins.html#build-tool-plugins-maven-plugin)
kako je opisano u prethodnom koraku.








