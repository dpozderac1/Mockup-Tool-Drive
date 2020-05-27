function offset(el) {
  var glavni = document.getElementById("glavni");
  var rect = el.getBoundingClientRect(),
    f = glavni.getBoundingClientRect(),
    scrollLeft = window.pageXOffset || document.documentElement.scrollLeft,
    scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  return { top: rect.top + scrollTop - f.top, left: rect.left + scrollLeft - f.left }
}

function bubbleSort(list) {
  let swapped
  let n = list.length - 1
  do {
    swapped = false
    for (let i = 0; i < n; i++) {
      // compare pairs of elements
      // if left element > right element, swap
      if (list[i][1] > list[i + 1][1]) {
        const temp = list[i]
        list[i] = list[i + 1]
        list[i + 1] = temp
        swapped = true
      }
    }
  }
  // continue swapping until sorted
  while (swapped)

  return list
}

function saveText(text, filename) {
  var a = document.createElement('a');
  a.setAttribute('href', 'data:text/plain;charset=utf-u,' + encodeURIComponent(text));
  a.setAttribute('download', filename);
  a.click()
}

function ocitajSveUdaljenosti() {
  var glavni = document.getElementById("glavni");

  var div = glavni.getElementsByTagName('div');
  var udaljenostiSvakogElementaX = [], udaljenostiSvakogElementaY = [];

  var sviElementi = document.getElementById("glavni").getElementsByTagName("*");
  var sveKlase = [];
  for (var i = 0; i < sviElementi.length; i++) {
    if (sviElementi[i].dataset.dodijeljenaklasa != undefined && sviElementi[i].dataset.dodijeljenaklasa != "") {
      sveKlase.push(sviElementi[i].dataset.dodijeljenaklasa);
    }
  }

  var skupKlasa = new Set(sveKlase);
  var nizKlasa = Array.from(skupKlasa);
  console.log("Niz klasa " + nizKlasa);

  var brojacTrenutneKlase = [];
  for (var i = 0; i < nizKlasa.length; i++) {
    brojacTrenutneKlase.push(1);
  }

  console.log("Brojac trenutne klase " + brojacTrenutneKlase);

  for (var i = 0; i < div.length; i++) {
    var dodijeliKlasu = "";
    if (div[i].dataset.inicijalniid != undefined && (div[i].dataset.dodijeljeniid != "" || div[i].dataset.dodijeljenaklasa != "")) {
      var divOffset = offset(div[i]);
      if (div[i].dataset.dodijeljenaklasa != undefined && div[i].dataset.dodijeljenaklasa != "") {
        for (var j = 0; j < nizKlasa.length; j++) {
          if (div[i].dataset.dodijeljenaklasa == nizKlasa[j]) {
            dodijeliKlasu = div[i].dataset.dodijeljenaklasa.replace(/\s+/g, '.') + "." + brojacTrenutneKlase[j].toString();
            brojacTrenutneKlase[j]++;
          }
        }
      }
      //vrijednosti su (indeks,x-udaljenost,y-udaljenost,sirina,visina,id,sirinapikseli,visinapikseli,dodijeljeniid,dodijeljenaklasa)
      udaljenostiSvakogElementaX.push([i, divOffset.left, divOffset.top, div[i].clientWidth, div[i].clientHeight, div[i].id, div[i].dataset.sirinapikseli, div[i].dataset.visinapikseli, div[i].dataset.dodijeljeniid, dodijeliKlasu]);
      //vrijednosti su (indeks,y-udaljenost,x-udaljenost,sirina,visina,id,sirinapikseli,visinapikseli,dodijeljeniid,dodijeljenaklasa)
      udaljenostiSvakogElementaY.push([i, divOffset.top, divOffset.left, div[i].clientWidth, div[i].clientHeight, div[i].id, div[i].dataset.sirinapikseli, div[i].dataset.visinapikseli, div[i].dataset.dodijeljeniid, dodijeliKlasu]);
    }
  }

  udaljenostiSvakogElementaX = bubbleSort(udaljenostiSvakogElementaX);
  udaljenostiSvakogElementaY = bubbleSort(udaljenostiSvakogElementaY);

  var jedanUnutarDrugog = [];
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    for (var j = 0; j < udaljenostiSvakogElementaX.length; j++) {
      var unutar = daLiJeUnutar(udaljenostiSvakogElementaX[j], udaljenostiSvakogElementaX[i]);
      if (unutar && i != j) {
        var prviElement = null;
        var drugiElement = null;
        if (udaljenostiSvakogElementaX[j][8] != "") {
          prviElement = "#" + udaljenostiSvakogElementaX[j][8];
        }
        else if (udaljenostiSvakogElementaX[j][9] != "") {
          prviElement = "." + udaljenostiSvakogElementaX[j][9];
        }

        if (udaljenostiSvakogElementaX[i][8] != "") {
          drugiElement = "#" + udaljenostiSvakogElementaX[i][8];
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          drugiElement = "." + udaljenostiSvakogElementaX[i][9];
        }
        if (prviElement != null && drugiElement != null)
          jedanUnutarDrugog.push([prviElement, drugiElement]);

        //jedanUnutarDrugog.push([udaljenostiSvakogElementaX[j][5], udaljenostiSvakogElementaX[i][5]]);
      }
      //console.log("Element " + udaljenostiSvakogElementaX[j][5] + " " + unutar + " element " + udaljenostiSvakogElementaX[i][5]);
    }
  }
  //console.log("Prvi unutar drugog "+jedanUnutarDrugog);
  for (var i = 0; i < jedanUnutarDrugog.length; i++) {
    console.log(jedanUnutarDrugog[i][0] + " je unutar " + jedanUnutarDrugog[i][1] + "\n");
  }

  var jedanDesnoOdDva = [];
  var jedanIspodDva = [];

  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    for (var j = i + 1; j < udaljenostiSvakogElementaX.length; j++) {
      var desno = daLiJeDesnoOd(udaljenostiSvakogElementaX[i], udaljenostiSvakogElementaX[j], jedanUnutarDrugog);
      if (desno) {
        //novi kod
        var prviElement = null;
        var drugiElement = null;
        if (udaljenostiSvakogElementaX[j][8] != "") {
          prviElement = "#" + udaljenostiSvakogElementaX[j][8];
        }
        else if (udaljenostiSvakogElementaX[j][9] != "") {
          prviElement = "." + udaljenostiSvakogElementaX[j][9];
        }

        if (udaljenostiSvakogElementaX[i][8] != "") {
          drugiElement = "#" + udaljenostiSvakogElementaX[i][8];
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          drugiElement = "." + udaljenostiSvakogElementaX[i][9];
        }
        if (prviElement != null && drugiElement != null)
          jedanDesnoOdDva.push([prviElement, drugiElement]);

        //jedanDesnoOdDva.push([udaljenostiSvakogElementaX[j][5], udaljenostiSvakogElementaX[i][5]]);
      }
      //console.log("U odnosu na element " + udaljenostiSvakogElementaX[i][5] + " " + desno + " element " + udaljenostiSvakogElementaX[j][5]);
    }
  }

  for (var i = 0; i < udaljenostiSvakogElementaY.length; i++) {
    for (var j = i + 1; j < udaljenostiSvakogElementaY.length; j++) {
      var ispod = daLiJeIspodOd(udaljenostiSvakogElementaY[i], udaljenostiSvakogElementaY[j], jedanUnutarDrugog);
      if (ispod) {
        //novi kod
        var prviElement = null;
        var drugiElement = null;
        if (udaljenostiSvakogElementaY[j][8] != "") {
          prviElement = "#" + udaljenostiSvakogElementaY[j][8];
        }
        else if (udaljenostiSvakogElementaY[j][9] != "") {
          prviElement = "." + udaljenostiSvakogElementaY[j][9];
        }

        if (udaljenostiSvakogElementaY[i][8] != "") {
          drugiElement = "#" + udaljenostiSvakogElementaY[i][8];
        }
        else if (udaljenostiSvakogElementaY[i][9] != "") {
          drugiElement = "." + udaljenostiSvakogElementaY[i][9];
        }
        if (prviElement != null && drugiElement != null)
          jedanIspodDva.push([prviElement, drugiElement]);

        //jedanIspodDva.push([udaljenostiSvakogElementaY[j][5], udaljenostiSvakogElementaY[i][5]]);
      }
      //console.log("U odnosu na element " + udaljenostiSvakogElementaY[i][5] + " " + ispod + " element " + udaljenostiSvakogElementaY[j][5]);
    }
  }

  var jedanLijevoOdDva = [];
  var jedanIznadDva = [];
  for (var i = 0; i < jedanDesnoOdDva.length; i++) {
    var prvi = jedanDesnoOdDva[i][0];
    var drugi = jedanDesnoOdDva[i][1];
    jedanLijevoOdDva.push([drugi, prvi]);
  }
  for (var i = 0; i < jedanIspodDva.length; i++) {
    var prvi = jedanIspodDva[i][0];
    var drugi = jedanIspodDva[i][1];
    jedanIznadDva.push([drugi, prvi]);
  }

  console.log("Prvi desno od drugog " + jedanDesnoOdDva);
  console.log("Prvi lijevo od drugog " + jedanLijevoOdDva);
  console.log("Prvi ispod drugog " + jedanIspodDva);
  console.log("Prvi iznad drugog " + jedanIznadDva);

  var dodaj = [];
  //saveText( dodaj , "filename.txt" );

  pozoviFunkciju(jedanUnutarDrugog);
  razvrstajTestove(udaljenostiSvakogElementaX, jedanUnutarDrugog, jedanDesnoOdDva, jedanLijevoOdDva, jedanIspodDva, jedanIznadDva);
  //napisiGalen(udaljenostiSvakogElementaX,udaljenostiSvakogElementaY,jedanUnutarDrugog,jedanDesnoOdDva,jedanLijevoOdDva,jedanIspodDva,jedanIznadDva);
}

