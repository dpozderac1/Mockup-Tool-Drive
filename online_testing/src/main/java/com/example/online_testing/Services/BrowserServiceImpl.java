package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.BrowserRepository;
import com.example.online_testing.Repositories.ServerRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        if(browserRepository.existsByID(id)) {
            Browser browser = browserRepository.findByID(id);
            return new ResponseEntity(browser, HttpStatus.OK);
        }
        else {
            throw new RecordNotFoundException("Browser does not exist!");
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
        throw new RecordNotFoundException("Browser does not exist!");
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
        Browser newBrowser = new Browser();
        if(server == null) {
            throw new RecordNotFoundException("Server does not exist!");
        }
        else {
            newBrowser.setName(browser.getName());
            newBrowser.setServerID(server);
            newBrowser.setVersion(browser.getVersion());
            List<Browser> browsers = browserRepository.findAll();
            boolean postoji = false;
            for (Browser b: browsers) {
                if(b.getName().equals(newBrowser.getName()) && b.getVersion().equals(newBrowser.getVersion()) && b.getServerID().equals(newBrowser.getServerID()))  {
                    postoji = true;
                }
            }
            if(!postoji) browserRepository.save(newBrowser);
            else {
                throw new AlreadyExistsException("Browser already exists!");
            }
        }
        return new ResponseEntity(newBrowser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateBrowser(Browser browser, Long id) {
        Browser oldBrowser = browserRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(oldBrowser == null) {
            throw new RecordNotFoundException("The browser you want to update does not exist!");
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
                    throw new RecordNotFoundException("Server does not exist!");
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
                throw new AlreadyExistsException("Browser already exists!");
            }
        }
        return new ResponseEntity(oldBrowser, HttpStatus.OK);
    }
}
