vertikalnaUdaljenostSelect.addEventListener("click", function(event) {
    odrediUdaljenost();
});
  
horizontalnaUdaljenostSelect.addEventListener("click", function(event) {
    odrediUdaljenost();
});

//funkcija koja određuje horizontalnu i vertikalnu udaljenost dva elementa
var pocetnaIspod = 0;
var pocetnaDesno = 0;
var pocetnaIspodPosto = 0;
var pocetnaDesnoPosto = 0;

function odrediUdaljenost(){
  
  var glavni = document.getElementById("glavni");
  var sviElementi = glavni.children;
  var oznacenaDjeca = [];

  for(var i = 0; i < sviElementi.length; i++){
    if(sviElementi[i].style.webkitBoxShadow == "rgb(32, 201, 151) 0px 0px 0px 2px"){
      oznacenaDjeca.push(sviElementi[i]); 
    }
  }
  
  console.log("oznaceni", oznacenaDjeca);

  if(oznacenaDjeca.length == 2){
    
    var objekat = odrediPozicijuElemenata(oznacenaDjeca);
    var jedanIspodDva = objekat.ispod;
    var jedanDesnoOdDva = objekat.desno;

    if(jedanIspodDva.length != 0){
      vertikalnaUdaljenost.disabled = false;
      vertikalnaUdaljenostSelect.disabled = false;
      var elementIspod = document.getElementById(jedanIspodDva[0][0]);
      var elementIznad = document.getElementById(jedanIspodDva[0][1]);
      var offsetIznad = offset(elementIznad);
      var offsetIspod = offset(elementIspod);
      if (vertikalnaUdaljenostSelect.options[vertikalnaUdaljenostSelect.selectedIndex].value == "px") {
        pocetnaIspod = (offsetIspod.top - offsetIznad.top - parseFloat(elementIznad.style.height) - 4).toFixed(0); 
        vertikalnaUdaljenost.value = pocetnaIspod;
      }
      else if(vertikalnaUdaljenostSelect.options[vertikalnaUdaljenostSelect.selectedIndex].value == "%") {
        var pomocna = offsetIspod.top - offsetIznad.top - parseFloat(elementIznad.style.height);
        pocetnaIspodPosto = ((pomocna/glavni.offsetHeight)*100).toFixed(0);
        vertikalnaUdaljenost.value = pocetnaIspodPosto;
      }
    }
    else{
      vertikalnaUdaljenost.disabled = true;
      vertikalnaUdaljenostSelect.disabled = true;
      vertikalnaUdaljenost.value = "";
    }

    if(jedanDesnoOdDva.length != 0){
      horizontalnaUdaljenost.disabled = false;
      horizontalnaUdaljenostSelect.disabled = false;
      var elementDesno = document.getElementById(jedanDesnoOdDva[0][0]);
      var elementLijevo = document.getElementById(jedanDesnoOdDva[0][1]);
      var offserDesno = offset(elementDesno);
      var offsetLijevo = offset(elementLijevo);
      if (horizontalnaUdaljenostSelect.options[horizontalnaUdaljenostSelect.selectedIndex].value == "px") {
        pocetnaDesno = (offserDesno.left - offsetLijevo.left - parseFloat(elementLijevo.style.width) - 5).toFixed(0); 
        console.log("odredi", horizontalnaUdaljenost.disabled, pocetnaDesno);
        horizontalnaUdaljenost.value = pocetnaDesno;
        console.log("horizontalna", horizontalnaUdaljenost.value);
      }
      else if(horizontalnaUdaljenostSelect.options[horizontalnaUdaljenostSelect.selectedIndex].value == "%") {
        var pomocna = offserDesno.left - offsetLijevo.left - parseFloat(elementLijevo.style.width);
        pocetnaDesnoPosto = ((pomocna/glavni.offsetWidth)*100).toFixed(0);
        horizontalnaUdaljenost.value = pocetnaDesnoPosto;
      }
    }
    else{
      horizontalnaUdaljenost.disabled = true;
      horizontalnaUdaljenostSelect.disabled = true;
      horizontalnaUdaljenost.value = "";
    }

  }
  else{
    vertikalnaUdaljenost.disabled = true;
    vertikalnaUdaljenostSelect.disabled = true;
    horizontalnaUdaljenost.disabled = true;
    horizontalnaUdaljenostSelect.disabled = true;
    horizontalnaUdaljenost.value = "";
    vertikalnaUdaljenost.value = "";
  }
}

//vertikalna udaljenost enter
vertikalnaUdaljenost.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    pomjeriElement();
  }
});

//horizontalna udaljenost enter
horizontalnaUdaljenost.addEventListener("keyup", function (event) {
  if (event.keyCode == 13) {
    pomjeriElement();
  }
});

