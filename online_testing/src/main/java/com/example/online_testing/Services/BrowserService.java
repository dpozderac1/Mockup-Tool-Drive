package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrowserService {
    public List<Browser> getAllBrowsers();
    public ResponseEntity getBrowserByID(Long id);
    public ResponseEntity deleteBrowserByID(Long id);
    public List<Browser> getBrowsersServer(Long id);
    public ResponseEntity saveBrowser(Browser browser);
    public ResponseEntity updateBrowser(Browser browser, Long id);

}
