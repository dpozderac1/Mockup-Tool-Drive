package com.example.demo.Seeders;

import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        User korisnik=new User(uloga,"Damir","Pozderac","dpozderac1",passwordEncoder.encode("Password1!"),"dpozderac1@etf.unsa.ba");


        /*Role uloga1=roleRepository.findByroleName(RoleNames.USER);
        User korisnik1=new User(uloga1,"Edina","Kovac","ekovac2","Sifra22+","ekovac2@etf.unsa.ba");
        if(!userRepository.existsByUsername(korisnik1.getUsername()))
            userRepository.save(korisnik1);
*/
        Role uloga2=roleRepository.findByroleName(RoleNames.USER);
        User korisnik2=new User(uloga2,"Zerina","Ramic","zramic1", passwordEncoder.encode("Nesto!!25"),"zramic1@gmail.com");
        /*if(!userRepository.existsByUsername(korisnik2.getUsername()))
            userRepository.save(korisnik2);*/



        /*Role uloga3=roleRepository.findByroleName(RoleNames.USER);
        User korisnik3=new User(uloga3,"Admir","Pozderac","apozderac1","Nesto!!25","apozderac1@gmail.com");*/

        Project projekat=new Project();
        Project projekat1=new Project();

        projectRepository.save(projekat);
        projectRepository.save(projekat1);

        korisnik.getProjects().add(projekat);
        korisnik2.getProjects().add(projekat1);

        projekat.getUsers().add(korisnik);
        projekat1.getUsers().add(korisnik2);

        if(!userRepository.existsByUsername(korisnik.getUsername()))
            userRepository.save(korisnik);
        userRepository.save(korisnik2);
    }
}
