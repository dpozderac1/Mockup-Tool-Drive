package com.example.systemevents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ActionRepository  extends JpaRepository<Action,Long> {

}
