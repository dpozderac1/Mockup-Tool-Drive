function print(quality = 1) {
    const filename  = 'mockup.pdf';

    html2canvas(document.querySelector('#glavni'), {scale: quality}).then(function(canvas) {
        //let pdf = new jsPDF('p', 'mm', 'a4');
        let pdf = new jsPDF('landscape');
        pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 10, 30, 250, 130);
        pdf.save(filename);
    });

}