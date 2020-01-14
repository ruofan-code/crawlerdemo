package com.ruofan.demo.service.impl;

import com.ruofan.demo.dao.MidMoviePerformerDao;
import com.ruofan.demo.entity.MidMoviePerformer;
import com.ruofan.demo.service.MidMoviePerfromerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MidMoviePerformerServiceImpl implements MidMoviePerfromerService {
    @Autowired
    private MidMoviePerformerDao midMoviePerformerDao;

    public void save(MidMoviePerformer midMoviePerformer){
        midMoviePerformerDao.save(midMoviePerformer);
    }

    @Override
    public List<MidMoviePerformer> findByMovieId(Long id) {
        return midMoviePerformerDao.findAllByMovieId(id);
    }

    @Override
    public void deleteAllByMovId(Long id) {
        midMoviePerformerDao.findAllByMovieId(id);
    }

    @Override
    public MidMoviePerformer findByMovieIdAndPerformIdAndType(Long movieId, Long performerId, Long type) {
        return midMoviePerformerDao.findByMovieIdAndPerformerIdAndType(movieId,performerId,type);
    }
}
