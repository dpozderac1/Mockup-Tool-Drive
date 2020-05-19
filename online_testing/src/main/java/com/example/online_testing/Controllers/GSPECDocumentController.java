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

import javax.validation.Valid;

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
    ResponseEntity deleteGSPECDocument(@PathVariable Long id) {
        return gspecDocumentService.deleteGSPECDocumentByID(id);
    }

    @PutMapping("/updateGSPECDocument/{id}")
    ResponseEntity updateGSPECDocument (@Valid @RequestBody GSPECDocument gspecDocument, @PathVariable Long id) {
        return gspecDocumentService.updateGSPECDocument(gspecDocument, id);
    }

    @PostMapping("/addGSPECDocument")
    ResponseEntity addGSPECDocument(@Valid @RequestBody GSPECDocument gspecDocument) {
        return gspecDocumentService.saveGSPECDocument(gspecDocument);
    }

}
