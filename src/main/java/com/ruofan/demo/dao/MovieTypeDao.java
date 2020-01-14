package com.ruofan.demo.dao;

import com.ruofan.demo.entity.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTypeDao extends JpaRepository<MovieType,Long> {
    public MovieType findByName(String name);

//    public MovieType findById(Long Id);
}
