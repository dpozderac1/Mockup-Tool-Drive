package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Seeder {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public Seeder(RoleRepository roleRepository,UserRepository userRepository,ProjectRepository projectRepository){
        this.roleRepository=roleRepository;
        this.userRepository=userRepository;
        this.projectRepository=projectRepository;
    }

    @EventListener
    public void dodaj(ApplicationReadyEvent event){
        Role admin=new Role();
        admin.setRole_name(RoleNames.ADMIN);
        if(!roleRepository.existsByroleName(RoleNames.ADMIN))
            roleRepository.save(admin);
        Role user=new Role();
        user.setRole_name(RoleNames.USER);
        if(!roleRepository.existsByroleName(RoleNames.USER))
            roleRepository.save(user);

        Role uloga=roleRepository.findByroleName(RoleNames.ADMIN);
        User korisnik=new User(uloga,"Damir","Pozderac","dpozderac1","passsword","dpozderac1@etf.unsa.ba");
        if(!userRepository.existsByUsername(korisnik.getUsername()))
            userRepository.save(korisnik);

        /*Project projekat=new Project();
        projectRepository.save(projekat);*/
    }
}
