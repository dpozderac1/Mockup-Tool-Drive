function print(quality = 1) {
    var naziv = document.getElementById("nepotrebniPDF");
    var filename = 'mockup.pdf';
    if (naziv != null && naziv != undefined && naziv !== "") {
        filename = naziv.getAttribute("name") + ".pdf";
    }

    html2canvas(document.querySelector('#glavni'), { scale: quality }).then(function (canvas) {
        //let pdf = new jsPDF('p', 'mm', 'a4');
        let pdf = new jsPDF('landscape');
        pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 10, 10);
        pdf.save(filename);
    });

}