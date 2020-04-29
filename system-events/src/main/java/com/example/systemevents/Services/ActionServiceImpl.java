package com.example.systemevents.Services;

import com.example.systemevents.Action;
import com.example.systemevents.Repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionServiceImpl implements ActionService{

    @Autowired
    private ActionRepository actionRepository;


    @Override
    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }
}
