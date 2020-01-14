package com.ruofan.demo.dao;

import com.ruofan.demo.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerDao extends JpaRepository<Performer,Long> {
    public Performer findByName(String name);
}
