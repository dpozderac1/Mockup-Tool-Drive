console.log("Tu sam");
var xOse = [];
var yOse = [];
function ocitajSveOse(trenutniDiv) {
  var sviDivovi = document.getElementById("glavni").getElementsByTagName("div");
  xOse = [];
  yOse = [];
  for (var i = 0; i < sviDivovi.length; i++) {
    if (sviDivovi[i].id != trenutniDiv.id) {
      var divOffset = offset(sviDivovi[i]);
      //vrijednosti su (indeks,x-udaljenost,y-udaljenost,sirina,visina,id) [i, divOffset.left, divOffset.top, div[i].offsetWidth, div[i].offsetHeight, div[i].id]
      xOse.push(divOffset.left);
      xOse.push(divOffset.left + sviDivovi[i].offsetWidth);

      yOse.push(divOffset.top);
      yOse.push(divOffset.top + sviDivovi[i].offsetHeight);
    }
  }
}

var trenutniMisX = 0;
var trenutniMisY = 0;
var xMis = 0;
var yMis = 0;

document.getElementById("glavni").addEventListener("mousemove", function (event) {
  xMis = event.clientX;
  yMis = event.clientY;
});

document.getElementById("glavni").addEventListener("mousedown", function (event) {
  if (event.target.id != "glavni") {
    trenutniMisX = xMis;
    trenutniMisY = yMis;
  }
  console.log("Klinut je " + event.target.id);
  console.log(trenutniMisX);
});

var brojacDivova = 0;
var glavniDio = document.getElementById("glavni");
glavniDio.classList.add("dropzone");

var searchText = document.getElementById("searchText");
var listaMockupa = document.getElementById("listaMockupa");

var prethodnoOznaceniElement = null;

var strelicaToolbara = document.getElementById("strelicaToolbara");

var oznaceniElementi = [];

var brojacOznacenih = 0;

