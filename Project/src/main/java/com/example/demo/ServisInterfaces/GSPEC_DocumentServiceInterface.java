package com.example.demo.ServisInterfaces;

import com.example.demo.RabbitMQ.MessageRabbitMq;
import com.example.demo.Models.GSPEC_Document;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public interface GSPEC_DocumentServiceInterface {

    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id);
    public ResponseEntity allGSPECsOfMockup(Long id);
    public ResponseEntity deleteOneGSPEC(Long id) throws JSONException;
    public ResponseEntity newGSPEC(GSPEC_Document newGspec);
    public ResponseEntity getAllGSPECs();
    public ResponseEntity getOneGSPEC(Long id);
    public void deletegspec(MessageRabbitMq msg) throws JSONException;

    public ResponseEntity addGSPECFile(Long id, MultipartFile gspecFajl, String naziv, HttpServletRequest request) throws IOException, SQLException;

    ResponseEntity<?> getOneGSPECFile(Long id) throws SQLException;
}
