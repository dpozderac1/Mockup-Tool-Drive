package com.example.online_testing.Services;

import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.RabbitMQ.MessageRabbitMq;
import org.springframework.http.ResponseEntity;

public interface GSPECDocumentService {

    ResponseEntity deleteGSPECDocumentByID(Long id);
    ResponseEntity updateGSPECDocument(GSPECDocument gspecDocument, Long id);
    ResponseEntity saveGSPECDocument(GSPECDocument gspecDocument);
}