//EventListener na elementima diva glavni
glavniDio.addEventListener("click", function (e) {
  console.log("trenutno oznaceni element " + e.target.nodeName);
  if (e.target && e.target.nodeName == "DIV" && e.target.id != "glavni" && e.target.id != "") {
    trenutnoOznaceniElement = e.target;
    if (trenutnoOznaceniElement.dataset.sirinapikseli == "true") {
      selectSirina.value = selectSirina.options[0].value;
    }
    else {
      selectSirina.value = selectSirina.options[1].value;
    }

    if (selectSirina.options[selectSirina.selectedIndex].value == "%") {
      var parent = trenutnoOznaceniElement.offsetParent || trenutnoOznaceniElement;
      widthTextBox.value = ((trenutnoOznaceniElement.offsetWidth / parent.offsetWidth) * 100).toFixed(0);

    }
    else if (selectSirina.options[selectSirina.selectedIndex].value == "px") {
      widthTextBox.value = trenutnoOznaceniElement.clientWidth;
    }


    if (trenutnoOznaceniElement.dataset.visinapikseli == "true") {
      selectVisina.value = selectVisina.options[0].value;
    }
    else {
      selectVisina.value = selectVisina.options[1].value;
    }

    if (selectVisina.options[selectVisina.selectedIndex].value == "%") {
      var parent = trenutnoOznaceniElement.offsetParent || trenutnoOznaceniElement;
      heightTextBox.value = ((trenutnoOznaceniElement.offsetHeight / parent.offsetHeight) * 100).toFixed(0);
    }
    else if (selectVisina.options[selectVisina.selectedIndex].value == "px") {
      heightTextBox.value = trenutnoOznaceniElement.clientHeight;
    }

    //prikazivanje kontura elementa kad je element oznacen u divu glavni
    trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
    if (trenutnoOznaceniElement.parentNode.id != "glavni") {
      trenutnoOznaceniElement.parentNode.style.boxShadow = "0 0 0 1px #6c727a";
    }
    vertikalnaUdaljenost.disabled = true;
    vertikalnaUdaljenostSelect.disabled = true;
    horizontalnaUdaljenost.disabled = true;
    horizontalnaUdaljenostSelect.disabled = true;
    horizontalnaUdaljenost.value = "";
    vertikalnaUdaljenost.value = "";

    //trenutnoOznaceniElement.style.border="dashed #20c997";

    //prethodno oznaceni element
    if (/*prethodnoOznaceniElement != null && prethodnoOznaceniElement != undefined &&*/ prethodnoOznaceniElement != trenutnoOznaceniElement) {
      if (event.shiftKey) {
        trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
        if (prethodnoOznaceniElement != null && trenutnoOznaceniElement != null) {
          odrediUdaljenost();
        }
      }
      else {
        if (prethodnoOznaceniElement != null && prethodnoOznaceniElement != undefined) {
          //var svaDjeca = document.getElementById("glavni").childNodes;
          var svaDjeca = document.getElementById("glavni").getElementsByTagName("div");
          for (var i = 0; i < svaDjeca.length; i++) {
            svaDjeca[i].style.boxShadow = "";
          }
          trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
          if (trenutnoOznaceniElement.parentNode.id != "glavni") {
            trenutnoOznaceniElement.parentNode.style.boxShadow = "0 0 0 1px #6c727a";
          }
        }
      }
    }
    else {
      if (event.shiftKey) {
        if (brojacOznacenih % 2 == 0) {
          trenutnoOznaceniElement.style.boxShadow = "";
          odrediUdaljenost();
          brojacOznacenih++;
        }
        else {
          trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
          odrediUdaljenost();
          brojacOznacenih = 0;
        }
      }
    }
    prethodnoOznaceniElement = trenutnoOznaceniElement;
    console.log("Prethodno je oznacen " + prethodnoOznaceniElement);

    //uklanjanje prikaza streliceToolbara
    strelicaToolbara.style.display = "none";

    //prikazivanje opcija sa desne strane
    desniToolbar.style.display = "inline";
    desniToolbar.style.zIndex = vrijednostZ + 1;
    onemoguciKlikUToolbaru(false);
    naslovOpcija.innerHTML = trenutnoOznaceniElement.title;
    if (trenutnoOznaceniElement.getElementsByTagName("li").length != 0) {
      fontElementa.value = window.getComputedStyle(trenutnoOznaceniElement.getElementsByTagName("li")[0]).fontSize.slice(0, -2);
    }
    else if (trenutnoOznaceniElement.getElementsByTagName("td").length != 0) {
      fontElementa.value = window.getComputedStyle(trenutnoOznaceniElement.getElementsByTagName("td")[0]).fontSize.slice(0, -2);
    }
    else {
      fontElementa.value = window.getComputedStyle(trenutnoOznaceniElement.getElementsByTagName("p")[0]).fontSize.slice(0, -2);
    }

    //prikaz id-a
    unosIdTrenutnogElementa.value = trenutnoOznaceniElement.dataset.dodijeljeniid;
    unosKlaseTrenutnogElementa.value = trenutnoOznaceniElement.dataset.dodijeljenaklasa;

    //omogucavanje dodatnih elemenata Toolbara
    var sveDodatneOpcije = document.getElementById("listaDodatnihOpcija").getElementsByTagName("li");
    for (var i = 0; i < sveDodatneOpcije.length; i++) {
      sveDodatneOpcije[i].style.display = "none";
    }
    if (trenutnoOznaceniElement.title == "Table") {
      redovi.style.display = "block";
      kolone.style.display = "block";
      var brojRedovaTabele = trenutnoOznaceniElement.getElementsByTagName("tr").length;
      document.getElementById("brojRedova").value = brojRedovaTabele;
      document.getElementById("brojKolona").value = (trenutnoOznaceniElement.getElementsByTagName("td").length + trenutnoOznaceniElement.getElementsByTagName("th").length) / brojRedovaTabele;
    }
    else if (trenutnoOznaceniElement.title == "Tabs") {
      //trenutnoOznaceniElement.getElementsByTagName("li")[1].setAttribute("data-oznacen","1");
      //console.log(trenutnoOznaceniElement.getElementsByTagName("li")[1].dataset.oznacen);
      tabovi.style.display = "block";
      trentunoOznaceniTab.style.display = "block";
      document.getElementById("brojTabova").value = trenutnoOznaceniElement.getElementsByTagName("li").length;
      for (var i = 0; i < trenutnoOznaceniElement.getElementsByTagName("li").length; i++) {
        if (trenutnoOznaceniElement.getElementsByTagName("li")[i].dataset.oznacen == "1") {
          document.getElementById("brojTrenutnoOznacenogTaba").value = i + 1;
          break;
        }
      }
    }
    else if (trenutnoOznaceniElement.title == "Radio Button") {
      radioDugmadi.style.display = "block";
      trenutnoOznacenoRadioDugme.style.display = "block";
      document.getElementById("brojOpcijaRadioDugmadi").value = trenutnoOznaceniElement.getElementsByTagName("li").length;
      for (var i = 0; i < trenutnoOznaceniElement.getElementsByTagName("li").length; i++) {
        if (trenutnoOznaceniElement.getElementsByTagName("li")[i].className == "oznacena") {
          document.getElementById("brojTrenutnoOznacenogRadioDugmeta").value = i + 1;
          break;
        }
      }
    }

    else if (trenutnoOznaceniElement.title == "Checkbox") {
      checkboxovi.style.display = "block";
      oznaceniCheckboxovi.style.display = "block";
      neoznaceniCheckboxovi.style.display = "block";
      document.getElementById("brojOpcijaCheckboxova").value = trenutnoOznaceniElement.getElementsByTagName("li").length;
      for (var i = 0; i < trenutnoOznaceniElement.getElementsByTagName("li").length; i++) {
        if (trenutnoOznaceniElement.getElementsByTagName("li")[i].className == "checkboxOznacena") {
          document.getElementById("brojTrenutnoOznacenogCheckboxa").value = i + 1;
          break;
        }
      }
    }

    else if (trenutnoOznaceniElement.title == "Vertical List") {
      vertikalnaLista.style.display = "block";
      document.getElementById("brojOpcijaVertikalneListe").value = trenutnoOznaceniElement.getElementsByTagName("li").length;
    }
    else if (trenutnoOznaceniElement.title == "Horizontal List") {
      horizontalnaListaElemenata.style.display = "block";
      document.getElementById("brojOpcijaHorizontalneListe").value = trenutnoOznaceniElement.getElementsByTagName("li").length;
    }

    //namjesti poravnanje
    if (trenutnoOznaceniElement.dataset.poravnanje == "lijevo") {
      document.getElementById("radio1").click();
    }
    else if (trenutnoOznaceniElement.dataset.poravnanje == "centar") {
      document.getElementById("radio2").click();
    }
    else if (trenutnoOznaceniElement.dataset.poravnanje == "desno") {
      document.getElementById("radio3").click();
    }
    else if (trenutnoOznaceniElement.dataset.poravnanje == "justify") {
      document.getElementById("radio4").click();
    }
  }
  else if (e.target && e.target.id != "glavni") {
    var roditelj = daLiMuJeRoditeljDiv(e.target);
    console.log("\nNasao sam roditelja\t" + roditelj);
    if (roditelj != null) {
      trenutnoOznaceniElement = document.getElementById(roditelj);
      if (prethodnoOznaceniElement != trenutnoOznaceniElement) {
        if (event.shiftKey) {
          trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
          if (prethodnoOznaceniElement != null && trenutnoOznaceniElement != null) {
            odrediUdaljenost();
          }
        }
        else {
          if (prethodnoOznaceniElement != null && prethodnoOznaceniElement != undefined) {
            var svaDjeca = document.getElementById("glavni").getElementsByTagName("div");
            for (var i = 0; i < svaDjeca.length; i++) {
              svaDjeca[i].style.boxShadow = "";
            }
            trenutnoOznaceniElement.style.boxShadow = "0 0 0 2px #20c997";
            if (trenutnoOznaceniElement.parentNode.id != "glavni") {
              trenutnoOznaceniElement.parentNode.style.boxShadow = "0 0 0 1px #6c727a";
            }
          }
          //else {
          trenutnoOznaceniElement.click();
          //}
        }
      }
      else {
        if (event.shiftKey) {
          if (trenutnoOznaceniElement.style.boxShadow == "rgb(32, 201, 151) 0px 0px 0px 2px") {
            trenutnoOznaceniElement.style.boxShadow = "";
            odrediUdaljenost();
          }
          else {
            trenutnoOznaceniElement.click();
            odrediUdaljenost();
          }
        }
        else trenutnoOznaceniElement.click();
      }
      prethodnoOznaceniElement = trenutnoOznaceniElement;


      //namjesti poravnanje
      if (trenutnoOznaceniElement.dataset.poravnanje == "lijevo") {
        document.getElementById("radio1").click();
      }
      else if (trenutnoOznaceniElement.dataset.poravnanje == "centar") {
        document.getElementById("radio2").click();
      }
      else if (trenutnoOznaceniElement.dataset.poravnanje == "desno") {
        document.getElementById("radio3").click();
      }
      else if (trenutnoOznaceniElement.dataset.poravnanje == "justify") {
        document.getElementById("radio4").click();
      }
    }
    else {
      trenutnoOznaceniElement = null;
      prethodnoOznaceniElement = null;
    }
  }
  else {
    trenutnoOznaceniElement = null;
    inputElement.value = "";
    inputElementVisina.value = "";
    selectSirina.value = selectSirina.options[0].value;
    selectVisina.value = selectVisina.options[0].value;
    desniToolbar.style.display = "none";
    horizontalnaUdaljenost.disabled = true;
    horizontalnaUdaljenostSelect.disabled = true;
    vertikalnaUdaljenost.disabled = true;
    vertikalnaUdaljenostSelect.disabled = true;
    horizontalnaUdaljenost.value = "";
    vertikalnaUdaljenost.value = "";
    if (prethodnoOznaceniElement != null && prethodnoOznaceniElement != undefined && prethodnoOznaceniElement != trenutnoOznaceniElement) {
      var svaDjeca = document.getElementById("glavni").getElementsByTagName("div");
      for (var i = 0; i < svaDjeca.length; i++) {
        svaDjeca[i].style.boxShadow = "";
      }
      prethodnoOznaceniElement.style.boxShadow = "";
    }
    prethodnoOznaceniElement = null;
    //prikazi strelicuToolbara
    strelicaToolbara.style.display = "flex";
    strelicaToolbara.style.zIndex = vrijednostZ + 1;
  }
  console.log("trenutno je oznacen " + trenutnoOznaceniElement);
}, false);

