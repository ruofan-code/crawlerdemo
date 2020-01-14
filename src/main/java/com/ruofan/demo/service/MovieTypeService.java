package com.ruofan.demo.service;

import com.ruofan.demo.entity.MovieType;

public interface MovieTypeService {
    public MovieType findByName(String name);

    public void save(MovieType movieType);

    public MovieType findById(Long id);

//    public MovieType fin
}
