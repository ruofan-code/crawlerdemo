package com.ruofan.demo.dao;

import com.ruofan.demo.entity.MidMovieType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidMovieTypeDao extends JpaRepository<MidMovieType,Long> {
    public List<MidMovieType> findAllByMovieId(Long id);

    public void deleteAllByMovieId(Long id);

    public MidMovieType findByMovieIdAndMovieTypeId(Long movieId,Long movieTypeId);
}
