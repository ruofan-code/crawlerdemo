package com.ruofan.demo.controller;

import com.ruofan.demo.entity.*;
import com.ruofan.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {
//    @RequestMapping("/")
//    public String index(){
//        return "movie";
//    }

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


    @GetMapping("/getAllMovie")
    @ResponseBody
    public WebResponse getMovieList(String page, String limit) {
        List<MovieInfo> movieInfoList = new ArrayList<>();
        Page<Movie> moviePage = movieService.getPage(Integer.parseInt(page), Integer.parseInt(limit));

        List<Movie> movieList = moviePage.getContent();

        MovieInfo movieInfo;
        for (Movie movie : movieList) {
            movieInfo = new MovieInfo();
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
            if (type != "")
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
            movieInfoList.add(movieInfo);

        }
        WebResponse webResponse = new WebResponse<>();
        webResponse.setCode(0);
        webResponse.setCount(movieService.getAllcount());
        webResponse.setData(movieInfoList);
        webResponse.setMsg("查询成功");
        return webResponse;

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteBatch")
    @ResponseBody
    @Transactional
    public String deleteBatch(@RequestParam(value = "ids[]") String[] ids) {
        for (String id : ids) {
            deleteMidMoviePerfromer(Long.parseLong(id));
            deleteMidMovieType(Long.parseLong(id));
            deleteMovie(Long.parseLong(id));
        }
        return "删除成功";
    }

    @Transactional
    void deleteMidMoviePerfromer(Long id) {
        midMoviePerfromerService.deleteAllByMovId(id);
    }

    @Transactional
    void deleteMidMovieType(Long id) {
        midMovieTypeService.deleteAllByMovieId(id);
    }

    @Transactional
    void deleteMovie(Long id) {
        movieService.deleteById(id);
    }


    /**
     * 通过id获取到某个电影的具体信息然后进行修改
     */
    @GetMapping("{id}")
    @ResponseBody
    public MovieInfo getMovieInfo(@PathVariable("id") String movieId) {
        MovieInfo movieInfo = new MovieInfo();
        Movie movie = movieService.findById(Long.parseLong(movieId));
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
        return movieInfo;
    }

    /**
     * 修改电影信息
     */
    @Transactional
    @PutMapping("/edit")
    @ResponseBody
    public WebResponse editMovie(MovieInfoTime movieInfo) throws ParseException {
        /**
         *删除之前俩张中间表的东西
         */
        deleteMidMoviePerfromer(movieInfo.getId());
        deleteMidMovieType(movieInfo.getId());
        deleteMovie(movieInfo.getId());
        /**
         * 保存电影相关信息
         */
        Movie movie = new Movie();
        movie.setId(movieInfo.getId());
        movie.setPlot(movieInfo.getPlot());
        movie.setImgUrl(movieInfo.getImgUrl());
        movie.setName(movieInfo.getName());

        String time = movieInfo.getReleaseTime();
        time = time.substring(0, 10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(time);
        movie.setReleaseTime(date);
        movieService.saveMovie(movie);


//        Long movieId = movie.getId();
//        deleteMidMoviePerfromer(movieId);
//        deleteMidMovieType(movieId);
//        deleteMovie(movieInfo.getId());

        /**
         * 重新新建中间表信息
         */
        String diretor = movieInfo.getDirector();
        String[] split = diretor.split(",");
        Performer performer;
        MidMoviePerformer midMoviePerformer;
        for (String s : split) {
            performer = new Performer();
            midMoviePerformer = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer.setName(s);
                performerService.savePerformer(performer);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer = performerService.findByName(s);
            midMoviePerformer.setMovieId(movie.getId());
            midMoviePerformer.setPerformerId(performer.getId());
            midMoviePerformer.setType((long) 1);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer.getId(), (long) 1) == null)
                midMoviePerfromerService.save(midMoviePerformer);
        }

        String writer = movieInfo.getWriter();
        String[] split1 = writer.split(",");
        Performer performer1;
        MidMoviePerformer midMoviePerformer1;
        for (String s : split1) {
            performer1 = new Performer();
            midMoviePerformer1 = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer1.setName(s);
                performerService.savePerformer(performer1);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer1 = performerService.findByName(s);
            midMoviePerformer1.setMovieId(movie.getId());
            midMoviePerformer1.setPerformerId(performer1.getId());
            midMoviePerformer1.setType((long) 2);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer1.getId(), (long) 2) == null)
                midMoviePerfromerService.save(midMoviePerformer1);
        }

        String actor = movieInfo.getActor();
        String[] split2 = actor.split(",");
        Performer performer2;
        MidMoviePerformer midMoviePerformer2;
        for (String s : split2) {
            performer2 = new Performer();
            midMoviePerformer2 = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer2.setName(s);
                performerService.savePerformer(performer2);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer2 = performerService.findByName(s);
            midMoviePerformer2.setMovieId(movie.getId());
            midMoviePerformer2.setPerformerId(performer2.getId());
            midMoviePerformer2.setType((long) 3);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer2.getId(), (long) 3) == null)
                midMoviePerfromerService.save(midMoviePerformer2);
        }


        String type = movieInfo.getType();
        String[] split3 = type.split(" ");
        MovieType movieType;
        MidMovieType midMovieType;
        for (String s : split3) {
            movieType = new MovieType();
            midMovieType = new MidMovieType();
            if (movieTypeService.findByName(type) == null) {
                movieType.setName(type);
                movieTypeService.save(movieType);
            }
            movieType = movieTypeService.findByName(type);
            midMovieType = new MidMovieType();
            midMovieType.setMovieId(movie.getId());
            midMovieType.setMovieTypeId(movieType.getId());
            if (midMovieTypeService.findByMovieIdAndMovieTypeId(movie.getId(), movieType.getId()) == null)
                midMovieTypeService.save(midMovieType);
//            System.out.println(type);
        }
        WebResponse webResponse = new WebResponse();
        webResponse.setCode(0);
        webResponse.setMsg("修改成功");

        return webResponse;
    }

    /**
     * 新建电影
     */
    @PostMapping("/add")
    @ResponseBody
    public WebResponse addMovie(MovieInfoTime movieInfo) throws ParseException {
        System.out.println(movieInfo);
        Movie movie = new Movie();
        movie.setId(movieInfo.getId());
        movie.setName(movieInfo.getName());
        movie.setPlot(movieInfo.getPlot());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(movieInfo.getReleaseTime());
        movie.setReleaseTime(date);
        System.out.println("*****************");
        System.out.println(movie.getReleaseTime().toString());
        movie.setImgUrl(movieInfo.getImgUrl());
        movieService.saveMovie(movie);

        String diretor = movieInfo.getDirector();
        String[] split = diretor.split(",");
        Performer performer;
        MidMoviePerformer midMoviePerformer;
        for (String s : split) {
            performer = new Performer();
            midMoviePerformer = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer.setName(s);
                performerService.savePerformer(performer);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer = performerService.findByName(s);
            midMoviePerformer.setMovieId(movie.getId());
            midMoviePerformer.setPerformerId(performer.getId());
            midMoviePerformer.setType((long) 1);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer.getId(), (long) 1) == null)
                midMoviePerfromerService.save(midMoviePerformer);
        }

        String writer = movieInfo.getWriter();
        String[] split1 = writer.split(",");
        Performer performer1;
        MidMoviePerformer midMoviePerformer1;
        for (String s : split1) {
            performer1 = new Performer();
            midMoviePerformer1 = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer1.setName(s);
                performerService.savePerformer(performer1);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer1 = performerService.findByName(s);
            midMoviePerformer1.setMovieId(movie.getId());
            midMoviePerformer1.setPerformerId(performer1.getId());
            midMoviePerformer1.setType((long) 2);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer1.getId(), (long) 2) == null)
                midMoviePerfromerService.save(midMoviePerformer1);
        }

        String actor = movieInfo.getActor();
        String[] split2 = actor.split(",");
        Performer performer2;
        MidMoviePerformer midMoviePerformer2;
        for (String s : split2) {
            performer2 = new Performer();
            midMoviePerformer2 = new MidMoviePerformer();
            if (performerService.findByName(s) == null) {
                performer2.setName(s);
                performerService.savePerformer(performer2);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
            }
            performer2 = performerService.findByName(s);
            midMoviePerformer2.setMovieId(movie.getId());
            midMoviePerformer2.setPerformerId(performer2.getId());
            midMoviePerformer2.setType((long) 3);
            if (midMoviePerfromerService.findByMovieIdAndPerformIdAndType(movie.getId(), performer2.getId(), (long) 3) == null)
                midMoviePerfromerService.save(midMoviePerformer2);
        }


        String type = movieInfo.getType();
        String[] split3 = type.split(" ");
        MovieType movieType;
        MidMovieType midMovieType;
        for (String s : split3) {
            movieType = new MovieType();
            midMovieType = new MidMovieType();
            if (movieTypeService.findByName(type) == null) {
                movieType.setName(type);
                movieTypeService.save(movieType);
            }
            movieType = movieTypeService.findByName(type);
            midMovieType = new MidMovieType();
            midMovieType.setMovieId(movie.getId());
            midMovieType.setMovieTypeId(movieType.getId());
            if (midMovieTypeService.findByMovieIdAndMovieTypeId(movie.getId(), movieType.getId()) == null)
                midMovieTypeService.save(midMovieType);
//            System.out.println(type);
        }
        WebResponse webResponse = new WebResponse();
        webResponse.setCode(0);
        webResponse.setMsg("新建成功");
        return webResponse;
    }

    /**
     * 条件搜索
     */
    @ResponseBody
    @GetMapping("/search")
    public WebResponse searchByOption(String page, String limit, String fieldType, String fieldValue) {
        System.out.println("asdf");
        List<MovieInfo> movieInfoList = new ArrayList<>();
        WebResponse webResponse = new WebResponse();
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit));
        if (fieldType.equals("director")) {
            List<Movie> directorMovieList = movieService.findByPeople(fieldValue, (long) 1, pageable);

            MovieInfo movieInfo;
            for (Movie movie : directorMovieList) {
                movieInfo = new MovieInfo();
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
                if (type != "")
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
                movieInfoList.add(movieInfo);

            }

            webResponse.setCode(0);
            webResponse.setMsg("查询成功");
            webResponse.setData(movieInfoList);
            webResponse.setCount(movieService.findByPeopleCount(fieldValue, (long) 1).size());
            System.out.println(webResponse);
            return webResponse;
        } else if (fieldType.equals("writer")) {
            List<Movie> writerMovieList = movieService.findByPeople(fieldValue, (long) 2, pageable);
            MovieInfo movieInfo;
            for (Movie movie : writerMovieList) {
                movieInfo = new MovieInfo();
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
                if (type != "")
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
                movieInfoList.add(movieInfo);

            }

            webResponse.setCode(0);
            webResponse.setMsg("查询成功");
            webResponse.setData(movieInfoList);
            webResponse.setCount(movieService.findByPeopleCount(fieldValue, (long) 2).size());
            System.out.println(webResponse);
            return webResponse;
        } else if (fieldType.equals("actor")) {
            List<Movie> actorMovieList = movieService.findByPeople(fieldValue, (long) 3, pageable);
            MovieInfo movieInfo;
            for (Movie movie : actorMovieList) {
                movieInfo = new MovieInfo();
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
                if (type != "")
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
                movieInfoList.add(movieInfo);

            }

            webResponse.setCode(0);
            webResponse.setMsg("查询成功");
            webResponse.setData(movieInfoList);
            webResponse.setCount(movieService.findByPeopleCount(fieldValue, (long) 3).size());
            System.out.println(webResponse);
            return webResponse;
        }
        else if (fieldType.equals("type")){
            List<Movie> typeMovieList = movieService.findByType(fieldValue, pageable);
            MovieInfo movieInfo;
            for (Movie movie : typeMovieList) {
                movieInfo = new MovieInfo();
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
                if (type != "")
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
                movieInfoList.add(movieInfo);

            }

            webResponse.setCode(0);
            webResponse.setMsg("查询成功");
            webResponse.setData(movieInfoList);
            webResponse.setCount(movieService.findByTypeCount(fieldValue).size());
            System.out.println(webResponse);
            return webResponse;
        }else{
            List<Movie> nameList = movieService.findByName(fieldValue, pageable);
            MovieInfo movieInfo;
            for (Movie movie : nameList) {
                movieInfo = new MovieInfo();
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
                if (type != "")
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
                movieInfoList.add(movieInfo);

            }

            webResponse.setCode(0);
            webResponse.setMsg("查询成功");
            webResponse.setData(movieInfoList);
            webResponse.setCount(movieService.findByNameCount(fieldValue).size());
            System.out.println(webResponse);
            return webResponse;
        }
//            return webResponse;
//        else if (fieldType=="type"){
//
//        }
    }
}


