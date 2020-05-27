var trenutnoOznaceniElement;
var vrijednostZ = 1;

var widthTextBox = document.getElementById("unosSirineTextBox");
var heightTextBox = document.getElementById("unosVisineTextBox");

var posljednjeObrisaniElement;

var selectSirina = document.getElementById("sirina");
var inputElement = document.getElementById("unosSirineTextBox");

var selectVisina = document.getElementById("visina");
var inputElementVisina = document.getElementById("unosVisineTextBox");

//desne opcije
var desniToolbar = document.getElementById("desniToolbar");
var desniToolbarOpcije = document.getElementById("desniToolbarOpcije");
var naslovOpcija = document.getElementById("nazivOdabranogElementa");

var fontElementa = document.getElementById("fontElementa");

var redovi = document.getElementById("redovi");
var kolone = document.getElementById("kolone");
var tabovi = document.getElementById("tabovi");
var radioDugmadi = document.getElementById("radioDugmadi");
var trentunoOznaceniTab = document.getElementById("trenutnoOznaceniTab");
var trenutnoOznacenoRadioDugme = document.getElementById("trenutnoOznacenoRadioDugme");
var checkboxovi = document.getElementById("checkboxovi");
var oznaceniCheckboxovi = document.getElementById("oznaceniCheckboxovi");
var neoznaceniCheckboxovi = document.getElementById("neoznaceniCheckboxovi");
var vertikalnaLista = document.getElementById("vertikalnaLista");
var horizontalnaListaElemenata = document.getElementById("horizontalnaLista");
var listaOznacenih = [];

var unosIdTrenutnogElementa = document.getElementById("unosIdTrenutnogElementa");
var listaDodijeljenihId = [];

var roditeljPosljednjeObrisanogElementa = null;

var horizontalnaUdaljenost = document.getElementById("unosHorizontalneUdaljenosti");
var vertikalnaUdaljenost = document.getElementById("unosVertikalneUdaljenosti");
var horizontalnaUdaljenostSelect = document.getElementById("horizontalnaUdaljenostSelect");
var vertikalnaUdaljenostSelect = document.getElementById("vertikalnaUdaljenostSelect");

var unosKlaseTrenutnogElementa = document.getElementById("unosKlaseTrenutnogElementa");

function obrisiElement() {
  if (trenutnoOznaceniElement != null) {
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

function staviIspred() {
  if (trenutnoOznaceniElement != null) {
    vrijednostZ = vrijednostZ + 1;
    trenutnoOznaceniElement.style.zIndex = vrijednostZ;
    desniToolbar.style.zIndex = vrijednostZ + 1;
    modal.style.zIndex = vrijednostZ + 1;
    console.log(trenutnoOznaceniElement.style.zIndex);
  }
}

function staviIza() {
  if (trenutnoOznaceniElement != null) {
    vrijednostZ = vrijednostZ - 1;
    trenutnoOznaceniElement.style.zIndex = vrijednostZ;
    console.log(trenutnoOznaceniElement.style.zIndex);
  }
}

//ENTER na input polju za sirinu i visinu
inputElement.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    document.getElementById("apply").click();
  }
});

inputElementVisina.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    document.getElementById("apply").click();
  }
});

//ENTER na input polja za font, broj redova i kolona, broj tabova i broj opcija radioButtona
fontElementa.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

redovi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

kolone.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

tabovi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

trentunoOznaceniTab.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

radioDugmadi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

checkboxovi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

trenutnoOznacenoRadioDugme.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

oznaceniCheckboxovi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

neoznaceniCheckboxovi.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

vertikalnaLista.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

horizontalnaLista.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

unosIdTrenutnogElementa.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

unosKlaseTrenutnogElementa.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    applyDesniDio();
  }
});

