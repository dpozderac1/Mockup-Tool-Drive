package com.example.routingandfilteringgateway;

import com.example.routingandfilteringgateway.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Tu sam!!!!!");
        User user=null;
        try {
            user= restTemplate.getForObject("http://user/user/username/" + s, User.class);
        }
        catch(HttpClientErrorException e){
            throw new UsernameNotFoundException(s);
        }
        System.out.println("Korisnik je: ");
        System.out.println(user.getRoleID().getRole_name().toString());
        return user;
    }
}
