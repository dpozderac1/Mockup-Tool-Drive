package com.example.online_testing.Controllers;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.BrowserRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Services.BrowserService;
import com.fasterxml.jackson.databind.node.POJONode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import netscape.javascript.JSObject;
import org.json.JSONObject;
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

    @Autowired
    private BrowserService browserService;

    @GetMapping("/browsers")
    List<Browser> all(){
        return browserService.getAllBrowsers();
    }

    @GetMapping("/browser/{id}")
    ResponseEntity oneId(@PathVariable Long id) {
        return browserService.getBrowserByID(id);
    }

    @DeleteMapping("/browser/{id}")
    ResponseEntity deleteBrowser(@PathVariable Long id) {
        return browserService.deleteBrowserByID(id);
    }

    @GetMapping("/browsersServer/{id}")
    List<Browser> browsersServer(@PathVariable Long id) {
        return browserService.getBrowsersServer(id);
    }

    @PostMapping("/addBrowser")
    ResponseEntity addBrowser(@RequestBody Browser browser) {
        return browserService.saveBrowser(browser);
    }

    @PutMapping("/updateBrowser/{id}")
    ResponseEntity updateBrowser(@RequestBody Browser browser, @PathVariable Long id) {
        return browserService.updateBrowser(browser, id);
    }
}
