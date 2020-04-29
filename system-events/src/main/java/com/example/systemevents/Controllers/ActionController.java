package com.example.systemevents.Controllers;

import com.example.systemevents.Action;
import com.example.systemevents.Services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class ActionController {

    @Autowired
    private ActionService actionService;

    @GetMapping(value="/actions",produces = MediaType.APPLICATION_JSON_VALUE)
    List<Action> all(){
        return actionService.getAllActions();
    }
}
