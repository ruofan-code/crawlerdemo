package com.ruofan.demo.entity;

import java.util.Date;

public class MovieInfo {
    private Long id;
    private String name;
    private String type;
    private String director;
    private String writer;
    private String actor;
    private Date releaseTime;
    private String imgUrl;
    private String plot;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", director='" + director + '\'' +
                ", writer='" + writer + '\'' +
                ", actor='" + actor + '\'' +
                ", releaseTime=" + releaseTime +
                ", imgUrl='" + imgUrl + '\'' +
                ", plot='" + plot + '\'' +
                '}';
    }
}