//Promjena sirine elementa
selectSirina.addEventListener("click", function (event) {
  var izabrano = selectSirina.options[selectSirina.selectedIndex].value;
  if (izabrano == "px") {
    widthTextBox.value = trenutnoOznaceniElement.clientWidth;
    trenutnoOznaceniElement.dataset.sirinapikseli = true;
  }
  else if (izabrano == "%") {
    var parent = trenutnoOznaceniElement.offsetParent || trenutnoOznaceniElement;
    widthTextBox.value = ((trenutnoOznaceniElement.offsetWidth / parent.offsetWidth) * 100).toFixed(0);
    trenutnoOznaceniElement.dataset.sirinapikseli = false;
  }
});

//Promjena visine elementa
selectVisina.addEventListener("click", function (event) {
  var izabrano = selectVisina.options[selectVisina.selectedIndex].value;
  if (izabrano == "px") {
    heightTextBox.value = trenutnoOznaceniElement.clientHeight;
    trenutnoOznaceniElement.dataset.visinapikseli = true;
  }
  else if (izabrano == "%") {
    var parent = trenutnoOznaceniElement.offsetParent || trenutnoOznaceniElement;
    heightTextBox.value = ((trenutnoOznaceniElement.offsetHeight / parent.offsetHeight) * 100).toFixed(0);
    trenutnoOznaceniElement.dataset.visinapikseli = false;
  }
});

//funkcija apply() za visinu i sirinu elementa
function apply() {
  if (inputElement.value == "" || inputElementVisina.value == "") {
    alert("Field cannot be empty!");
  }
  else {

    var izabrano = selectSirina.options[selectSirina.selectedIndex].value;
    if (inputElement.value != null && trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.style.width = inputElement.value + izabrano;
    }

    var izabrano1 = selectVisina.options[selectVisina.selectedIndex].value;
    if (inputElementVisina.value != null && trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.style.height = inputElementVisina.value + izabrano1;
    }

    //trenutnoOznaceniElement = null;
  }
}

