package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrowserService {
     List<Browser> getAllBrowsers();
     ResponseEntity getBrowserByID(Long id);
     ResponseEntity deleteBrowserByID(Long id);
     List<Browser> getBrowsersServer(Long id);
     ResponseEntity saveBrowser(Browser browser);
     ResponseEntity updateBrowser(Browser browser, Long id);

}
