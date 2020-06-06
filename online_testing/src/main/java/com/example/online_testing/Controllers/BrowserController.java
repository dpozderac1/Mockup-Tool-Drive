package com.example.online_testing.Controllers;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Services.BrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@CrossOrigin
@RestController
public class BrowserController {

    @Autowired
    private BrowserService browserService;

    @GetMapping("/browsers")
    public List<Browser> all(){
        return browserService.getAllBrowsers();
    }

    @GetMapping("/browser/{id}")
    public ResponseEntity oneId(@PathVariable Long id) {
        return browserService.getBrowserByID(id);
    }

    @DeleteMapping(value = "/browser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteBrowser(@PathVariable Long id) {
        return browserService.deleteBrowserByID(id);
    }

    @GetMapping("/browsersServer/{id}")
    public List<Browser> browsersServer(@PathVariable Long id) {
        return browserService.getBrowsersServer(id);
    }

    @PostMapping("/addBrowser")
    public ResponseEntity addBrowser(@Valid @RequestBody Browser browser) {
        return browserService.saveBrowser(browser);
    }

    @PutMapping("/updateBrowser/{id}")
    public ResponseEntity updateBrowser(@Valid @RequestBody Browser browser, @PathVariable Long id) {
        return browserService.updateBrowser(browser, id);
    }
}