//funkcija applyDesniDio() za opcije toolbara
function applyDesniDio() {
  //promjena fonta elementa
  if (fontElementa == "") {
    alert("Field cannot be empty!");
  }
  else if (fontElementa.value < 0 || fontElementa.value > 50) {
    alert("Numbers between [1,50] are allowed for this field");
  }
  else {
    if (fontElementa.value != null && trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.style.fontSize = fontElementa.value + "px";
    }
  }

  //promjena id elementa
  /*if (unosIdTrenutnogElementa.value == "") {
    alert("Field cannot be empty!");
  }
  else {*/
  if (unosIdTrenutnogElementa.value != null && trenutnoOznaceniElement != null) {
    var pronadjen = listaDodijeljenihId.indexOf(unosIdTrenutnogElementa.value);
    console.log("Usao sam u uslov\t" + pronadjen);
    if (unosIdTrenutnogElementa.value == "") {
      trenutnoOznaceniElement.id = trenutnoOznaceniElement.dataset.inicijalniid;
      trenutnoOznaceniElement.dataset.dodijeljeniid="";
    }
    else {
      if (pronadjen == -1) {
        console.log("Nije pronadjen");
        var pozicijaElementaZaBrisanje = listaDodijeljenihId.indexOf(trenutnoOznaceniElement.dataset.dodijeljeniid);
        if (pozicijaElementaZaBrisanje != -1) {
          listaDodijeljenihId.splice(pozicijaElementaZaBrisanje, 1);
          listaDodijeljenihId.splice(pozicijaElementaZaBrisanje, 0, unosIdTrenutnogElementa.value);
        }
        trenutnoOznaceniElement.dataset.dodijeljeniid = unosIdTrenutnogElementa.value;
        trenutnoOznaceniElement.id=trenutnoOznaceniElement.dataset.dodijeljeniid;
      }
      else if (trenutnoOznaceniElement.dataset.dodijeljeniid != unosIdTrenutnogElementa.value) {
        alert("Id already exists!");
      }
    }
    //}    
  }

  trenutnoOznaceniElement.dataset.dodijeljenaklasa = unosKlaseTrenutnogElementa.value;

  console.log("Lista elemenata " + listaDodijeljenihId);

  if (trenutnoOznaceniElement != null && redovi.style.display != "none" && kolone.style.display != "none") {
    console.log("Drugi uslov");
    var noviBrojRedova = document.getElementById("brojRedova").value;
    var noviBrojKolona = document.getElementById("brojKolona").value;
    if (noviBrojRedova == "" || noviBrojKolona == "") {
      alert("Field cannot be empty!");
    }
    else if (parseInt(noviBrojRedova) < 1 || parseInt(noviBrojKolona) < 1 || parseInt(noviBrojRedova) > 20 || parseInt(noviBrojKolona) > 20) {
      alert("Only numbers between [1,20] are allowed for this field!");
    }
    else {
      trenutnoOznaceniElement.getElementsByTagName("table")[0].remove();
      var redoviKolone = "";
      for (var i = 0; i < noviBrojRedova; i++) {
        if (i == 0) {
          redoviKolone += `\n<thead>`;
        }
        else if (i == 1) {
          redoviKolone += `\n<tbody>`;
        }
        redoviKolone += `<tr>`;
        for (var j = 0; j < noviBrojKolona; j++) {
          if (i == 0) {
            var broj = j + 1;
            redoviKolone += `<th class="prviRedTabeleKlasa">` + `Header ` + broj + `</th>`;
          }
          else {
            var broj = (i - 1) * noviBrojKolona + j + 1;
            redoviKolone += `<td>` + `Cell ` + broj + `</td>`
          }
        }
        redoviKolone += `</tr>`;
        if (i == 0) {
          redoviKolone += `</thead>`;
        }
        else if (i == noviBrojRedova - 1) {
          redoviKolone += `</tbody>`;
        }
      }
      var novaTabela = `<table class="tabelaElementKlasa" onclick="this.contentEditable=false;" ondblclick="this.contentEditable=true;this.focus();" contentEditable="false" style="pointer-events: auto;"><text></text>` + redoviKolone + `</table>`;
      console.log(novaTabela);
      trenutnoOznaceniElement.innerHTML = novaTabela;
    }
  }


  if (trenutnoOznaceniElement != null && tabovi.style.display != "none") {
    var noviBrojTabova = document.getElementById("brojTabova").value;
    var noviBrojTrenutnoOznacenogTaba = document.getElementById("brojTrenutnoOznacenogTaba").value;
    if (noviBrojTabova == "" || noviBrojTrenutnoOznacenogTaba == "") {
      alert("Field cannot be empty!");
    }
    else if (parseInt(noviBrojTabova) < 1 || parseInt(noviBrojTabova) > 20) {
      alert("Only numbers between [1,20] are allowed for this field!");
    }
    else if (parseInt(noviBrojTrenutnoOznacenogTaba) < 1 || parseInt(noviBrojTrenutnoOznacenogTaba) > parseInt(noviBrojTabova)) {
      alert("Number must be in range between 1 and number inserted into \"Number of tabs\" field!")
    }
    else {
      var listaTabova = trenutnoOznaceniElement.getElementsByTagName("ul")[0];
      var izgledListe = "";
      if (listaTabova != null) {
        listaTabova.remove();
        izgledListe += `<ul class="tabListaKlasa">`;
        for (var i = 0; i < noviBrojTabova; i++) {
          if (i == noviBrojTrenutnoOznacenogTaba - 1) {
            izgledListe += `<li data-oznacen=\"1\">Tab` + (i + 1) + `</li>`;
          }
          else {
            izgledListe += `<li data-oznacen=\"0\">Tab` + (i + 1) + `</li>`;
          }
        }
        izgledListe += `</ul>`;
        var horizontalnaLista = trenutnoOznaceniElement.getElementsByTagName("horizontal")[0];
        horizontalnaLista.innerHTML = izgledListe;
      }
    }
  }

  if (trenutnoOznaceniElement != null && radioDugmadi.style.display != "none") {
    var noviBrojRadioDugmadi = document.getElementById("brojOpcijaRadioDugmadi").value;
    var noviBrojTrenutnoOznacenogRadioDugmeta = document.getElementById("brojTrenutnoOznacenogRadioDugmeta").value;
    if (noviBrojRadioDugmadi == "" || noviBrojTrenutnoOznacenogRadioDugmeta == "") {
      alert("Field cannot be empty!");
    }
    else if (parseInt(noviBrojRadioDugmadi) < 1 || parseInt(noviBrojRadioDugmadi) > 20) {
      alert("Only numbers between [1,20] are allowed for this field!");
    }
    else if (parseInt(noviBrojTrenutnoOznacenogRadioDugmeta) < 1 || parseInt(noviBrojTrenutnoOznacenogRadioDugmeta) > parseInt(noviBrojRadioDugmadi)) {
      alert("Number must be in range between 1 and number inserted into \"Number of options in radio button\" field!")
    }
    else {
      var listaRadioDugmadi = trenutnoOznaceniElement.getElementsByTagName("ul")[0];
      var izgledRadioDugmeta = "";
      if (listaRadioDugmadi != null) {
        listaRadioDugmadi.remove();
        izgledRadioDugmeta += `<ul class="radioButtonListaKlasa">`;
        for (var i = 0; i < noviBrojRadioDugmadi; i++) {
          if (i == noviBrojTrenutnoOznacenogRadioDugmeta - 1) {
            izgledRadioDugmeta += `<li class=\"oznacena\">option` + (i + 1) + `</li>`;
          }
          else {
            izgledRadioDugmeta += `<li class=\"neoznacena\">option` + (i + 1) + `</li>`;
          }
        }
        izgledRadioDugmeta += `</ul>`;
        trenutnoOznaceniElement.innerHTML = izgledRadioDugmeta;
      }
    }
  }

  if (trenutnoOznaceniElement != null && checkboxovi.style.display != "none") {
    var noviBrojCheckboxova = document.getElementById("brojOpcijaCheckboxova").value;
    var noviBrojTrenutnoOznacenogCheckboxa = document.getElementById("brojTrenutnoOznacenogCheckboxa").value;
    var noviBrojTrenutnoOdselektovanogCheckboxa = document.getElementById("brojTrenutnoNeoznacenogCheckboxa").value;
    if (noviBrojCheckboxova == "") {
      alert("Field cannot be empty!");
    }
    else if (parseInt(noviBrojCheckboxova) < 1 || parseInt(noviBrojCheckboxova) > 20) {
      alert("Only numbers between [1,20] are allowed for this field!");
    }
    else if (parseInt(noviBrojTrenutnoOznacenogCheckboxa) < 1 || parseInt(noviBrojTrenutnoOznacenogCheckboxa) > parseInt(noviBrojCheckboxova)) {
      alert("Number must be in range between 1 and number inserted into \"Number of options in checkbox\" field!")
    }
    else if (parseInt(noviBrojTrenutnoOdselektovanogCheckboxa) < 1 || parseInt(noviBrojTrenutnoOdselektovanogCheckboxa) > parseInt(noviBrojCheckboxova)) {
      alert("Number must be in range between 1 and number inserted into \"Number of options in checkbox\" field!")
    }
    else {
      listaOznacenih.push(noviBrojTrenutnoOznacenogCheckboxa - 1);

      var oznaceniCHCH = [];

      for (var s = 0; s < noviBrojCheckboxova; s++) {
        oznaceniCHCH.push(0);
      }

      for (var s = 0; s < listaOznacenih.length; s++) {
        if (noviBrojTrenutnoOdselektovanogCheckboxa != "" && listaOznacenih[s] == noviBrojTrenutnoOdselektovanogCheckboxa - 1) {
          listaOznacenih.splice(s, 1);
        }
      }

      for (var s = 0; s < listaOznacenih.length; s++) {
        for (var m = 0; m < oznaceniCHCH.length; m++) {
          if (m == listaOznacenih[s]) oznaceniCHCH[m] = 1;
        }
      }

      console.log(oznaceniCHCH);

      var listaCheckboxova = trenutnoOznaceniElement.getElementsByTagName("ul")[0];
      var izgledCheckboxa = "";
      if (listaCheckboxova != null) {
        listaCheckboxova.remove();
        izgledCheckboxa += `<ul class="checkboxListaKlasa">`;
        for (var i = 0; i < noviBrojCheckboxova; i++) {
          if (oznaceniCHCH[i] == 1) {
            izgledCheckboxa += `<li class=\"checkboxOznacena\">option` + (i + 1) + `</li>`;
          }
          else {
            izgledCheckboxa += `<li class=\"checkboxNeoznacena\">option` + (i + 1) + `</li>`;
          }
        }
        izgledCheckboxa += `</ul>`;
        trenutnoOznaceniElement.innerHTML = izgledCheckboxa;
      }
    }
  }

  if (trenutnoOznaceniElement != null && vertikalnaLista.style.display != "none") {

    var noviBrojElemenataVertikalneListe = document.getElementById("brojOpcijaVertikalneListe").value;
    if (noviBrojElemenataVertikalneListe == "") {
      alert("Field cannot be empty!");
    }
    else {
      var listaElemenataVertikalno = trenutnoOznaceniElement.getElementsByTagName("ul")[0];
      var izgledVertikalneListe = "";
      if (listaElemenataVertikalno != null) {
        listaElemenataVertikalno.remove();
        izgledVertikalneListe += `
  <div id="unutarListe">
      <vertical id="listVertical">
          <ul class="verticalListKlasa">`;
        for (var i = 0; i < noviBrojElemenataVertikalneListe; i++) {

          izgledVertikalneListe += `
            <li class=\"elementListeV\">List element ` + (i + 1) + `</li>
            `;
        }
        izgledVertikalneListe += `
          
          </ul>
  
     </vertical>
  </div>`;
        trenutnoOznaceniElement.innerHTML = izgledVertikalneListe;
      }
    }
    console.log(trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[1]);

  }

  if (trenutnoOznaceniElement != null && horizontalnaListaElemenata.style.display != "none") {
    var noviBrojElemenataHorizontalneListe = document.getElementById("brojOpcijaHorizontalneListe").value;
    if (noviBrojElemenataHorizontalneListe == "") {
      alert("Field cannot be empty!");
    }
    else {
      var listaElemenataHorizontalno = trenutnoOznaceniElement.getElementsByTagName("ul")[0];
      console.log(listaElemenataHorizontalno);
      var izgledHorizontalneListe = "";
      if (listaElemenataHorizontalno != null) {
        listaElemenataHorizontalno.remove();
        izgledHorizontalneListe += `<horizontal id="listHorizontal"><ul class="horizontalListKlasa">`;
        for (var i = 0; i < noviBrojElemenataHorizontalneListe; i++) {

          izgledHorizontalneListe += `<li class=\"elementListeH\">List element ` + (i + 1) + `</li>`;
        }
        izgledHorizontalneListe += `</ul></horizontal>`;
        trenutnoOznaceniElement.innerHTML = izgledHorizontalneListe;
      }
    }
  }
}

