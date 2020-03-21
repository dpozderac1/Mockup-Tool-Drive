package com.example.online_testing.Controllers;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.BrowserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class BrowserController {

    private BrowserRepository browserRepository;

    @Autowired
    public BrowserController(BrowserRepository browserRepository) {
        this.browserRepository = browserRepository;
    }

    @GetMapping("/browsers")
    List<Browser> all(){
        return browserRepository.findAll();
    }

    @GetMapping("/browser/{id}")
    Browser oneId(@PathVariable Long id) {
        return browserRepository.findByID(id);
    }

    @DeleteMapping("/browser/{id}")
    String deleteBrowser(@PathVariable Long id) {
        if(browserRepository.existsByID(id)) {
            browserRepository.deleteById(id);
            return "Browser is successfully deleted!\n";
        }
        return "Browser does not exist!\n";
    }

    @GetMapping("/browsersServer/{id}")
    List<Browser> browsersServer(@PathVariable Long id) {
        List<Browser> browsers = browserRepository.findAll();
        ArrayList<Browser> returnBrowsers = new ArrayList<>();
        for (Browser b: browsers) {
            if(b.getServer_ID().getID().equals(id)) returnBrowsers.add(b);
        }
        return returnBrowsers;
    }

    @PostMapping("/addBrowser")
    String addBrowser(@RequestBody Browser browser) {
        browserRepository.save(browser);
        return "OK!";

    }

    @PutMapping("/updateBrowser/{id}")
    String updateBrowser(@RequestBody Browser browser, @PathVariable Long id) {
        Browser oldBrowser = browserRepository.findByID(id);
        if(oldBrowser != null) {
            oldBrowser.setName(browser.getName());
            oldBrowser.setVersion(browser.getVersion());
            browserRepository.save(oldBrowser);
            return "OK!";
        }
        return "Not OK!";
    }
}