function razvrstajTestove(udaljenostiSvakogElementaX, jedanUnutarDrugog, jedanDesnoOdDva, jedanLijevoOdDva, jedanIspodDva, jedanIznadDva) {
  var nizParenta = ocitajJeLiParent();
  var nizDjece = ocitajJeLiDijete();
  var nizZKoordinata = odrediZKoordinatu();
  console.log("Razvrstaj testove");
  var sviCheckboxovi = `<span class="zatvoriModalKlasa" onclick="zatvoriModal();">&times;</span><p style="text-decoration:none">&nbsp;</p>`;
  var modal = document.getElementsByClassName("sadrzajModalaKlasa")[0];
  sviCheckboxovi += `<p>Width</p>`;
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    if (udaljenostiSvakogElementaX[i][8] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>#` + udaljenostiSvakogElementaX[i][8] + `<br>`;
    }
    else if (udaljenostiSvakogElementaX[i][9] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>.` + udaljenostiSvakogElementaX[i][9] + `<br>`;
    }
  }

  sviCheckboxovi += `<p>Height</p>`;
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    if (udaljenostiSvakogElementaX[i][8] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>#` + udaljenostiSvakogElementaX[i][8] + `<br>`;
    }
    else if (udaljenostiSvakogElementaX[i][9] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>.` + udaljenostiSvakogElementaX[i][9] + `<br>`;
    }
  }

  sviCheckboxovi += `<p>Inside</p>`;
  for (var i = 0; i < jedanUnutarDrugog.length; i++) {
    sviCheckboxovi += `<input type="checkbox" checked>` + jedanUnutarDrugog[i][0] + ` inside ` + jedanUnutarDrugog[i][1] + `<br>`;
  }

  sviCheckboxovi += `<p>Left</p>`;
  for (var i = 0; i < jedanLijevoOdDva.length; i++) {
    sviCheckboxovi += `<input type="checkbox" checked>` + jedanLijevoOdDva[i][0] + ` left of ` + jedanLijevoOdDva[i][1] + `<br>`;
  }

  sviCheckboxovi += `<p>Right</p>`;
  for (var i = 0; i < jedanDesnoOdDva.length; i++) {
    sviCheckboxovi += `<input type="checkbox" checked>` + jedanDesnoOdDva[i][0] + ` right of ` + jedanDesnoOdDva[i][1] + `<br>`;
  }

  sviCheckboxovi += `<p>Above</p>`;
  for (var i = 0; i < jedanIznadDva.length; i++) {
    sviCheckboxovi += `<input type="checkbox" checked>` + jedanIznadDva[i][0] + ` above ` + jedanIznadDva[i][1] + `<br>`;
  }

  sviCheckboxovi += `<p>Below</p>`;
  for (var i = 0; i < jedanIspodDva.length; i++) {
    sviCheckboxovi += `<input type="checkbox" checked>` + jedanIspodDva[i][0] + ` below ` + jedanIspodDva[i][1] + `<br>`;
  }

  sviCheckboxovi += `<p>Visible</p>`;
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    if (udaljenostiSvakogElementaX[i][8] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>#` + udaljenostiSvakogElementaX[i][8] + `<br>`;
    }
    else if (udaljenostiSvakogElementaX[i][9] != "") {
      sviCheckboxovi += `<input type="checkbox" checked>.` + udaljenostiSvakogElementaX[i][9] + `<br>`;
    }
  }

  /*sviCheckboxovi+=`<p>Parent</p>`;
  for(var i=0;i<nizParenta.length;i++){
    sviCheckboxovi+=`<input type="checkbox" checked>`+nizParenta[i][0]+` is parent of `+nizParenta[i][1]+`<br>`;
  }*/

  /*sviCheckboxovi+=`<p>Child</p>`;
  for(var i=0;i<nizDjece.length;i++){
    sviCheckboxovi+=`<input type="checkbox" checked>`+nizDjece[i][0]+` is child of `+nizDjece[i][1]+`<br>`;
  }*/

  /*sviCheckboxovi+=`<p>Z-index</p>`;
  for(var i=0;i<nizZKoordinata.length;i++){
    sviCheckboxovi+=`<input type="checkbox" checked>`+nizZKoordinata[i][0]+` is on `+nizZKoordinata[i][1]+`<br>`;
  }*/

  modal.innerHTML = sviCheckboxovi + `<p style="text-decoration:none">&nbsp;</p><button onclick="napisiGalen();" title="Creates and saves Galen tests on local computer">Save tests</Button>`;
  udaljenostiSvakogElementaXGlobalni = udaljenostiSvakogElementaX;
  jedanUnutarDrugogGlobalni = jedanUnutarDrugog;
  jedanDesnoOdDvaGlobalni = jedanDesnoOdDva;
  jedanLijevoOdDvaGlobalni = jedanLijevoOdDva;
  jedanIspodDvaGlobalni = jedanIspodDva;
  jedanIznadDvaGlobalni = jedanIznadDva;

  jedanRoditeljDvaGlobalni = nizParenta;
  jedanDijeteDvaGlobalni = nizDjece;
  zKoordinataGlobalni = nizZKoordinata;
  //console.log("Globalni"+udaljenostiSvakogElementaX);
}

