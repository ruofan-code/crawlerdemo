package com.ruofan.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "mid_movie_performer")
public class MidMoviePerformer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long movieId;
    private Long performerId;
    private Long type;

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

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MidMoviePerformer{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", performerId=" + performerId +
                ", type=" + type +
                '}';
    }
}
