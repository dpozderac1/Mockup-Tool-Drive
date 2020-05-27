function poravnajLijevo() {
    if (trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.dataset.poravnanje="lijevo";
  
      if (trenutnoOznaceniElement.title == "Table") {
  
        for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells.length; j++) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].align = "left";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].style["padding-left"] = 4 + "px";
        }
  
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows.length; i++) {
          for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells.length; j++) {
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].align = "left";
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].style["padding-left"] = 4 + "px";
          }
        }
  
  
      }
      else if (trenutnoOznaceniElement.title == "Vertical List") {
        for (var i = 1; i < trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes.length; i = i + 2) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style.textAlign = "left";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style["padding-left"] = 10 + "px";
  
        }
  
      }
      else {
        var indeks = "";
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes.length; i++) {
          if (trenutnoOznaceniElement.childNodes[i].nodeName == "P") indeks = i;
  
          if (indeks != "") {
            trenutnoOznaceniElement.childNodes[indeks].style.textAlign = "left";
            trenutnoOznaceniElement.childNodes[indeks].style["padding-left"] = 10 + "px";
          }
        }
      }
    }
  }
  
  function poravnajDesno() {
    if (trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.dataset.poravnanje="desno";
  
      if (trenutnoOznaceniElement.title == "Table") {
  
  
        for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells.length; j++) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].align = "right";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].style["padding-right"] = 4 + "px";
        }
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows.length; i++) {
          for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells.length; j++) {
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].align = "right";
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].style["padding-right"] = 4 + "px";
          }
        }
  
  
      }
      else if (trenutnoOznaceniElement.title == "Vertical List") {
        console.log("uslo")
        for (var i = 1; i < trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes.length; i = i + 2) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style.textAlign = "right";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style["padding-right"] = 10 + "px";
  
        }
  
      }
      else {
  
        var indeks = "";
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes.length; i++) {
          if (trenutnoOznaceniElement.childNodes[i].nodeName == "P") indeks = i;
  
          if (indeks != "") {
            trenutnoOznaceniElement.childNodes[indeks].style.textAlign = "right";
            trenutnoOznaceniElement.childNodes[indeks].style["padding-right"] = 10 + "px";
          }
        }
      }
    }
  }
  
  function centriraj() {
    if (trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.dataset.poravnanje="centar";
  
      if (trenutnoOznaceniElement.title == "Table") {
  
  
        for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells.length; j++) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].align = "center";
  
        }
  
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows.length; i++) {
          for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells.length; j++) {
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].align = "center";
  
          }
        }
  
  
      }
      else if (trenutnoOznaceniElement.title == "Vertical List") {
  
        for (var i = 1; i < trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes.length; i = i + 2) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style.textAlign = "center";
  
        }
  
      }
      else {
  
        var indeks = "";
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes.length; i++) {
          if (trenutnoOznaceniElement.childNodes[i].nodeName == "P") indeks = i;
          if (indeks != "") trenutnoOznaceniElement.childNodes[indeks].style.textAlign = "center";
        }
      }
    }
  }
  
  function justify() {
    if (trenutnoOznaceniElement != null) {
      trenutnoOznaceniElement.dataset.poravnanje="justify";
  
      if (trenutnoOznaceniElement.title == "Table") {
  
  
        for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells.length; j++) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].align = "justify";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].rows[0].cells[j].style["padding-left"] = 4 + "px";
        }
  
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows.length; i++) {
          for (var j = 0; j < trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells.length; j++) {
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].align = "justify";
            trenutnoOznaceniElement.childNodes[1].childNodes[3].rows[i].cells[j].style["padding-left"] = 4 + "px";
          }
        }
  
  
      }
      else if (trenutnoOznaceniElement.title == "Vertical List") {
  
        for (var i = 1; i < trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes.length; i = i + 2) {
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style.textAlign = "justify";
          trenutnoOznaceniElement.childNodes[1].childNodes[1].childNodes[1].childNodes[i].style["padding-left"] = 10 + "px";
  
        }
  
      }
      else {
  
        var indeks = "";
  
        for (var i = 0; i < trenutnoOznaceniElement.childNodes.length; i++) {
          if (trenutnoOznaceniElement.childNodes[i].nodeName == "P") indeks = i;
  
          if (indeks != "") {
            trenutnoOznaceniElement.childNodes[indeks].style.textAlign = "justify";
            trenutnoOznaceniElement.childNodes[indeks].style["padding-left"] = 10 + "px";
          }
        }
      }
    }
  }