function vratiPosljednjeObrisaniElement() {
  if (posljednjeObrisaniElement != null) {
    roditeljPosljednjeObrisanogElementa.appendChild(posljednjeObrisaniElement);
    posljednjeObrisaniElement = null;
  }
}

strelicaToolbara.addEventListener("click", function (event) {
  strelicaToolbara.style.display = "none";
  desniToolbar.style.display = "inline";
  onemoguciKlikUToolbaru(true);
})

function onemoguciKlikUToolbaru(onemogucen) {
  unosIdTrenutnogElementa.disabled = onemogucen;
  unosKlaseTrenutnogElementa.disabled = onemogucen;
  document.getElementById("deleteDugme").disabled = onemogucen;
  document.getElementById("bringToFrontDugme").disabled = onemogucen;
  document.getElementById("sendToBackDugme").disabled = onemogucen;
  document.getElementById("radio1").disabled = onemogucen;
  document.getElementById("radio2").disabled = onemogucen;
  document.getElementById("radio3").disabled = onemogucen;
  document.getElementById("radio4").disabled = onemogucen;
  fontElementa.disabled = onemogucen;
  inputElementVisina.disabled = onemogucen;
  inputElement.disabled = onemogucen;
  document.getElementById("visina").disabled = onemogucen;
  document.getElementById("sirina").disabled = onemogucen;
  document.getElementById("apply").disabled = onemogucen;
}

