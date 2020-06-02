//funkcija za download datoteke
function download(data, filename, type) {
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

//funkcija za download cijelog html-a
function snimiCitavFajl() {
  document.getElementById("glavni").click();
  var fajl = `<!DOCTYPE html><html><head></head><meta charset="utf-8"><body>`;
  var sviElementi = document.getElementById("glavni").getElementsByTagName("*");
  var stil = ``;
  var element = ``;
  for (var i = 0; i < sviElementi.length; i++) {
    if (sviElementi[i].id != "") {
      stil += `#` + sviElementi[i].id + ` {` + napraviCSS(sviElementi[i], true) + `}\n`;
      stil += `#` + sviElementi[i].id + `::after {` + napraviCSSAfter(sviElementi[i]) + `}\n`;
      stil += `#` + sviElementi[i].id + `::before {` + napraviCSSBefore(sviElementi[i]) + `}\n`;
      if (sviElementi[i].tagName != "LI") {
        element = sviElementi[i];
      }
    }
    else if (sviElementi[i].className != "") {
      var klasa = sviElementi[i].className.split(" ")[0];
      stil += `.` + klasa + ` {` + napraviCSS(sviElementi[i], true) + `}\n`;
      stil += `.` + klasa + `::after {` + napraviCSSAfter(sviElementi[i]) + `}\n`;
      stil += `.` + klasa + `::before {` + napraviCSSBefore(sviElementi[i]) + `}\n`;
    }
    else {
      var nazivTaga = sviElementi[i].tagName;
      stil += `#` + element.id + ` ` + nazivTaga + ` {` + napraviCSS(sviElementi[i], false) + `}\n`;
      stil += `#` + element.id + ` ` + nazivTaga + `::after {` + napraviCSSAfter(sviElementi[i]) + `}\n`;
      stil += `#` + element.id + ` ` + nazivTaga + `::before {` + napraviCSSBefore(sviElementi[i]) + `}\n`;
    }
    /*else{
      stil+=`#`+element.id+` `+sviElementi[i].tagName+` {`+napraviCSS(sviElementi[i])+`}\n`;
      stil+=`#`+element.id+` `+sviElementi[i].tagName+`::after {`+napraviCSSAfter(sviElementi[i])+`}\n`;
      stil+=`#`+element.id+` `+sviElementi[i].tagName+`::before {`+napraviCSSBefore(sviElementi[i])+`}\n`;
    }*/
  }
  fajl += `<style>[data-oznacen="1"]{
    background: #3D5A75 !important;
  }\n`+ stil + `</style>`;
  fajl += glavniDio.innerHTML;
  fajl += `</body></html>`;
  var naziv = document.getElementById("nepotrebniPDF").getAttribute("name");
  download(fajl, naziv + ".html", ".html");

  /*var fajl=`<!DOCTYPE html><html>`;
  fajl+=`<head></head><body>`;
  fajl+=glavniDio.innerHTML;
  fajl+=`</body></html>`;
  download(fajl,"nekiFajl.html",".html");*/
}

function napraviCSS(element, vazno) {
  var s = '';
  var o = getComputedStyle(element);
  for (var i = 0; i < o.length; i++) {
    if (vazno) {
      s += o[i] + ':' + o.getPropertyValue(o[i]) + ' !important;';
    }
    else
      s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
  }
  return s;
};

function napraviCSSAfter(element) {
  var s = '';
  var o = getComputedStyle(element, "::after");
  for (var i = 0; i < o.length; i++) {
    s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
  }
  return s;
};

function napraviCSSBefore(element) {
  var s = '';
  var o = getComputedStyle(element, "::before");
  for (var i = 0; i < o.length; i++) {
    s += o[i] + ':' + o.getPropertyValue(o[i]) + ';';
  }
  return s;
};

function otvoriFajl() {
  document.getElementById("glavni").click();
  //document.getElementById("importFajlaInput").click();



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