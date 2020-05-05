package com.example.routingandfilteringgateway;

import com.example.routingandfilteringgateway.models.AuthenticationRequest;
import com.example.routingandfilteringgateway.models.AuthenticationResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

import java.security.SignatureException;

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

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            /*authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );*/
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            /*Authentication authentication = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);*/
            System.out.println("OKURRRRRRRR!!!!!!!!!!!");
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        System.out.println("Username je: ");
        System.out.println(authenticationRequest.getUsername());
        UserDetails userDetails=null;
        try {
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        }
        catch (UsernameNotFoundException e){
            JSONObject jo=new JSONObject();
            jo.put("message","Username not found!");
            //HttpHeaders zaglavlja=new HttpHeaders();
            //zaglavlja.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity odgovor=new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
            return odgovor;
        }
        //System.out.println("OKURRRRRRRR!!!!!!!!!!!");
        String jwt="";
        try {
            jwt = jwtTokenUtil.generateToken(userDetails);
        }
        catch (MalformedJwtException e){
            JSONObject jo=new JSONObject();
            jo.put("message","Invalid token!");
            return new ResponseEntity(jo.toString(), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