function arraysEqual(arr1, arr2) {
  if(arr1.length !== arr2.length)
      return false;
  for(var i = arr1.length; i--;) {
      if(arr1[i] !== arr2[i])
          return false;
  }
  return true;
}

//funkcija za određivanje gdje se jedan element nalazi u odnosu na drugi
function odrediPozicijuElemenata(oznacenaDjeca){

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

    var jedanUnutarDrugog=[];
    for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
      for (var j = 0; j < udaljenostiSvakogElementaX.length; j++) {
        var unutar = daLiJeUnutar(udaljenostiSvakogElementaX[j], udaljenostiSvakogElementaX[i]);
        if (unutar && i!=j) {
          jedanUnutarDrugog.push([udaljenostiSvakogElementaX[j][5], udaljenostiSvakogElementaX[i][5]])
        }
        //console.log("Element " + udaljenostiSvakogElementaX[j][5] + " " + unutar + " element " + udaljenostiSvakogElementaX[i][5]);
      }
    }

    var jedanDesnoOdDva = [];
    var jedanIspodDva = [];

    for (var i = 0; i < udaljenostiSvakogElementaX.length; i++) {
      for (var j = i + 1; j < udaljenostiSvakogElementaX.length; j++) {
        var desno = daLiJeDesnoOd(udaljenostiSvakogElementaX[i], udaljenostiSvakogElementaX[j],jedanUnutarDrugog);
        if (desno) {
          jedanDesnoOdDva.push([udaljenostiSvakogElementaX[j][5], udaljenostiSvakogElementaX[i][5]]);
        }
        //console.log("U odnosu na element " + udaljenostiSvakogElementaX[i][5] + " " + desno + " element " + udaljenostiSvakogElementaX[j][5]);
      }
    }

    for (var i = 0; i < udaljenostiSvakogElementaY.length; i++) {
      for (var j = i + 1; j < udaljenostiSvakogElementaY.length; j++) {
        var ispod = daLiJeIspodOd(udaljenostiSvakogElementaY[i], udaljenostiSvakogElementaY[j],jedanUnutarDrugog);
        if (ispod) {
          jedanIspodDva.push([udaljenostiSvakogElementaY[j][5], udaljenostiSvakogElementaY[i][5]]);
        }
        //console.log("U odnosu na element " + udaljenostiSvakogElementaY[i][5] + " " + ispod + " element " + udaljenostiSvakogElementaY[j][5]);
      }
    }

    return {ispod: jedanIspodDva, desno: jedanDesnoOdDva};
}

//funkcija koja pomjera element na željenu udaljenost
var brojacZaPocetni = 0;
var pocetnaUdaljenost = 0;
var pocetnaUdaljenostPosto = 0;
var brojacZaPocetniDesno = 0;
var pocetnaUdaljenostDesno = 0;
var pocetnaUdaljenostPostoDesno = 0;
var oznacenaDjecaPamti = [];
var x1 = 0;
var y1 = 0;
var x11 = 0;
var y11 = 0;

