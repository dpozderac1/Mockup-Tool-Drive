package com.example.demo.Services;

import com.example.demo.RabbitMQ.BindingInterfaceInput;
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
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.*;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GSPEC_DocumentService implements GSPEC_DocumentServiceInterface {
    private GSPEC_DocumentRepository gspec_documentRepository;
    private MockupRepository mockupRepository;
    private MessageRabbitMq messageRabbitMq = new MessageRabbitMq(0L, Command.START);
    private MessageChannel greet;

    public GSPEC_DocumentService(MessageRabbitMq messageRabbitMq) {
        this.messageRabbitMq = messageRabbitMq;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public GSPEC_DocumentService(GSPEC_DocumentRepository gspec_documentRepository, MockupRepository mockupRepository, BindingInterface binding) {
        this.gspec_documentRepository = gspec_documentRepository;
        this.mockupRepository = mockupRepository;
        this.greet = binding.greeting();
    }

    //RabbitMQ
    @StreamListener(target = BindingInterfaceInput.GREETING)
    public void processHelloChannelGreeting(MessageRabbitMq msg) throws JSONException {
        System.out.println(msg.getCommand());
        messageRabbitMq = msg;
        deletegspec(msg);
    }

    @Override
    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id){
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        HttpEntity<GSPEC_Document> request = new HttpEntity<>(newGspec);
        restTemplate.put("http://online-testing/updateGSPECDocument/{id}", request, id);
        if(gspec_document != null) {
            Mockup mockup = mockupRepository.findByID(gspec_document.getMockupId().getID());
            if(mockup != null) {
                if (newGspec.getMockupId() != null && mockupRepository.findByID(newGspec.getMockupId().getID()) != null) {
                    gspec_document.setMockupId(mockup);
                }

                if (!newGspec.getName().equals("")) gspec_document.setName(newGspec.getName());

                if (newGspec.getDate_created() != null) gspec_document.setDate_created(newGspec.getDate_created());
                if (newGspec.getDate_modified() != null) gspec_document.setDate_modified(newGspec.getDate_modified());
                if (newGspec.getFile() != null) gspec_document.setFile(newGspec.getFile());
                if (newGspec.getAccessed_date() != null) gspec_document.setAccessed_date(newGspec.getAccessed_date());

                gspec_documentRepository.save(gspec_document);
                gspec_document.setFile(null);
                gspec_document.getMockupId().setFile(null);
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
                newGspec.setFile(null);
                newGspec.getMockupId().setFile(null);
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
            for(GSPEC_Document gspec:gspec_documents){
                gspec.setFile(null);
                gspec.getMockupId().setFile(null);
            }
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
    public ResponseEntity deleteOneGSPEC(Long id) throws JSONException {
        //restTemplate.delete("http://online-testing/GSPECDocument/{id}", id);
        if(gspec_documentRepository.existsById(id)){
            Command poruka = Command.SUCCESS;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
        }
        else{
            Command poruka = Command.FAIL;
            MessageRabbitMq messageRabbitMq = new MessageRabbitMq(id, poruka);
            Message<MessageRabbitMq> msg = MessageBuilder.withPayload(messageRabbitMq).build();
            this.greet.send(msg);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","GSPEC document does not exit");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
        }

        while (true){
            if(messageRabbitMq.getCommand().equals(Command.FINAL)) {
                messageRabbitMq.setCommand(Command.START);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message","GSPEC document successfully deleted!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }
            if(messageRabbitMq.getCommand().equals(Command.FAIL)) {
                messageRabbitMq.setCommand(Command.START);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message","GSPEC document does not exit");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
            }
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
                gspec_document.setFile(null);
                gspec_document.getMockupId().setFile(null);
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
        if(gspec_documents != null) {
            for(GSPEC_Document gspec:gspec_documents){
                gspec.setFile(null);
                gspec.getMockupId().setFile(null);
            }
            return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("GSPEC documents do not exist!");
    }

    @Override
    public ResponseEntity getOneGSPEC(Long id) {
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null){
            gspec_document.setFile(null);
            gspec_document.getMockupId().setFile(null);
            return new ResponseEntity<>(gspec_document, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("GSPEC document with id " + id + "does not exist!");
    }

    @Override
    public ResponseEntity addGSPECFile(Long id, MultipartFile gspecFajl, String naziv, HttpServletRequest httpServletRequest) throws IOException, SQLException {
        System.out.println("Usao sam u addGPSECFile");
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            Blob blob=new SerialBlob(gspecFajl.getBytes());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();
            GSPEC_Document gspec_document=new GSPEC_Document(mockup,naziv,blob,date,date,date);
            GSPEC_Document gspec_documentZaOnlineTesting=new GSPEC_Document(null,naziv,null,date,date,date);

            //gspec_documentZaOnlineTesting.getMockupId().setFile(null);
            HttpEntity<GSPEC_Document> request = new HttpEntity<>(gspec_documentZaOnlineTesting);
            GSPEC_Document gspec_document11 = restTemplate.postForObject("http://online-testing/addGSPECDocument", request, GSPEC_Document.class);

            Long idOnlineTestiranje=gspec_document11.getID();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name("gspecFile")
                    .filename("filename")
                    .build();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(gspecFajl.getBytes(), fileMap);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("gspecFile",fileEntity);
            HttpEntity<MultiValueMap<String, Object>> requestEntity =new HttpEntity<>(body, headers);
            System.out.println(gspecFajl.getBytes().length);
            restTemplate.put("http://online-testing/changeGSPECFile/"+idOnlineTestiranje.toString(), requestEntity);

            gspec_documentRepository.save(gspec_document);


            JSONObject objekat = new JSONObject();
            try {
                objekat.put("message","GSPEC file is successfully updated!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(objekat.toString(), HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
        }
    }

    @Override
    public ResponseEntity<?> getOneGSPECFile(Long id) throws SQLException {
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null){
            Blob blob=gspec_document.getFile();
            String rezultat="";
            if(blob!=null) {
                byte[] niz = blob.getBytes(1l, (int) blob.length());
                blob.free();
                rezultat = new String(niz, StandardCharsets.UTF_8);
            }
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("GSPEC document with id " + id + "does not exist!");
    }

}
