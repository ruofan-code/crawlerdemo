package com.ruofan.demo.service.impl;

import com.ruofan.demo.dao.MovieTypeDao;
import com.ruofan.demo.entity.MovieType;
import com.ruofan.demo.service.MovieTypeService;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MovieTypeServiceImpl implements MovieTypeService {
    @Autowired
    private MovieTypeDao movieTypeDao;

    @Override
    public MovieType findByName(String name) {
        return movieTypeDao.findByName(name);
    }

    @Override
    public void save(MovieType movieType) {
        movieTypeDao.save(movieType);
    }

    @Override
    public MovieType findById(Long id) {
//        return movieTypeDao.findById(id);
        Optional<MovieType> optional = movieTypeDao.findById(id);
        MovieType movieType = optional.get();
        return movieType;
    }
}
