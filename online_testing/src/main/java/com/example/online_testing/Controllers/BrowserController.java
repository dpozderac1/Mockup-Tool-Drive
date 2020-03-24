package com.example.online_testing.Controllers;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.BrowserRepository;
import com.example.online_testing.Repositories.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class BrowserController {

    private BrowserRepository browserRepository;
    private ServerRepository serverRepository;

    @Autowired
    public BrowserController(BrowserRepository browserRepository, ServerRepository serverRepository) {
        this.browserRepository = browserRepository;
        this.serverRepository = serverRepository;
    }

    @GetMapping("/browsers")
    List<Browser> all(){
        return browserRepository.findAll();
    }

    @GetMapping("/browser/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        if(browserRepository.existsByID(id)) {
            Browser browser = browserRepository.findByID(id);
            return new ResponseEntity(browser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity("Browser does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/browser/{id}")
    ResponseEntity deleteBrowser(@PathVariable Long id) {
        if(browserRepository.existsByID(id)) {
            browserRepository.deleteById(id);
            return new ResponseEntity("Browser is successfully deleted!", HttpStatus.OK);
        }
        return new ResponseEntity("Browser does not exist!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/browsersServer/{id}")
    List<Browser> browsersServer(@PathVariable Long id) {
        Server server = serverRepository.findByID(id);
        List<Browser> browsers = browserRepository.findAllByserverID(server);
        return browsers;
    }

    @PostMapping("/addBrowser")
    ResponseEntity addBrowser(@RequestBody Browser browser) {
        Server server = serverRepository.findByID(Long.valueOf(browser.getIdServer()));
        if(server == null) {
            return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
        }
        else {
            Browser newBrowser = new Browser(browser.getName(), server, browser.getVersion());
            List<Browser> browsers = browserRepository.findAll();
            boolean postoji = false;
            for (Browser b: browsers) {
                if(b.getName().equals(newBrowser.getName()) && b.getVersion().equals(newBrowser.getVersion()) && b.getServerID().equals(newBrowser.getServerID()))  {
                    postoji = true;
                }
            }
            if(!postoji) browserRepository.save(newBrowser);
            else return new ResponseEntity("Browser already exists!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity("Browser is successfully added!", HttpStatus.CREATED);
    }

    @PutMapping("/updateBrowser/{id}")
    ResponseEntity updateBrowser(@RequestBody Browser browser, @PathVariable Long id) {
        Browser oldBrowser = browserRepository.findByID(id);
        if(oldBrowser == null) {
            return new ResponseEntity("Browser does not exist!", HttpStatus.NOT_FOUND);
        }
        else {
            Browser newBrowser = new Browser(oldBrowser.getName(), oldBrowser.getServerID(), oldBrowser.getVersion());
            if(!browser.getName().isEmpty()) {
                newBrowser.setName(browser.getName());
            }
            if(!browser.getVersion().isEmpty()) {
                newBrowser.setVersion(browser.getVersion());
            }
            if(!Integer.toString(browser.getIdServer()).equals(Integer.toString(0))) {
                Server server = serverRepository.findByID(Long.valueOf(browser.getIdServer()));
                if (server == null) {
                    return new ResponseEntity("Server does not exist!", HttpStatus.NOT_FOUND);
                }
                else {
                    newBrowser.setServerID(server);
                }
            }
            List<Browser> browsers = browserRepository.findAll();
            boolean postoji = false;
            for (Browser b: browsers) {
                if(b.getName().equals(newBrowser.getName()) && b.getVersion().equals(newBrowser.getVersion()) && b.getServerID().equals(newBrowser.getServerID()))  {
                    postoji = true;
                }
            }
            if(!postoji) {
                oldBrowser.setName(newBrowser.getName());
                oldBrowser.setServerID(newBrowser.getServerID());
                oldBrowser.setVersion(newBrowser.getVersion());
                browserRepository.save(oldBrowser);
            }
            else return new ResponseEntity("Browser already exists!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity(oldBrowser, HttpStatus.OK);

    }
}
