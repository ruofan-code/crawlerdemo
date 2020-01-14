package com.ruofan.demo.service.impl;

import com.ruofan.demo.dao.MidMovieTypeDao;
import com.ruofan.demo.entity.MidMovieType;
import com.ruofan.demo.service.MidMovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MidMovieTypeServiceImpl implements MidMovieTypeService {

    @Autowired
    private MidMovieTypeDao midMovieTypeDao;
    @Override
    public void save(MidMovieType midMovieType) {
        midMovieTypeDao.save(midMovieType);
    }

    @Override
    public List<MidMovieType> findByMovieId(Long id) {
        return midMovieTypeDao.findAllByMovieId(id);
    }

    @Override
    public void deleteAllByMovieId(Long id) {
        midMovieTypeDao.deleteAllByMovieId(id);
    }

    @Override
    public MidMovieType findByMovieIdAndMovieTypeId(Long movieId, Long movieTypeId) {
        return midMovieTypeDao.findByMovieIdAndMovieTypeId(movieId,movieTypeId);
    }
}
