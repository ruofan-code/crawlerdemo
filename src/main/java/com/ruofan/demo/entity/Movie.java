package com.ruofan.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    private Long id;
    private String name;
    private String plot;
    private String imgUrl;
    private Date releaseTime;

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

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", plot='" + plot + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", releaseTime=" + releaseTime +
                '}';
    }
}