//funkcija koja provjerava da li roditelj ima id "div" i vraca id roditelja
function daLiMuJeRoditeljDiv(dijete) {
  var node = dijete.parentNode;
  while (node != null) {
    console.log("OKURRRRRRRRRRRRRRRR\t\t" + node.id.substring(0, 3));
    if (node.id == "glavni") {
      return null;
    }
    else if (node.dataset.inicijalniid != undefined && node.dataset.inicijalniid.substring(0, 3) == "div") {
      return node.id;
    }
    node = node.parentNode;
  }
  return null;
}


//Klik na element liste Mockupa sa lijeve stranu (tj. u divu lijevo)
document.getElementById("listaMockupa").addEventListener("click", function (e) {

  e.stopPropagation();
  e.preventDefault();
  console.log("Kliknut je   " + e.target);


  if (e.target && e.target.nodeName == "BUTTON") {
    if (e.target.getAttribute("id") == "searchButton") {
      search();
    }
    else if (e.target.getAttribute("id") == "deleteButton") {
      deleteSearchText();
    }

  }

  else if (e.target && e.target.nodeName == "DIV") {
    //Dupliciranje elementa liste
    e2 = e.target.cloneNode(true);
    //Dodavanje mogucnosti drag and drop na element liste kako bi ga bilo moguce pomjerati u glavnom divu
    e2.classList.add("resize-drag");

    //Pokusaj drag and dropa
    //e2.classList.add("draggable");
    //Ovo sam dodao jer je izbacivalo neki warning ali ne pravi nikakvu razliku, valjda je ovo vezano za touch-screen dislay
    e2.setAttribute("style", "touch-action:none;");
    e2.setAttribute("style", "margin-left:0px;margin-top:0px;margin-bottom:0px");

    //Dimenzije glavnog diva
    var rect = glavniDio.getBoundingClientRect();

    //pozicioniranje Mockupa
    e2.style.position = "absolute";
    e2.style.left = rect.left;
    e2.style.top = rect.top;

    e2.style.width = e.target.offsetWidth + "px";
    e2.style.height = e.target.offsetHeight + "px";

    //Omogucavanje editovanja paragrafa na dupliClick
    if (e2.getElementsByTagName("p")[0] != null) {
      for (var i = 0; i < e2.getElementsByTagName("p").length; i++) {
        //e2.getElementsByTagName("p")[i].contentEditable = "true";
        e2.getElementsByTagName("p")[i].setAttribute("onclick", "this.contentEditable=false;");
        e2.getElementsByTagName("p")[i].setAttribute("ondblclick", "this.contentEditable=true;this.focus();");
        e2.getElementsByTagName("p")[i].style.pointerEvents = "auto";
      }
    }

    //Omogucavanje editovanja tabele na dupliClick
    if (e2.getElementsByTagName("table")[0] != null) {
      e2.getElementsByTagName("table")[0].setAttribute("onclick", "this.contentEditable=false;");
      e2.getElementsByTagName("table")[0].setAttribute("ondblclick", "this.contentEditable=true;this.focus();");
      e2.getElementsByTagName("table")[0].style.pointerEvents = "auto";
    }

    //Omogucavanje editovanja li na dupliClick
    if (e2.getElementsByTagName("li")[0] != null) {
      for (var i = 0; i < e2.getElementsByTagName("li").length; i++) {
        e2.getElementsByTagName("li")[i].setAttribute("onclick", "this.contentEditable=false;");
        e2.getElementsByTagName("li")[i].setAttribute("ondblclick", "this.contentEditable=true;this.focus();");
        e2.getElementsByTagName("li")[i].style.pointerEvents = "auto";
      }
    }

    //pretrazi sve id-eve
    var sviIdKojiImajuDiv = [];
    for (var i = 0; i < glavniDio.getElementsByTagName("div").length; i++) {
      if (glavniDio.getElementsByTagName("div")[i].id != null && glavniDio.getElementsByTagName("div")[i].id.substring(0, 3) == "div") {
        var citavId = glavniDio.getElementsByTagName("div")[i].id;
        if (citavId.substring(3, citavId.length) == parseInt(citavId.substring(3, citavId.length), 10))
          sviIdKojiImajuDiv.push(citavId.substring(3, citavId.length));
      }
    }
    sviIdKojiImajuDiv.sort(sortirajBroj);
    for (var i = 0; i < sviIdKojiImajuDiv.length; i++) {
      if (brojacDivova == sviIdKojiImajuDiv[i]) {
        brojacDivova = brojacDivova + 1;
      }
    }


    e2.style.zIndex = 1;
    //console.log("z", e2.style.zIndex);
    e2.setAttribute("id", "div" + brojacDivova);
    //dodatni atribut data-inicijalniId
    e2.setAttribute("data-inicijalniid", "div" + brojacDivova);
    e2.setAttribute("data-dodijeljeniid", "div" + brojacDivova);
    listaDodijeljenihId.push("div" + brojacDivova);
    brojacDivova = brojacDivova + 1;

    //Alignenment elementa
    e2.setAttribute("data-poravnanje", "centar");
    //Dodavanje title kloniranom elementu
    e2.title = e.target.title;
    //console.log("OVO JE      " + e2.title);

    e2.setAttribute("data-visinapikseli", true);
    e2.setAttribute("data-sirinapikseli", true);
    e2.setAttribute("data-dodijeljenaklasa", "");

    //Dodavanje kloniranog elementa glavnom divu
    glavniDio.appendChild(e2);
  }
}, true);