function pomjeriElement(){
  
  var glavni = document.getElementById("glavni");
  var sviElementi = glavni.children;
  var oznacenaDjeca = [];

  for(var i = 0; i < sviElementi.length; i++){
    if(sviElementi[i].style.webkitBoxShadow == "rgb(32, 201, 151) 0px 0px 0px 2px"){
      oznacenaDjeca.push(sviElementi[i]); 
      if(brojacZaPocetni == 0 && brojacZaPocetniDesno == 0){
        oznacenaDjecaPamti.push(sviElementi[i]);
        x1 = oznacenaDjecaPamti[0].dataset.x;
        y1 = oznacenaDjecaPamti[0].dataset.y;
        if(i == 1){
          x11 = oznacenaDjecaPamti[1].dataset.x;
          y11 = oznacenaDjecaPamti[1].dataset.y;
          x1 = (Math.round(parseFloat(x1)*100)/100);
          y1 = (Math.round(parseFloat(y1)*100)/100);
          x11 = (Math.round(parseFloat(x11)*100)/100);
          y11 = (Math.round(parseFloat(y11)*100)/100);
        }
      }
    }
  }

  if(arraysEqual(oznacenaDjeca, oznacenaDjecaPamti)){
    
    var x = oznacenaDjeca[0].dataset.x;
    var y = oznacenaDjeca[0].dataset.y;
    var xx = oznacenaDjeca[1].dataset.x;
    var yy = oznacenaDjeca[1].dataset.y;

    if((Math.round(parseFloat(x)*100)/100) != x1) {
      if(vertikalnaUdaljenost.value != ""){
        brojacZaPocetni = 0;
      }
      else if(horizontalnaUdaljenost.value != ""){
        brojacZaPocetniDesno = 0;
      }
      x1 = (Math.round(parseFloat(x)*100)/100);
    }
    if((Math.round(parseFloat(xx)*100)/100) != x11) {
      if(vertikalnaUdaljenost.value != ""){
        brojacZaPocetni = 0;
      }
      else if(horizontalnaUdaljenost.value != ""){
        brojacZaPocetniDesno = 0;
      }
      x11 = (Math.round(parseFloat(xx)*100)/100);
    }
    if((Math.round(parseFloat(y)*100)/100) != y1){
      if(horizontalnaUdaljenost.value != "") {
        brojacZaPocetniDesno = 0;
      }
      else if (vertikalnaUdaljenost.value != ""){
        brojacZaPocetni = 0;
      }
      y1 = (Math.round(parseFloat(y)*100)/100);
    }
    if((Math.round(parseFloat(yy)*100)/100) != y11){
      if(horizontalnaUdaljenost.value != "") {
        brojacZaPocetniDesno = 0;
      }
      else if (vertikalnaUdaljenost.value != ""){
        brojacZaPocetni = 0;
      }
      y11 = (Math.round(parseFloat(yy)*100)/100);
    }
  }

  if(!arraysEqual(oznacenaDjeca, oznacenaDjecaPamti)){
    brojacZaPocetni = 0;
    brojacZaPocetniDesno = 0;
    oznacenaDjecaPamti = [];
    for(var i=0; i<oznacenaDjeca.length;i++){
      oznacenaDjecaPamti.push(oznacenaDjeca[i]);
      x1 = oznacenaDjecaPamti[0].dataset.x;
      y1 = oznacenaDjecaPamti[0].dataset.y;
      if(i == 1){
        x11 = oznacenaDjecaPamti[1].dataset.x;
        y11 = oznacenaDjecaPamti[1].dataset.y;
        x1 = (Math.round(parseFloat(x1)*100)/100);
        y1 = (Math.round(parseFloat(y1)*100)/100);
        x11 = (Math.round(parseFloat(x11)*100)/100);
        y11 = (Math.round(parseFloat(y11)*100)/100);
      }
    }
  }

  if(oznacenaDjeca.length == 2){

    var objekat = odrediPozicijuElemenata(oznacenaDjeca);
    var jedanIspodDva = objekat.ispod;
    var jedanDesnoOdDva = objekat.desno;

    if(jedanIspodDva.length != 0) {
      var elementIspod = document.getElementById(jedanIspodDva[0][0]);
      var elementIznad = document.getElementById(jedanIspodDva[0][1]);

      if(brojacZaPocetni == 0) {
          pocetnaUdaljenost = (parseFloat(elementIspod.dataset.y) - parseFloat(elementIznad.dataset.y) -  parseFloat(elementIznad.style.height) - 4).toFixed(0);
          pocetnaUdaljenostPosto = ((pocetnaUdaljenost/glavni.offsetHeight)*100).toFixed(0);
          brojacZaPocetni++;
      }
      if (vertikalnaUdaljenostSelect.options[vertikalnaUdaljenostSelect.selectedIndex].value == "px") {
        elementIspod.style.top =  parseFloat(vertikalnaUdaljenost.value) - parseFloat(pocetnaUdaljenost) + "px";
      }
      else if(vertikalnaUdaljenostSelect.options[vertikalnaUdaljenostSelect.selectedIndex].value == "%") {
        elementIspod.style.top = parseFloat(vertikalnaUdaljenost.value) - parseFloat(pocetnaUdaljenostPosto) - 1 + "%";
      }
    }
    if(jedanDesnoOdDva.length != 0){
      var elementDesno = document.getElementById(jedanDesnoOdDva[0][0]);
      var elementLijevo = document.getElementById(jedanDesnoOdDva[0][1]);

      if(brojacZaPocetniDesno == 0) {
        pocetnaUdaljenostDesno = (parseFloat(elementDesno.dataset.x) - parseFloat(elementLijevo.dataset.x) - parseFloat(elementLijevo.style.width) - 5).toFixed(0);
        pocetnaUdaljenostPostoDesno = ((pocetnaUdaljenostDesno/glavni.offsetWidth)*100).toFixed(0);
        brojacZaPocetniDesno++;
      }
      if (horizontalnaUdaljenostSelect.options[horizontalnaUdaljenostSelect.selectedIndex].value == "px") {
        elementDesno.style.left = parseFloat(horizontalnaUdaljenost.value) - parseFloat(pocetnaUdaljenostDesno) + "px";
      }
      else if(horizontalnaUdaljenostSelect.options[horizontalnaUdaljenostSelect.selectedIndex].value == "%") {
        elementDesno.style.left = parseFloat(horizontalnaUdaljenost.value) - parseFloat(pocetnaUdaljenostPostoDesno) - 1 + "%";
      }
    }
    
  }
}