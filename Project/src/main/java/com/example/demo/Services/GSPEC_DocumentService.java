package com.example.demo.Services;

import com.example.demo.RabbitMQ.Command;
import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.RabbitMQ.MessageRabbitMq;
import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.RabbitMQ.BindingInterface;
import com.example.demo.ServisInterfaces.GSPEC_DocumentServiceInterface;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GSPEC_DocumentService implements GSPEC_DocumentServiceInterface {
    private GSPEC_DocumentRepository gspec_documentRepository;
    private MockupRepository mockupRepository;

    private MessageChannel greet;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public GSPEC_DocumentService(GSPEC_DocumentRepository gspec_documentRepository, MockupRepository mockupRepository, BindingInterface binding) {
        this.gspec_documentRepository = gspec_documentRepository;
        this.mockupRepository = mockupRepository;
        this.greet = binding.greeting();
    }

    @Override
    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id){
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        HttpEntity<GSPEC_Document> request = new HttpEntity<>(newGspec);
        restTemplate.put("http://online-testing/updateGSPECDocument/{id}", request, id);
        if(gspec_document != null) {
            Mockup mockup = mockupRepository.findByID(newGspec.getMockupId().getID());
            if(mockup != null) {
                gspec_document.setMockupId(mockup);

                if (!newGspec.getName().equals("")) gspec_document.setName(newGspec.getName());

                if (newGspec.getDate_created() != null) gspec_document.setDate_created(newGspec.getDate_created());
                if (newGspec.getDate_modified() != null) gspec_document.setDate_modified(newGspec.getDate_modified());
                if (newGspec.getFile() != null) gspec_document.setFile(newGspec.getFile());
                if (newGspec.getAccessed_date() != null) gspec_document.setAccessed_date(newGspec.getAccessed_date());

                gspec_documentRepository.save(gspec_document);
                return new ResponseEntity<>(gspec_document, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newGspec.getMockupId().getID() + " does not exist!");
        }
        else{
            Mockup mockup = mockupRepository.findByID(newGspec.getMockupId().getID());
            if(mockup != null){
                newGspec.setMockupId(mockup);
                newGspec.setID(id);
                gspec_documentRepository.save(newGspec);
                return new ResponseEntity<>(newGspec, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newGspec.getMockupId().getID() + " does not exist!");
        }
    }

    @Override
    public ResponseEntity allGSPECsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null) {
            List<GSPEC_Document> gspec_documents = gspec_documentRepository.findAllBymockupID(mockup);
            if(gspec_documents != null){
                return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("GSPEC document with mockup with id " + id + "do not exist!");
        }
        else
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
    }

    @Override
    public void deletegspec(MessageRabbitMq msg) throws JSONException {
        if(msg.getCommand().equals(Command.FINAL)){
            gspec_documentRepository.deleteById(msg.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","GSPEC document successfully deleted!");
        }
    }

    @Override
    public void deleteOneGSPEC(Long id) throws JSONException {
        //restTemplate.delete("http://online-testing/GSPECDocument/{id}", id);
        if(gspec_documentRepository.existsById(id)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","GSPEC document successfully deleted!");
            Command poruka = Command.SUCCESS;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
            //return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        else{
            Command poruka = Command.FAIL;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
            //throw new ObjectNotFoundException("GSPEC document with id " + id + " does not exit!");
        }
    }

    @Override
    public ResponseEntity newGSPEC(GSPEC_Document newGspec){
        HttpEntity<GSPEC_Document> request = new HttpEntity<>(newGspec);
        GSPEC_Document gspec_document11 = restTemplate.postForObject("http://online-testing/addGSPECDocument", request, GSPEC_Document.class);

        List<GSPEC_Document> gspec_documents  = gspec_documentRepository.findAll();
        boolean alreadyExists = false;
        for(GSPEC_Document m: gspec_documents){
            if(m.getID().equals(newGspec.getID())) alreadyExists = true;
        }
        if(!alreadyExists) {
            Mockup mockup = mockupRepository.findByID(newGspec.getMockupId().getID());
            if(mockup != null) {
                GSPEC_Document gspec_document = gspec_documentRepository.save(newGspec);
                return new ResponseEntity<>(gspec_document, HttpStatus.CREATED);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newGspec.getMockupId().getID() + " does not exist!");
        }
        else
            throw new ObjectAlreadyExistsException("GSPEC document with id " + newGspec.getID() + " already exists!");
    }

    @Override
    public ResponseEntity getAllGSPECs(){
        List<GSPEC_Document> gspec_documents = gspec_documentRepository.findAll();
        if(gspec_documents != null)
            return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("GSPEC documents do not exist!");
    }

    @Override
    public ResponseEntity getOneGSPEC(Long id) {
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null){
            return new ResponseEntity<>(gspec_document, HttpStatus.OK);
        }
        else
        throw new ObjectNotFoundException("GSPEC document with id " + id + "does not exist!");
    }

}
