package com.ruofan.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "mid_movie_type")
public class MidMovieType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private Long movieTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getMovieTypeId() {
        return movieTypeId;
    }

    public void setMovieTypeId(Long movieTypeId) {
        this.movieTypeId = movieTypeId;
    }

    @Override
    public String toString() {
        return "MidMovieType{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", movieTypeId=" + movieTypeId +
                '}';
    }
}
