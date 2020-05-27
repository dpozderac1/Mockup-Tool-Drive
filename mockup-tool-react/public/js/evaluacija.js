var elementiAtributi = [];
function ocitajSveElemente() {
    elementiAtributi=[];
    var sviElementi = document.getElementById("glavni").getElementsByTagName("div");
    for (var i = 0; i < sviElementi.length; i++) {
        if (sviElementi[i].dataset.inicijalniid != undefined) {
            var x = sviElementi[i].dataset.x;
            var y = sviElementi[i].dataset.y;
            if (x == undefined) x = 0;
            if (y == undefined) y = 0;
            elementiAtributi.push([sviElementi[i].id, sviElementi[i].offsetWidth, sviElementi[i].offsetHeight, parseInt(sviElementi[i].style.zIndex), parseInt(x), parseInt(y)]);
        }
    }
    return elementiAtributi;
}

function dajSlucajniInteger(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

var slucajniAtributi = [];
function odrediSlucajneAtribute() {
    slucajniAtributi=[];
    for (var i = 0; i < elementiAtributi.length; i++) {
        var slucajniBroj = dajSlucajniInteger(0, 5);
        slucajniAtributi.push(slucajniBroj);
    }
    return slucajniAtributi;
}

var noveVrijednosti = [];
function promijeniVrijednostiAtributa() {
    noveVrijednosti=[];
    ocitajSveElemente();
    odrediSlucajneAtribute();
    for (var i = 0; i < slucajniAtributi.length; i++) {
        if (slucajniAtributi[i] == 0) {
            noveVrijednosti.push(dajSlucajniString(5));
        }
        else if (slucajniAtributi[i] == 1) {
            noveVrijednosti.push(dajSlucajniInteger(1, document.getElementById("glavni").offsetWidth));
        }
        else if (slucajniAtributi[i] == 2) {
            noveVrijednosti.push(dajSlucajniInteger(1, document.getElementById("glavni").offsetHeight));
        }
        else if (slucajniAtributi[i] == 3) {
            noveVrijednosti.push(dajSlucajniInteger(1, vrijednostZ));
        }
        else if (slucajniAtributi[i] == 4) {
            noveVrijednosti.push(dajSlucajniInteger(1, document.getElementById("glavni").offsetWidth - elementiAtributi[i][1] - 1));
        }
        else if (slucajniAtributi[i] == 5) {
            noveVrijednosti.push(dajSlucajniInteger(1, document.getElementById("glavni").offsetHeight - elementiAtributi[i][2] - 1));
        }
    }
    return noveVrijednosti;
}

function dajSlucajniString(duzina) {
    var string = '';
    var znakovi = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (var i = 0; i < duzina; i++) {
        string += znakovi.charAt(Math.floor(Math.random() * znakovi.length));
    }
    return string;
}