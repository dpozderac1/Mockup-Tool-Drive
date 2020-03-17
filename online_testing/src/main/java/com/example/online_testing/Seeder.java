package com.example.online_testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;

@Component
public class Seeder {

    private Role_repository role_repository;
    private User_repository user_repository;
    private Server_repository server_repository;
    private Online_test_repository online_test_repository;
    private GSPEC_Document_repository gspec_document_repository;
    private Browser_repository browser_repository;

    @Autowired
    public Seeder(Role_repository role_repository, User_repository user_repository, Server_repository server_repository, Online_test_repository online_test_repository, GSPEC_Document_repository gspec_document_repository, Browser_repository browser_repository) {
        this.role_repository = role_repository;
        this.user_repository = user_repository;
        this.server_repository = server_repository;
        this.online_test_repository = online_test_repository;
        this.gspec_document_repository = gspec_document_repository;
        this.browser_repository = browser_repository;
    }

    @EventListener
    public void insert_in_database (ApplicationReadyEvent event){
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
        User user1 = new User(admin, "zramic1", "123456789", "zramic1@etf.unsa.ba");
        if(!user_repository.existsByusername(user1.getUsername())) user_repository.save(user1);

        Role user = role_repository.findByroleName(RoleNames.USER);
        User user2 = new User(user, "dpozderac1", "0000", "dpozderac1@etf.unsa.ba");
        if(!user_repository.existsByusername(user2.getUsername())) user_repository.save(user2);
    }

    private void insert_server() {
        User user1 = user_repository.findByID(Long.valueOf(1));
        Server server1 = new Server("http://nekiserver.com", 3306, 1, user1);
        if(!server_repository.existsByurl(server1.getUrl())) server_repository.save(server1);

        Server server2 = new Server("http://nekiserver2.com", 3310, 1, user1);
        if(!server_repository.existsByurl(server2.getUrl())) server_repository.save(server2);
    }

    private void insert_GSPEC_document() {
        GSPEC_Document document1 = new GSPEC_Document("Document1", null);
        if(!gspec_document_repository.existsByname(document1.getName())) gspec_document_repository.save(document1);

        GSPEC_Document document2 = new GSPEC_Document("Document2", null);
        if(!gspec_document_repository.existsByname(document2.getName())) gspec_document_repository.save(document2);
    }

    private void insert_online_test() {
        Server server = server_repository.findByID(Long.valueOf(1));
        GSPEC_Document gspec_document = gspec_document_repository.findByID(Long.valueOf(1));
        User user = user_repository.findByID(Long.valueOf(1));

        Online_test online_test1 = new Online_test("Test1", null, server, user, gspec_document);
        if(!online_test_repository.existsBytests(online_test1.getTests())) online_test_repository.save(online_test1);

        GSPEC_Document gspec_document2 = gspec_document_repository.findByID(Long.valueOf(2));
        Online_test online_test2 = new Online_test("Test2", null, server, user, gspec_document2);
        if(!online_test_repository.existsBytests(online_test2.getTests())) online_test_repository.save(online_test2);
    }

    private void insert_browser() {
        Server server1 = server_repository.findByID(Long.valueOf(1));
        Browser browser1 = new Browser("Mozila Firefox", server1);
        browser_repository.save(browser1);

        Server server2 = server_repository.findByID(Long.valueOf(2));
        Browser browser2 = new Browser("Google Chrome", server2);
        browser_repository.save(browser2);
    }
}