//funkcija koja poredi date elemente sa nizom elemenata koji su jedan unutar drugog
function pretraziDaLiSuJedanUnutarDrugog(element1Id, element2Id, jedanUnutarDrugog) {
  for (var i = 0; i < jedanUnutarDrugog.length; i++) {
    if ((jedanUnutarDrugog[i][0] == element1Id && jedanUnutarDrugog[i][1] == element2Id) || (jedanUnutarDrugog[i][0] == element2Id && jedanUnutarDrugog[i][1] == element1Id)) {
      return true;
    }
  }
  return false;
}

//funkcija za odredjivanje da li je element2 desno od element1
function daLiJeDesnoOd(element1, element2, jedanUnutarDrugog) {
  console.log("Da li je desno od " + element1);
  if (element1[2] <= (element2[2] + element2[4]) && (element2[2]) <= (element1[2] + element1[4]) && !pretraziDaLiSuJedanUnutarDrugog(element1[5], element2[5], jedanUnutarDrugog) && element2[1] >= (element1[1] + element1[3])) {
    return true;
  }
  return false;
}

function daLiJeIspodOd(element1, element2, jedanUnutarDrugog) {
  if (element1[2] <= (element2[2] + element2[3]) && (element2[2]) <= (element1[2] + element1[3]) && !pretraziDaLiSuJedanUnutarDrugog(element1[5], element2[5], jedanUnutarDrugog) && element2[1] >= (element1[1] + element1[4])) {
    return true;
  }
  return false;
}

