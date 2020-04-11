package com.example.online_testing.Seeders;

import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class Seeder {

    private RoleRepository role_repository;
    private UserRepository user_repository;
    private ServerRepository server_repository;
    private OnlineTestRepository online_test_repository;
    private GSPECDocumentRepository gspec_document_repository;
    private BrowserRepository browser_repository;

    @Autowired
    public Seeder(RoleRepository role_repository, UserRepository user_repository, ServerRepository server_repository, OnlineTestRepository online_test_repository, GSPECDocumentRepository gspec_document_repository, BrowserRepository browser_repository) {
        this.role_repository = role_repository;
        this.user_repository = user_repository;
        this.server_repository = server_repository;
        this.online_test_repository = online_test_repository;
        this.gspec_document_repository = gspec_document_repository;
        this.browser_repository = browser_repository;
    }

    @EventListener
    public void insert_in_database (ApplicationReadyEvent event) throws SQLException {
        insert_role();
        insert_user();
        insert_server();
        insert_GSPEC_document();
        insert_online_test();
        insert_browser();
    }


    private void insert_role() {
        Role admin = new Role();
        admin.setRole_name(RoleNames.ADMIN);
        if(!role_repository.existsByroleName(RoleNames.ADMIN))role_repository.save(admin);

        Role user = new Role();
        user.setRole_name(RoleNames.USER);
        if(!role_repository.existsByroleName(RoleNames.USER))role_repository.save(user);
    }

    private void insert_user() {
        Role admin = role_repository.findByroleName(RoleNames.ADMIN);
        User user1 = new User(admin, "dpozderac1", "dpozderac1@etf.unsa.ba");
        if(!user_repository.existsByusername(user1.getUsername())) user_repository.save(user1);

        Role user = role_repository.findByroleName(RoleNames.USER);
        User user2 = new User(user, "zramic1", "zramic1@gmail.com");
        if(!user_repository.existsByusername(user2.getUsername())) user_repository.save(user2);
    }

    private void insert_server() {
        User user1 = user_repository.findByID(Long.valueOf(1));
        Server server1 = new Server("http://nekiserver1.com", 3306, "1", user1);
        if(!server_repository.existsByurl(server1.getUrl())) server_repository.save(server1);

        Server server2 = new Server("http://nekiserver2.com", 3310, "1", user1);
        if(!server_repository.existsByurl(server2.getUrl())) server_repository.save(server2);
    }

    private void insert_GSPEC_document() throws SQLException {
        GSPECDocument document1 = new GSPECDocument("AGspec 1", null);
        if(!gspec_document_repository.existsByname(document1.getName())) gspec_document_repository.save(document1);

        GSPECDocument document2 = new GSPECDocument("Gspec 2", null);
        if(!gspec_document_repository.existsByname(document2.getName())) gspec_document_repository.save(document2);
    }

    private void insert_online_test() {
        Server server = server_repository.findByID(Long.valueOf(1));
        GSPECDocument gspec_document = gspec_document_repository.findByID(Long.valueOf(1));
        User user = user_repository.findByID(Long.valueOf(1));

        OnlineTest online_test1 = new OnlineTest("Test1", null, server, user, gspec_document);
        if(!online_test_repository.existsBytests(online_test1.getTests())) online_test_repository.save(online_test1);

        GSPECDocument gspec_document2 = gspec_document_repository.findByID(Long.valueOf(2));
        OnlineTest online_test2 = new OnlineTest("Test2", null, server, user, gspec_document2);
        if(!online_test_repository.existsBytests(online_test2.getTests())) online_test_repository.save(online_test2);
    }

    private void insert_browser() {
        Server server1 = server_repository.findByID(Long.valueOf(1));
        Browser browser1 = new Browser("Mozila Firefox", server1, "Mobile");

        Server server2 = server_repository.findByID(Long.valueOf(2));
        Browser browser2 = new Browser("Google Chrome", server2, "Desktop");

        List<Browser> browsers = browser_repository.findAll();
        boolean postoji1 = false;
        boolean postoji2 = false;
        for (Browser b : browsers) {
            if(b.getName().equals(browser1.getName()) && b.getVersion().equals(browser1.getVersion()) && b.getIdServer() == browser1.getIdServer())  {
                postoji1 = true;
            }
            if(b.getName().equals(browser2.getName()) && b.getVersion().equals(browser2.getVersion()) && b.getIdServer() == browser2.getIdServer())  {
                postoji2 = true;
            }
        }
        if(!postoji1) browser_repository.save(browser1);
        if(!postoji2) browser_repository.save(browser2);



    }
}
