package com.example.online_testing.Controllers;

import com.example.online_testing.RabbitMQ.BindingInterface;
import com.example.online_testing.RabbitMQ.MessageRabbitMq;
import com.example.online_testing.RabbitMQ.Command;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import com.example.online_testing.Services.GSPECDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@EnableBinding(BindingInterface.class)
//@CrossOrigin
@RestController
public class GSPECDocumentController {

    @Autowired
    private GSPECDocumentService gspecDocumentService;

    @Autowired
    private GSPECDocumentRepository gspecDocumentRepository;
    private GSPECDocumentRepository gspecDocumentRepositoryCopy = gspecDocumentRepository;


    @StreamListener(target = BindingInterface.GREETING)
    public void processHelloChannelGreeting(MessageRabbitMq msg) {
        if(msg.getCommand().equals(Command.SUCCESS)){
            gspecDocumentService.deleteGSPECDocumentByID(msg.getId());
        }
        System.out.println(msg.getCommand());
    }

    @Autowired
    public GSPECDocumentController(GSPECDocumentService gspec_documentService) {
        this.gspecDocumentService = gspec_documentService;
    }


    @DeleteMapping(value = "/GSPECDocument/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteGSPECDocument(@PathVariable Long id) {
        return gspecDocumentService.deleteGSPECDocumentByID(id);
    }

    @PutMapping("/updateGSPECDocument/{id}")
    public ResponseEntity updateGSPECDocument (@Valid @RequestBody GSPECDocument gspecDocument, @PathVariable Long id) {
        return gspecDocumentService.updateGSPECDocument(gspecDocument, id);
    }

    @PostMapping("/addGSPECDocument")
    public ResponseEntity addGSPECDocument(@Valid @RequestBody GSPECDocument gspecDocument) {
        return gspecDocumentService.saveGSPECDocument(gspecDocument);
    }

    @PutMapping(value="/changeGSPECFile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity changeGSPECFile (@RequestParam("gspecFile") MultipartFile gspecFile, @PathVariable Long id) throws IOException, SQLException {
        return gspecDocumentService.changeGSPECFile(gspecFile, id);
    }

}