function daLiJeUnutar(element1, element2) {
  if (element1[1] >= element2[1] && (element1[1] + element1[3]) <= (element2[1] + element2[3]) && element1[2] >= element2[2] && (element1[2] + element1[4]) <= (element2[2] + element2[4])) {
    return true;
  }
  return false;
}

udaljenostiSvakogElementaXGlobalni = [];
jedanUnutarDrugogGlobalni = [];
jedanDesnoOdDvaGlobalni = [];
jedanLijevoOdDvaGlobalni = [];
jedanIspodDvaGlobalni = [];
jedanIznadDvaGlobalni = [];
//!!!!!pozivanje galena: cd C:\Users\pc\Documents\Zavrsi Rad\Galen testovi primjeri pocetni
//galen check pocetniTest.gspec --url file:///C:/Users/pc/Downloads/Mockup%20tool.html --size 1600x900 --htmlreport "Izvjestaj"
//galen check pocetniTest.gspec --url file:///C:/Users/cico/Downloads/Documents/Zavrsni%20Rad%20Literatura/Mockupi/The%20GTK%20Project.html --size 1600x900 --htmlreport "Izvjestaj"
//galen check galenTestsDamirov.gspec --url https://www.gtk.org/?fbclid=IwAR3pvUlKcpgC9KoQTxXlf_knNpHXa6-M3qmgODrNDL7c8eIiZgnq9JUBVHU --size 1600x900 --htmlreport "Izvjestaj"
//galen check galenTestsGTK.gspec --url https://www.gtk.org/?fbclid=IwAR3pvUlKcpgC9KoQTxXlf_knNpHXa6-M3qmgODrNDL7c8eIiZgnq9JUBVHU --size 1600x900 --htmlreport "Izvjestaj"

