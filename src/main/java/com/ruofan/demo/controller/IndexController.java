package com.ruofan.demo.controller;

import com.ruofan.demo.entity.MidMoviePerformer;
import com.ruofan.demo.entity.MidMovieType;
import com.ruofan.demo.entity.Movie;
import com.ruofan.demo.entity.MovieInfo;
import com.ruofan.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MidMovieTypeService midMovieTypeService;

    @Autowired
    private MovieTypeService movieTypeService;

    @Autowired
    private MidMoviePerfromerService midMoviePerfromerService;

    @Autowired
    private PerformerService performerService;

    @RequestMapping("/movie")
    public String movie(){
        return "movie";
    }

    /**
     * 返回班级修改页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/movie_edit")
    public String class_update(String id, HttpServletRequest request){
        request.getSession().setAttribute("movieId",id);
        return "movie_edit";
    }

    /**
     * 返回电影详情页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/moviedetail")
    public String classStudent(String id, HttpServletRequest request, Model model){
        MovieInfo movieInfo = new MovieInfo();
        Movie movie = movieService.findById(Long.parseLong(id));
        movieInfo.setId(movie.getId());
        movieInfo.setName(movie.getName());
        movieInfo.setReleaseTime(movie.getReleaseTime());
        movieInfo.setPlot(movie.getPlot());
        movieInfo.setImgUrl(movie.getImgUrl());


        String type = "";
        List<MidMovieType> midMovieTypeList = midMovieTypeService.findByMovieId(movie.getId());
        for (MidMovieType midMovieType : midMovieTypeList) {
            type = type + movieTypeService.findById(midMovieType.getMovieTypeId()).getName() + " ";
        }
        type = type.substring(0, type.length() - 1);
        movieInfo.setType(type);

        List<MidMoviePerformer> midMoviePerformerList = midMoviePerfromerService.findByMovieId(movie.getId());

        String diretor = "";
        String writer = "";
        String actor = "";
        for (MidMoviePerformer midMoviePerformer : midMoviePerformerList) {
            if (midMoviePerformer.getType() == 1) {
                diretor = diretor + performerService.findById(midMoviePerformer.getPerformerId()).getName() + ",";
            } else if (midMoviePerformer.getType() == 2) {
                writer = writer + performerService.findById(midMoviePerformer.getPerformerId()).getName() + ",";
            } else {
                actor = actor + performerService.findById(midMoviePerformer.getPerformerId()).getName() + ",";
            }
        }

        if (diretor != "")
            diretor = diretor.substring(0, diretor.length() - 1);
        if (writer != "")
            writer = writer.substring(0, writer.length() - 1);
        if (actor != "")
            actor = actor.substring(0, actor.length() - 1);
        movieInfo.setDirector(diretor);
        movieInfo.setWriter(writer);
        movieInfo.setActor(actor);
        request.getSession().setAttribute("movieInfo",movieInfo);
        model.addAttribute("movieInfo",movieInfo);
        System.out.println(movieInfo);
        return "moviedetail";
    }
}