//funkcija za sortiranje brojeva u rastuci poredak
function sortirajBroj(a, b) {
  return a - b;
}

//Brisanje oznacenog elementa pritiskom na tipku DELETE
document.addEventListener("keydown", function (event) {
  if (event.keyCode == 46) {
    if (trenutnoOznaceniElement != null && trenutnoOznaceniElement.nodeName != "P") {
      posljednjeObrisaniElement = trenutnoOznaceniElement;
      roditeljPosljednjeObrisanogElementa = trenutnoOznaceniElement.parentNode;
      if (trenutnoOznaceniElement.parentNode.id != "glavni") {
        if (trenutnoOznaceniElement.parentNode.children.length == 1) {
          trenutnoOznaceniElement.parentNode.remove();
        }
        else {
          trenutnoOznaceniElement.remove();
        }
      }
      else {
        trenutnoOznaceniElement.remove();
      }

      glavniDio.click();
    }
  }
});

document.addEventListener("keydown", function (event) {
  if (event.keyCode == 90 && event.ctrlKey) {
    if (posljednjeObrisaniElement != null) {
      roditeljPosljednjeObrisanogElementa.appendChild(posljednjeObrisaniElement);
      posljednjeObrisaniElement = null;
    }
  }
});


//Search mockup elemenata
function search() {
  var tekst = document.getElementById("searchText").value;

  for (var i = 3; i < listaMockupa.childNodes.length; i = i + 2) {
    listaMockupa.childNodes[i].style.display = "block";
  }

  for (var i = 3; i < listaMockupa.childNodes.length; i = i + 2) {
    if (!listaMockupa.childNodes[i].getElementsByTagName("div")[0].title.toLowerCase().includes(tekst.toLowerCase())) {
      listaMockupa.childNodes[i].style.display = "none";
    }
  }
}