//Zerinin galen
//galen check galenTestsZerinin.gspec --url http://bugmenot.com/?fbclid=IwAR1kPL6WG9EmmFvqkPHNB4lQ8Rrt6bYL8_6GpjDAudQ514X8rLe7jrH50H0 --size 1600x900 --htmlreport "Izvjestaj"
//galen check galenTestsBugMeNot.gspec --url http://bugmenot.com/?fbclid=IwAR1kPL6WG9EmmFvqkPHNB4lQ8Rrt6bYL8_6GpjDAudQ514X8rLe7jrH50H0 --size 1600x900 --htmlreport "Izvjestaj"

//Edinin galen
//galen check galenTestsEdinin.gspec --url https://cloudconvert.com/?fbclid=IwAR2hB9QhHrz78ojPmPtB94IohW8R8DdlU-8Ma56bYFfouaLhtssLwm7I0N4 --size 1600x900 --htmlreport "Izvjestaj"
//galen check galenTestsConvertAnythingToAnything.gspec --url https://cloudconvert.com/?fbclid=IwAR2hB9QhHrz78ojPmPtB94IohW8R8DdlU-8Ma56bYFfouaLhtssLwm7I0N4 --size 1600x900 --htmlreport "Izvjestaj"

//napisi Galen pravila i testove
function napisiGalen() {
  var udaljenostiSvakogElementaX = udaljenostiSvakogElementaXGlobalni;
  var jedanUnutarDrugog = jedanUnutarDrugogGlobalni;
  var jedanDesnoOdDva = jedanDesnoOdDvaGlobalni;
  var jedanLijevoOdDva = jedanLijevoOdDvaGlobalni;
  var jedanIspodDva = jedanIspodDvaGlobalni;
  var jedanIznadDva = jedanIznadDvaGlobalni;
  console.log("X" + udaljenostiSvakogElementaX);
  var izuzeci = [];
  var checkboxovi = document.getElementsByClassName("sadrzajModalaKlasa")[0].getElementsByTagName("input");
  for (var i = 0; i < checkboxovi.length; i++) {
    if (!checkboxovi[i].checked) {
      izuzeci.push(i);
    }
  }
  console.log("Izuzeci su " + izuzeci);
  var ukupniBrojac = 0;
  var galen = "";
  var objekti = "@objects\n\tbody body\n";
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    if (udaljenostiSvakogElementaX[i][8] != "") {
      objekti += "\t" + udaljenostiSvakogElementaX[i][8] + " #" + udaljenostiSvakogElementaX[i][8] + "\n";
    }
    else if (udaljenostiSvakogElementaX[i][9] != "") {
      var razdvoji = udaljenostiSvakogElementaX[i][9].split(".");
      var broj = razdvoji[razdvoji.length - 1];
      var ostatak="";
      for(var j=0;j<razdvoji.length-1;j++){
        ostatak+=razdvoji[j];
        if(j!=razdvoji.length-2){
          ostatak+=" ";
        }
      }
      objekti += "\t" + udaljenostiSvakogElementaX[i][9] + " xpath //*[@class=\'" + ostatak + "\'][" + broj + "]\n";
    }
  }
  console.log(objekti);
  var ispisi = true;
  var sekcije = "";
  var sekcijaSirine = "\n= Sirina section =\n";
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi) {
      if (udaljenostiSvakogElementaX[i][6] == "true") {
        if (udaljenostiSvakogElementaX[i][8] != "") {
          sekcijaSirine += "\t" + udaljenostiSvakogElementaX[i][8] + ":\n" + "\t\twidth ~" + udaljenostiSvakogElementaX[i][3] + "px\n";
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          sekcijaSirine += "\t" + udaljenostiSvakogElementaX[i][9] + ":\n" + "\t\twidth ~" + udaljenostiSvakogElementaX[i][3] + "px\n";
        }

      }
      else if (udaljenostiSvakogElementaX[i][6] == "false") {
        var parent = document.getElementById(udaljenostiSvakogElementaX[i][5]).offsetParent || document.getElementById(udaljenostiSvakogElementaX[i][5]);
        if (udaljenostiSvakogElementaX[i][8] != "") {
          sekcijaSirine += "\t" + udaljenostiSvakogElementaX[i][8] + ":\n" + "\t\twidth " + (((udaljenostiSvakogElementaX[i][3] / parent.offsetWidth) * 100 - 1).toFixed(0)) + " to " + (((udaljenostiSvakogElementaX[i][3] / parent.offsetWidth) * 100 + 1).toFixed(0)) + " % of body/width\n";
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          sekcijaSirine += "\t" + udaljenostiSvakogElementaX[i][9] + ":\n" + "\t\twidth " + (((udaljenostiSvakogElementaX[i][3] / parent.offsetWidth) * 100 - 1).toFixed(0)) + " to " + (((udaljenostiSvakogElementaX[i][3] / parent.offsetWidth) * 100 + 1).toFixed(0)) + " % of body/width\n";
        }
      }
    }
    ukupniBrojac++;
  }
  sekcije += sekcijaSirine;
  var sekcijaVisine = "= Visina section =\n";
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi) {
      if (udaljenostiSvakogElementaX[i][7] == "true") {
        if (udaljenostiSvakogElementaX[i][8] != "") {
          sekcijaVisine += "\t" + udaljenostiSvakogElementaX[i][8] + ":\n" + "\t\theight ~" + udaljenostiSvakogElementaX[i][4] + "px\n";
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          sekcijaVisine += "\t" + udaljenostiSvakogElementaX[i][9] + ":\n" + "\t\theight ~" + udaljenostiSvakogElementaX[i][4] + "px\n";
        }
      }
      else if (udaljenostiSvakogElementaX[i][7] == "false") {
        var parent = document.getElementById(udaljenostiSvakogElementaX[i][5]).offsetParent || document.getElementById(udaljenostiSvakogElementaX[i][5]);
        if (udaljenostiSvakogElementaX[i][8] != "") {
          sekcijaVisine += "\t" + udaljenostiSvakogElementaX[i][8] + ":\n" + "\t\theight " + (((udaljenostiSvakogElementaX[i][4] / parent.offsetHeight) * 100 - 1).toFixed(0)) + " to " + (((udaljenostiSvakogElementaX[i][4] / parent.offsetHeight) * 100 + 1).toFixed(0)) + " % of body/height\n";
        }
        else if (udaljenostiSvakogElementaX[i][9] != "") {
          sekcijaVisine += "\t" + udaljenostiSvakogElementaX[i][9] + ":\n" + "\t\theight " + (((udaljenostiSvakogElementaX[i][4] / parent.offsetHeight) * 100 - 1).toFixed(0)) + " to " + (((udaljenostiSvakogElementaX[i][4] / parent.offsetHeight) * 100 + 1).toFixed(0)) + " % of body/height\n";
        }
      }
    }

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaVisine;

  var sekcijaUnutar = "= Unutar section =\n";
  for (var i = 0; i < jedanUnutarDrugog.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi)
      sekcijaUnutar += "\t" + jedanUnutarDrugog[i][0].substring(1) + ":\n" + "\t\tinside " + jedanUnutarDrugog[i][1].substring(1) + "\n";

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaUnutar;

  var sekcijaLijevo = "= Lijevo section =\n";
  for (var i = 0; i < jedanLijevoOdDva.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi)
      sekcijaLijevo += "\t" + jedanLijevoOdDva[i][0].substring(1) + ":\n" + "\t\tleft-of " + jedanLijevoOdDva[i][1].substring(1) + "\n";

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaLijevo;

  var sekcijaDesno = "= Desno section =\n";
  for (var i = 0; i < jedanDesnoOdDva.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi)
      sekcijaDesno += "\t" + jedanDesnoOdDva[i][0].substring(1) + ":\n" + "\t\tright-of " + jedanDesnoOdDva[i][1].substring(1) + "\n";

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaDesno;

  var sekcijaIznad = "= Iznad section =\n";
  for (var i = 0; i < jedanIznadDva.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi)
      sekcijaIznad += "\t" + jedanIznadDva[i][0].substring(1) + ":\n" + "\t\tabove " + jedanIznadDva[i][1].substring(1) + "\n";

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaIznad;

  var sekcijaIspod = "= Ispod section =\n";
  for (var i = 0; i < jedanIspodDva.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi)
      sekcijaIspod += "\t" + jedanIspodDva[i][0].substring(1) + ":\n" + "\t\tbelow " + jedanIspodDva[i][1].substring(1) + "\n";

    ukupniBrojac++;
  }
  sekcije += "\n" + sekcijaIspod;

  var sekcijaVisible = "= Visible section =\n";
  for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
    ispisi = true;
    for (var j = 0; j < izuzeci.length; j++) {
      if (izuzeci[j] == ukupniBrojac) {
        ispisi = false;
      }
    }
    if (ispisi) {
      if (udaljenostiSvakogElementaX[i][8] != "") {
        sekcijaVisible += "\t" + udaljenostiSvakogElementaX[i][8] + ":\n\t\tvisible\n";
      }
      else if (udaljenostiSvakogElementaX[i][9] != "") {
        sekcijaVisible += "\t" + udaljenostiSvakogElementaX[i][9] + ":\n\t\tvisible\n";
      }
    }

    ukupniBrojac++;
  }
  sekcije += sekcijaVisible;

  /*var sekcijaContains="= Contains section =\n\tglavni:\n\t\tcontains ";
  for(var i=0;i<udaljenostiSvakogElementaX.length;i++){
    sekcijaContains+=udaljenostiSvakogElementaX[i][5];
    if(i!=udaljenostiSvakogElementaX.length-1){
      sekcijaContains+=", ";
    }
  }
  sekcije+=sekcijaContains;*/

  /*var nizParenta = ocitajJeLiParent();
  var sekcijaParent="= Roditelj section =\n";
  for(var i=0;i<nizParenta.length;i++){
    ispisi=true;
    for(var j=0;j<izuzeci.length;j++){
      if(izuzeci[j]==ukupniBrojac){
        ispisi=false;
      } 
    }
    if(ispisi)
    sekcijaParent+="\t"+nizParenta[i][0]+":\n"+"\t\tparent of "+ nizParenta[i][1]+"\n";
    ukupniBrojac++;
  }
  sekcije+="\n"+sekcijaParent;*/

  /*var nizDjece = ocitajJeLiDijete();
  var sekcijaDjece="= Dijete i roditelj section =\n";
  for(var i=0;i<nizDjece.length;i++){
    ispisi=true;
    for(var j=0;j<izuzeci.length;j++){
      if(izuzeci[j]==ukupniBrojac){
        ispisi=false;
      } 
    }
    if(ispisi)
    sekcijaDjece+="\t"+nizDjece[i][0]+":\n"+"\t\tinside parent "+ nizDjece[i][1]+"\n";
    ukupniBrojac++;
  }
  sekcije+="\n"+sekcijaDjece;*/

  /*var nizZKoordinata = odrediZKoordinatu();
  var sekcijaKoordinata="= Z koordinata section =\n";
  for(var i=0;i<nizZKoordinata.length;i++){
    ispisi=true;
    for(var j=0;j<izuzeci.length;j++){
      if(izuzeci[j]==ukupniBrojac){
        ispisi=false;
      } 
    }
    if(ispisi)
    sekcijaKoordinata+="\t"+nizZKoordinata[i][0]+":\n"+"\t\ton "+ nizZKoordinata[i][1]+"\n";
    ukupniBrojac++;
  }
  sekcije+="\n"+sekcijaKoordinata;*/


  galen = objekti + sekcije;
  console.log(galen);
  download(galen, "galenTests.gspec", "txt");
}

