package com.ruofan.demo.service;

import com.ruofan.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    public void saveMovie(Movie movie);

    public Movie findById(Long id);

    // 普通分页
    Page<Movie> getPage(Integer page, Integer limit);

    public Integer getAllcount();

    public void deleteById(Long id);

    public List<Movie> findByPeople(String name, Long type, Pageable pageable);
    public List<Movie> findByPeopleCount(String name, Long type);

    public List<Movie> findByType(String type, Pageable pageable);
    public List<Movie> findByTypeCount(String type);

    public List<Movie> findByName(String name, Pageable pageable);
    public List<Movie> findByNameCount(String name);

}