//Enter nad searchText
searchText.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    search();
  }
});

//Brisanje iz searcText

function deleteSearchText() {
  searchText.value = "";
  for (var i = 3; i < listaMockupa.childNodes.length; i = i + 2) {
    listaMockupa.childNodes[i].style.display = "block";
  }
}


//funkcija koja pita da li je korisnik siguran da zeli napustiti stranicu
/*window.addEventListener("beforeunload", function (e) {
  var confirmationMessage = 'Are you sure you want to leave? ' + 'Some of the changes you made might not be saved.';

  (e || window.event).returnValue = confirmationMessage; //Gecko + IE
  return confirmationMessage; //Gecko + Webkit, Safari, Chrome etc.
});*/

function prikaziModal() {
  var modal = document.getElementById("modal");
  modal.style.zIndex = vrijednostZ + 1;
  modal.style.display = "block";
  console.log("Prikazi modal");
  ocitajSveUdaljenosti();
}

// When the user clicks on <span> (x), close the modal
function zatvoriModal() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

//Event Listener nad inputom za import
document.getElementById("importFajlaInput").addEventListener("input", function (event) {
  console.log("Desilo se");
  var importFajla = document.getElementById("importFajlaInput");
  console.log(importFajla.files[0]);
  var file = importFajla.files[0];
  var reader = new FileReader();
  reader.onload = function (e) {
    var content = reader.result;
    //console.log(content.split("</style>")[1]);
    var prviDio = content.split("</style>")[1];
    console.log(prviDio.split("</body>")[0]);
    var procitaniFajl = prviDio.split("</body>")[0];
    if (procitaniFajl != "") {
      document.getElementById("glavni").innerHTML = procitaniFajl;
      var sviElementi = document.getElementById("glavni").getElementsByTagName("*");
      var noviZ = 1;
      for (var i = 0; i < sviElementi.length; i++) {
        if (parseInt(sviElementi[i].style.zIndex) > noviZ) {
          noviZ = parseInt(sviElementi[i].style.zIndex);
        }
      }
      vrijednostZ = noviZ;
      desniToolbar.style.zIndex = vrijednostZ + 1;

      listaDodijeljenihId = [];
      var sviElementi = document.getElementById("glavni").getElementsByTagName("*");
      for (var i = 0; i < sviElementi.length; i++) {
        if (sviElementi[i].dataset.dodijeljeniid != null && sviElementi[i].dataset.dodijeljeniid != "") {
          listaDodijeljenihId.push(sviElementi[i].dataset.dodijeljeniid);
        }
      }
    }
  }
  reader.readAsText(file);
});