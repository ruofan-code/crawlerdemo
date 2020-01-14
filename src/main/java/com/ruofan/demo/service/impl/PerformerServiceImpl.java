package com.ruofan.demo.service.impl;

import com.ruofan.demo.dao.PerformerDao;
import com.ruofan.demo.entity.Performer;
import com.ruofan.demo.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PerformerServiceImpl implements PerformerService {
    @Autowired
    private PerformerDao performerDao;

    public Performer findByName(String name){
        return performerDao.findByName(name);
    }

    @Override
    public void savePerformer(Performer performer) {
        performerDao.save(performer);
    }

    @Override
    public Performer findById(Long id) {
        Optional<Performer> byId = performerDao.findById(id);
        return byId.get();
    }
}
