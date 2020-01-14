package com.ruofan.demo.dao;

import com.ruofan.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieDao extends JpaRepository<Movie, Long> {

    public void deleteById(Long id);

    @Query(value = "select distinct movie.* from movie " +
            "join mid_movie_performer on mid_movie_performer.movie_id=movie.id " +
            "join performer on mid_movie_performer.performer_id=performer.id " +
            "and mid_movie_performer.type=?2 " +
            "and performer.name like %?1%", nativeQuery = true)
    public Page<Movie> findByPeople(String name, Long type, Pageable pageable);

    @Query(value = "select distinct movie.* from movie " +
            "join mid_movie_performer on mid_movie_performer.movie_id=movie.id " +
            "join performer on mid_movie_performer.performer_id=performer.id " +
            "and mid_movie_performer.type=?2 " +
            "and performer.name like %?1%", nativeQuery = true)
    public List<Movie> findByPeopleCount(String name, Long type);

    @Query(value = "select distinct movie.* from movie " +
            "join mid_movie_type on mid_movie_type.movie_id=movie.id " +
            "join movie_type on movie_type.id=mid_movie_type.movie_type_id " +
            "and movie_type.name like %?1% ",nativeQuery = true)
    public Page<Movie> findByType(String type, Pageable pageable);

    @Query(value = "select distinct movie.* from movie " +
            "join mid_movie_type on mid_movie_type.movie_id=movie.id " +
            "join movie_type on movie_type.id=mid_movie_type.movie_type_id " +
            "and movie_type.name like %?1% ",nativeQuery = true)
    public List<Movie> findByTypeCount(String type);

    @Query(value = "select distinct movie.* from movie where movie.name like %?1% ",nativeQuery = true)
    public Page<Movie> findByName(String name, Pageable pageable);

    @Query(value = "select distinct movie.* from movie where movie.name like %?1% ",nativeQuery = true)
    public List<Movie> findByNameCount(String name);

}
