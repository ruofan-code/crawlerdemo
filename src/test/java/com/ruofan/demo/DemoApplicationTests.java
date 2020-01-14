package com.ruofan.demo;

import com.ruofan.demo.dao.MovieDao;
import com.ruofan.demo.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {


    @Autowired
    private MovieDao movieDao;
    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
//    @Modifying
    void aksjdhfa(){
        Movie movie = new Movie();
        movie.setId((long) 16516);
        movie.setName("aksgdjka");
        movieDao.save(movie);

        List<Movie> all = movieDao.findAll();
        System.out.println(all);
    }

}
