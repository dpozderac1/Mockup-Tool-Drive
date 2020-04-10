package com.example.demo;

import com.example.demo.ErrorHandling.AlreadyExistsException;
import com.example.demo.ErrorHandling.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (clientHttpResponse.getStatusCode().series() == CLIENT_ERROR || clientHttpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        System.out.println("Doslo je do greske!");
        if (clientHttpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            String tijelo= StreamUtils.copyToString(clientHttpResponse.getBody(), Charset.defaultCharset());
            int pozicija=tijelo.indexOf("[\"");
            int pozicija1=tijelo.indexOf("\"]");
            String poruka=tijelo.substring(pozicija+2,pozicija1);
            throw new RecordNotFoundException(poruka);
        }
        else if(clientHttpResponse.getStatusCode()== HttpStatus.CONFLICT){
            String tijelo=StreamUtils.copyToString(clientHttpResponse.getBody(), Charset.defaultCharset());
            int pozicija=tijelo.indexOf("[\"");
            int pozicija1=tijelo.indexOf("\"]");
            String poruka=tijelo.substring(pozicija+2,pozicija1);
            throw new AlreadyExistsException(poruka);
        }
    }
}