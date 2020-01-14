package com.ruofan.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "performer")
public class Performer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

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

    @Override
    public String toString() {
        return "Performer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
