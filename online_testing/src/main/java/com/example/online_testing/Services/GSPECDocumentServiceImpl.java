package com.example.online_testing.Services;

import com.example.online_testing.RabbitMQ.BindingInterfaceOutput;
import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.RabbitMQ.MessageRabbitMq;
import com.example.online_testing.RabbitMQ.Command;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GSPECDocumentServiceImpl implements GSPECDocumentService {

    private MessageChannel greet;

    @Autowired
    private GSPECDocumentRepository gspecDocumentRepository;


    @Autowired
    private RestTemplate restTemplate;

    public GSPECDocumentServiceImpl(BindingInterfaceOutput binding) {
        this.greet = binding.greeting();
    }

    @Override
    public ResponseEntity deleteGSPECDocumentByID(Long id) {
        JSONObject jo = new JSONObject();
        if(gspecDocumentRepository.existsByID(id)) {
            gspecDocumentRepository.deleteById(id);
            jo.put("message", "GSPEC Document is successfully deleted!");
            Command poruka = Command.FINAL;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        else{
            Command poruka = Command.FAIL;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
            jo.put("message", "GSPEC Document is not found!");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
            //throw new RecordNotFoundException("GSPEC Document does not exist!");
        }
    }

    @Override
    public ResponseEntity updateGSPECDocument(GSPECDocument gspecDocument, Long id) {
        GSPECDocument oldGspecDocument = gspecDocumentRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(oldGspecDocument == null) {
            throw new RecordNotFoundException("The GSPEC Document you want to update does not exist!");
        }
        else {
            GSPECDocument newGspecDocument = new GSPECDocument(oldGspecDocument.getName(), oldGspecDocument.getFile());
            if(!gspecDocument.getName().isEmpty()) {
                newGspecDocument.setName(gspecDocument.getName());
            }
            if(gspecDocument.getFile() != null) {
                newGspecDocument.setFile(gspecDocument.getFile());
            }
            List<GSPECDocument> gspecDocuments = gspecDocumentRepository.findAll();
            boolean postoji = false;
            for (GSPECDocument g: gspecDocuments) {
                if(g.getName().equals(newGspecDocument.getName()) && g.getFile() != null && newGspecDocument.getFile() != null && (g.getFile() == newGspecDocument.getFile()))  {
                    postoji = true;
                }
            }
            if(!postoji) {
                oldGspecDocument.setName(newGspecDocument.getName());
                oldGspecDocument.setFile(newGspecDocument.getFile());
                gspecDocumentRepository.save(oldGspecDocument);
            }
            else {
                throw new AlreadyExistsException("GSPEC DOcument already exists!");
            }
        }
        return new ResponseEntity(oldGspecDocument, HttpStatus.OK);
    }

    @Override
    public ResponseEntity saveGSPECDocument(GSPECDocument gspecDocument) {
        JSONObject jo = new JSONObject();
        GSPECDocument newGspecDocument = new GSPECDocument(gspecDocument.getName(), gspecDocument.getFile());
        List<GSPECDocument> gspecDocuments = gspecDocumentRepository.findAll();
        boolean postoji = false;
        for (GSPECDocument g: gspecDocuments) {
            if(g.getName().equals(newGspecDocument.getName()) && g.getFile() != null && newGspecDocument.getFile() != null && (g.getFile() == newGspecDocument.getFile()))  {
                postoji = true;
            }
        }
        if(!postoji) gspecDocumentRepository.save(newGspecDocument);
        else {
            throw new AlreadyExistsException("GSPEC Document already exists!");
        }
        jo.put("message", "GSPEC Document is successfully added!");
        return new ResponseEntity(newGspecDocument, HttpStatus.CREATED);
    }
}
