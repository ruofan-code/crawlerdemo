package com.ruofan.demo.service;

import com.ruofan.demo.entity.MidMoviePerformer;

import java.util.List;

public interface MidMoviePerfromerService {
    public void save(MidMoviePerformer midMoviePerformer);

    public List<MidMoviePerformer> findByMovieId(Long id);

    public void deleteAllByMovId(Long id);

    public MidMoviePerformer findByMovieIdAndPerformIdAndType(Long movieId,Long performerId,Long type);
}
