package com.example.routingandfilteringgateway;

import com.example.routingandfilteringgateway.models.AuthenticationRequest;
import com.example.routingandfilteringgateway.models.AuthenticationResponse;
import com.example.routingandfilteringgateway.models.Message;
import com.example.routingandfilteringgateway.util.JwtUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.security.SignatureException;

@CrossOrigin
@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @RequestMapping(value="/check",method=RequestMethod.GET)
    public void check(){
        System.out.println("Usao sam u check!");
    }

    @RequestMapping(value = "/getUser/{token}", method = RequestMethod.GET)
    public ResponseEntity returnUser(@PathVariable String token) throws Exception {
        String username = "";
        UserDetails userDetails = null;
        try {
            username = jwtTokenUtil.extractUsername(token);
            userDetails = userDetailsService.loadUserByUsername(username);
        }
        catch (Exception e){
            Message poruka = new Message("Token invalid!");
            return new ResponseEntity(poruka, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(userDetails, HttpStatus.OK);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        UserDetails userDetails=null;
        try {
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        }
        catch (UsernameNotFoundException e){
            /*JSONObject jo=new JSONObject();
            jo.put("message","Username not found!");*/
            Message poruka=new Message("Username not found");
            ResponseEntity odgovor=new ResponseEntity(poruka, HttpStatus.NOT_FOUND);
            return odgovor;
        }
        String password = authenticationRequest.getPassword();
        //if(!password.equals(userDetails.getPassword())){
        if(!BCrypt.checkpw(password, userDetails.getPassword())){
            Message poruka=new Message("Invalid password!");
            return new ResponseEntity(poruka, HttpStatus.FORBIDDEN);
        }
        String jwt="";
        try {
            jwt = jwtTokenUtil.generateToken(userDetails);
        }
        catch (MalformedJwtException e){
            Message poruka=new Message("Invalid token!");
            return new ResponseEntity(poruka, HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