function ocitajJeLiParent() {

  var glavni = document.getElementById("glavni");

  sviSviElementi = glavni.getElementsByTagName('div');
  var parenti = [];

  for (var i = 0; i < sviSviElementi.length; i++) {
    var pomocni = [];
    for (var j = 0; j < sviSviElementi[i].getElementsByTagName('div').length; j++) {
      pomocni.push(sviSviElementi[i].getElementsByTagName('div')[j].id);
    }
    if (pomocni.length != 0) parenti.push([sviSviElementi[i].id, pomocni]);
  }
  return parenti;

}

var nizJedanUnutarDrugog = [];
function odrediZKoordinatu() {
  /*console.log("ispisi tu");
  var svaDjeca = glavni.getElementsByTagName('div');
  console.log(svaDjeca);
  var zKoordinata = [];

  for(var i=0;i<nizJedanUnutarDrugog.length;i++){

    var prvi = [], drugi = [];
    for(var j=0; j<svaDjeca.length; j++){
        if(nizJedanUnutarDrugog[i][0] == svaDjeca[j].id) prvi.push(svaDjeca[j],j);
        if(nizJedanUnutarDrugog[i][1] == svaDjeca[j].id) drugi.push(svaDjeca[j],j);
    }
  
    console.log(prvi[0].style.zIndex,drugi[0].style.zIndex);

    if(prvi[0].style.zIndex == drugi[0].style.zIndex){
      if(prvi[1]>drugi[1]){
        zKoordinata.push([prvi[0].id,drugi[0].id]);
      }
      else{
        zKoordinata.push([drugi[0].id, prvi[0].id]);
      }
    }
    else if(prvi[0].style.zIndex > drugi[0].style.zIndex){
      zKoordinata.push([prvi[0].id,drugi[0].id]);
    }
    else{
      zKoordinata.push([drugi[0].id, prvi[0].id]);
    }
  }

  console.log(zKoordinata);
  return zKoordinata;*/
}

function pozoviFunkciju(unutarDrugog) {
  nizJedanUnutarDrugog = unutarDrugog;
}

function ocitajJeLiDijete() {
  sviSviElementi = glavni.getElementsByTagName('div');

  var djeca = [];
  for (var i = 0; i < sviSviElementi.length; i++) {
    var roditelj = sviSviElementi[i].parentNode.id;
    djeca.push([sviSviElementi[i].id, roditelj]);
  }
  return djeca;
}