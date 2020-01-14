package com.ruofan.demo.dao;

import com.ruofan.demo.entity.MidMoviePerformer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidMoviePerformerDao extends JpaRepository<MidMoviePerformer,Long> {

    public List<MidMoviePerformer> findAllByMovieId(Long id);

    public void deleteAllByMovieId(Long id);

    public MidMoviePerformer findByMovieIdAndPerformerIdAndType(Long movieId, Long performerId, Long type);

}
