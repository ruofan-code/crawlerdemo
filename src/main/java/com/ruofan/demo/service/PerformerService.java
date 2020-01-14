package com.ruofan.demo.service;

import com.ruofan.demo.entity.Performer;

public interface PerformerService {
    public Performer findByName(String name);
    public void savePerformer(Performer performer);
    public Performer findById(Long id);
}
