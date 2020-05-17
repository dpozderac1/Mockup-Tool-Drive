package com.example.demo.ServisInterfaces;

import com.example.demo.RabbitMQ.MessageRabbitMq;
import com.example.demo.Models.GSPEC_Document;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

public interface GSPEC_DocumentServiceInterface {

    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id);
    public ResponseEntity allGSPECsOfMockup(Long id);
    public void deleteOneGSPEC(Long id) throws JSONException;
    public ResponseEntity newGSPEC(GSPEC_Document newGspec);
    public ResponseEntity getAllGSPECs();
    public ResponseEntity getOneGSPEC(Long id);
    public void deletegspec(MessageRabbitMq msg) throws JSONException;
}
