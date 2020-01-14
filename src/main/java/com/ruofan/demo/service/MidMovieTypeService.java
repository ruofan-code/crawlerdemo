package com.ruofan.demo.service;

import com.ruofan.demo.entity.MidMovieType;

import java.util.List;

public interface MidMovieTypeService {
    public void save(MidMovieType midMovieType);

    public List<MidMovieType> findByMovieId(Long id);

    public void deleteAllByMovieId(Long id);

    public MidMovieType findByMovieIdAndMovieTypeId(Long  movieId,Long movieTypeId);
}