//Grupisanje elemenata
var group = document.getElementById("Group");
group.addEventListener("click", function (event) {

  var glavni = document.getElementById("glavni");
  var sviElementi = glavni.children;
  var roditeljGroup = document.createElement("div");
  var nizMinLeft = [];
  var nizMinTop = [];
  var minLeft = 0;
  var minTop = 0;
  var maxLeft = 0;
  var maxTop = 0;
  var nizObjekata = [];
  var offsetBottom = [];
  var offsetRight = [];

  roditeljGroup.style.background = "transparent"
  roditeljGroup.classList.add("resize-drag");

  var oznacenaDjeca = [];
  for (var i = 0; i < sviElementi.length; i++) {
    if (sviElementi[i].style.webkitBoxShadow == "rgb(32, 201, 151) 0px 0px 0px 2px") {
      oznacenaDjeca.push(sviElementi[i]);
    }
  }

  if (oznacenaDjeca.length == 1) {
    alert("It is not possible to group only one element!");
  }
  else {

    var div = oznacenaDjeca;
    var udaljenostiSvakogElementaX = [], udaljenostiSvakogElementaY = [];

    for (var i = 0; i < div.length; i++) {
      var divOffset = offset(div[i]);

      //vrijednosti su (indeks,x-udaljenost,y-udaljenost,sirina,visina,id)
      udaljenostiSvakogElementaX.push([i, divOffset.left, divOffset.top, div[i].offsetWidth, div[i].offsetHeight, div[i].id]);
      //vrijednosti su (indeks,y-udaljenost,x-udaljenost,sirina,visina,id)
      udaljenostiSvakogElementaY.push([i, divOffset.top, divOffset.left, div[i].offsetWidth, div[i].offsetHeight, div[i].id]);
    }

    udaljenostiSvakogElementaX = bubbleSort(udaljenostiSvakogElementaX);
    udaljenostiSvakogElementaY = bubbleSort(udaljenostiSvakogElementaY);

    var jedanUnutarDrugog = [];
    for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
      for (var j = 0; j < udaljenostiSvakogElementaX.length; j++) {
        var unutar = daLiJeUnutar(udaljenostiSvakogElementaX[j], udaljenostiSvakogElementaX[i]);
        if (unutar && i != j) {
          jedanUnutarDrugog.push([udaljenostiSvakogElementaX[j][5], udaljenostiSvakogElementaX[i][5]])
        }
      }
    }

    for (var i = 0; i < jedanUnutarDrugog.length; i++) {
      var roditelj = document.getElementById(jedanUnutarDrugog[i][1]);
      var dijete = document.getElementById(jedanUnutarDrugog[i][0]);
      dijete.style.position = "absolute";
      var offsetDijete = offset(dijete);
      var offsetRoditelj = offset(roditelj);
      dijete.style.left = (offsetDijete.left - offsetRoditelj.left) + "px";
      dijete.style.top = (offsetDijete.top - offsetRoditelj.top) + "px";
      dijete.style.setProperty("transform", "none");
      dijete.dataset.x = "0";
      dijete.dataset.y = "0";
      dijete.style.boxShadow = "";
      roditelj.appendChild(dijete);
    }

    var oznacenaDjeca = [];
    for (var i = 0; i < sviElementi.length; i++) {
      if (sviElementi[i].style.webkitBoxShadow == "rgb(32, 201, 151) 0px 0px 0px 2px") {
        oznacenaDjeca.push(sviElementi[i]);
      }
    }

    if (oznacenaDjeca.length > 1) {

      for (var j = 0; j < oznacenaDjeca.length; j++) {
        var offs = offset(oznacenaDjeca[j]);
        var objekat = { top: offs.top, left: offs.left, lik: oznacenaDjeca[j] };
        nizObjekata.push(objekat);
        nizMinLeft.push(offs.left);
        nizMinTop.push(offs.top);
        oznacenaDjeca[j].parentNode.insertBefore(roditeljGroup, oznacenaDjeca[j]);
        roditeljGroup.appendChild(oznacenaDjeca[j]);
      }

      minLeft = Math.min.apply(null, nizMinLeft);
      minTop = Math.min.apply(null, nizMinTop);

      roditeljGroup.style.position = "absolute";
      roditeljGroup.style.left = minLeft + "px";
      roditeljGroup.style.top = minTop + "px";

      for (var i = 0; i < oznacenaDjeca.length; i++) {
        oznacenaDjeca[i].style.position = "absolute";
        var offsetGlavni = offset(roditeljGroup);
        var offs = offset(oznacenaDjeca[i]);
        console.log("offsPocetak", offs);
        oznacenaDjeca[i].style.left = (offs.left - offsetGlavni.left - offsetGlavni.left) + "px";
        oznacenaDjeca[i].style.top = (offs.top - offsetGlavni.top - offsetGlavni.top) + "px";
        oznacenaDjeca[i].style.setProperty("transform", "none");
        oznacenaDjeca[i].dataset.x = "0";
        oznacenaDjeca[i].dataset.y = "0";
        oznacenaDjeca[i].style.boxShadow = "";
        offs = offset(oznacenaDjeca[i]);
        offsetBottom.push(parseFloat(offs.top - offsetGlavni.top) + parseFloat(oznacenaDjeca[i].style.height));
        offsetRight.push(parseFloat(offs.left - offsetGlavni.left) + parseFloat(oznacenaDjeca[i].style.width));
      }

      maxLeft = Math.max.apply(null, offsetRight);
      maxTop = Math.max.apply(null, offsetBottom);

      roditeljGroup.style.width = maxLeft + "px";
      roditeljGroup.style.height = maxTop + "px";

      roditeljGroup.style.boxShadow = "0 0 0 2px #20c997";
      roditeljGroup.style.overflow = "hidden";
      roditeljGroup.setAttribute("id", "div" + brojacDivova);
      listaDodijeljenihId.push("div" + brojacDivova);
      brojacDivova = brojacDivova + 1;
      trenutnoOznaceniElement = roditeljGroup;

      glavni.appendChild(roditeljGroup);
    }

  }
});

function mobileVersion() {
  var el = document.getElementById("glavni");
  el.style.height = 70 + "%";
  el.style.width = 25 + "%"
  el.style.cssFloat = "left"
  el.style.overflow = "hidden";
  el.style.border = "thick solid #E8E8E8";
  el.style.borderRadius = 50 + "px";
}
function tabletVersion() {
  var el = document.getElementById("glavni");
  el.style.height = 79 + "%";
  el.style.width = 50 + "%"
  el.style.cssFloat = "left"
  el.style.overflow = "hidden";
  el.style.border = "thick solid #E8E8E8";
  el.style.borderRadius = 50 + "px";
}

function desktopVersion() {
  var el = document.getElementById("glavni");
  el.style.height = 85 + "%";
  el.style.width = 75 + "%";
  el.style.cssFloat = "right"
  el.style.overflow = "scroll";
  el.style.border = "none";
  el.style.borderRadius = 0 + "px";
}

