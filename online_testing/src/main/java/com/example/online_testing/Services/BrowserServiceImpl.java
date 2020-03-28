package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.BrowserRepository;
import com.example.online_testing.Repositories.ServerRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequestExtensionsKt;

import java.util.List;

@Service
public class BrowserServiceImpl implements BrowserService {

    @Autowired
    private BrowserRepository browserRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Override
    public List<Browser> getAllBrowsers() {
        return browserRepository.findAll();
    }

    @Override
    public ResponseEntity getBrowserByID(Long id) {
        JSONObject jo = new JSONObject();
        if(browserRepository.existsByID(id)) {
            Browser browser = browserRepository.findByID(id);
            return new ResponseEntity(browser, HttpStatus.OK);
        }
        else {
            jo.put("message", "Browser does not exist!");
            return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity deleteBrowserByID(Long id) {
        JSONObject jo = new JSONObject();
        if(browserRepository.existsByID(id)) {
            browserRepository.deleteById(id);
            jo.put("message", "Browser is successfully deleted!");
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        jo.put("message", "Browser does not exist!");
        return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Browser> getBrowsersServer(Long id) {
        Server server = serverRepository.findByID(id);
        List<Browser> browsers = browserRepository.findAllByserverID(server);
        return browsers;
    }

    @Override
    public ResponseEntity saveBrowser(Browser browser) {
        Server server = serverRepository.findByID(Long.valueOf(browser.getIdServer()));
        JSONObject jo = new JSONObject();
        if(server == null) {
            jo.put("message", "Server does not exist!");
            return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
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
            else {
                jo.put("message", "Browser already exists!");
                return new ResponseEntity(jo.toString(), HttpStatus.CONFLICT);
            }
        }
        jo.put("message", "Browser is successfully added!");
        return new ResponseEntity(jo.toString(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateBrowser(Browser browser, Long id) {
        Browser oldBrowser = browserRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(oldBrowser == null) {
            jo.put("message", "Browser does not exist!");
            return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
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
                    jo.put("message", "Server does not exist!");
                    return new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND);
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
            else {
                jo.put("message", "Browser already exists!");
                return new ResponseEntity(jo.toString(), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity(oldBrowser, HttpStatus.OK);
    }
}
