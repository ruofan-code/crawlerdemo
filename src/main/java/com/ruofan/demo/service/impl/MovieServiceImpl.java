package com.ruofan.demo.service.impl;

import com.ruofan.demo.dao.MovieDao;
import com.ruofan.demo.entity.Movie;
import com.ruofan.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.PageRequest.of;


@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Override
    public void saveMovie(Movie movie) {
        movieDao.save(movie);
    }

    @Override
    public Movie findById(Long id) {
        Optional<Movie> optional = movieDao.findById(id);
        Movie movie = optional.get();
        return movie;
    }

    @Override
    public Page<Movie> getPage(Integer page, Integer limit) {
        // 普通分页

//        Sort sort = new Sort(Sort.Direction.DESC,"id");
//        PageRequest pageRequest = (page - 1, limit,sort);
        Pageable pageable = PageRequest.of(page-1, limit);
//        Pageable pageable =PageRequest.of(page,limit,sort);
//        Class<? extends PageRequest> requestClass = pageRequest.getClass();
//        requestClass.getClasses()
        return movieDao.findAll(pageable);
    }

    @Override
    public Integer getAllcount() {
        return movieDao.findAll().size();
    }

    @Override
    public void deleteById(Long id) {
        movieDao.deleteById(id);
    }

    @Override
    public List<Movie> findByPeople(String name, Long type, Pageable pageable) {
        return movieDao.findByPeople(name,type,pageable).getContent();
    }

    @Override
    public List<Movie> findByPeopleCount(String name, Long type) {
        return movieDao.findByPeopleCount(name,type);
    }

    @Override
    public List<Movie> findByType(String type, Pageable pageable) {
//        return movieDao.findByType(type,pageable).getContent();
        return movieDao.findByType(type,pageable).getContent();
    }

    @Override
    public List<Movie> findByTypeCount(String type) {
        return movieDao.findByTypeCount(type);
    }

    @Override
    public List<Movie> findByName(String name, Pageable pageable) {
        return movieDao.findByName(name,pageable).getContent();
    }

    @Override
    public List<Movie> findByNameCount(String name) {
        return movieDao.findByNameCount(name);
    }


}
