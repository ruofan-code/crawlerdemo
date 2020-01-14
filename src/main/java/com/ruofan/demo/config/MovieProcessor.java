package com.ruofan.demo.config;

import com.ruofan.demo.dao.MidMovieTypeDao;
import com.ruofan.demo.entity.*;
import com.ruofan.demo.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MovieProcessor implements PageProcessor {

    public MovieProcessor() {
//        this.site = site;
        System.out.println("构造器");
    }

    @Autowired
    private MovieService movieService;

    @Autowired
    private PerformerService performerService;

    @Autowired
    private MidMoviePerfromerService midMoviePerfromerService;

    @Autowired
    private MovieTypeService movieTypeService;

    @Autowired
    private MidMovieTypeService midMovieTypeService;


    @Override
    public void process(Page page) {
        List<Selectable> selectableList = page.getHtml().css("li.fl").nodes();

        //电影详情页，说明不在列表
        if (selectableList.size() == 0) {
            if (page.getUrl().toString().contains("performer"))
                saveMoviePeople(page);
            else {
                try {
                    saveMovieTime(page);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Movie movie;
            for (Selectable selectable : selectableList) {
                // 从页面上【摘取】出电影的名字
                String name = selectable.xpath("/li/div/p[1]/a/text()").get();
                if (StringUtils.isBlank(name))  // name != null && name != "" && name != "      "
                    continue;

                movie = new Movie();
                // 从页面上【摘取】出电影的名字
//                String name = selectable.xpath("/li/div/p[1]/a/text()").get();
                movie.setName(name);
                // 从页面上【摘取】电影的id
                String idContent = selectable.xpath("/li/div/p[1]/a/@href").get();
                String id = new String();
                if (StringUtils.isNotBlank(idContent)) {
                    String[] tokens = StringUtils.split(idContent, "/");
                    id = tokens[tokens.length - 1];

                    movie.setId(Long.parseLong(id));
                }

                //保存当前电影的部分信息（name 和id）
                System.out.println(movieService);
                movieService.saveMovie(movie);

                String peopleUrl = "https://www.1905.com/mdb/film/" + id + "/performer/?fr=mdbypsy_dh_yzry";
                String timeUrl = "https://www.1905.com/mdb/film/" + id + "/";

                page.addTargetRequest(peopleUrl);
                page.addTargetRequest(timeUrl);

            }
        }


    }


    /**
     * 保存电影的人员信息，图片信息
     *
     * @param page
     */
    private void saveMoviePeople(Page page) {

        Performer performer;
        MidMoviePerformer midMoviePerformer;


        List<Selectable> selectableList = page.getHtml().css("div.secPage-actors").nodes();
        String moviePictureLink = page.getHtml().css("div.header-wrapper-poster>a").xpath("/a/img/@src").get();

        String id = page.getUrl().toString().substring(30, page.getUrl().toString().length() - 30);
        System.out.println("*********");
        System.out.println(id);
        Movie movie = movieService.findById(Long.parseLong(id));

        movie.setImgUrl(moviePictureLink.substring(0, moviePictureLink.length()));
        movieService.saveMovie(movie);

        // 去取页面上的 导演、编剧、演员
        for (Selectable selectable : selectableList) {

//            midMoviePerformer = new MidMoviePerformer();

//            StringBuffer allDirector = new StringBuffer();
            // 当前的 <div class="secPage-actors"> ... </div> 中包含了导演的信息
            /**
             * 获取导演信息
             */
            if (containsDirector(selectable)) {
                // 找到包含导演（一个或多个）的那些 <a> ... </a>
                List<Selectable> directerNodeList = selectable.css("a[class='left-actor']").nodes();
                for (Selectable directorNode : directerNodeList) {
                    performer = new Performer();
                    midMoviePerformer = new MidMoviePerformer();
                    // 取 <a>...</a> 【夹着的】文本内容，即，导演的名字。
                    String director = directorNode.xpath("/a/text()").get();
                    if (performerService.findByName(director)==null) {
                        performer.setName(director);
                        performerService.savePerformer(performer);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
                    }
                    performer=performerService.findByName(director);
                    midMoviePerformer.setMovieId(Long.parseLong(id));
                    midMoviePerformer.setPerformerId(performer.getId());
                    midMoviePerformer.setType((long) 1);
                    midMoviePerfromerService.save(midMoviePerformer);
//                    allDirector.append(director).append(",");
                }

                // 去掉最后的一个多余的 “,”


//                movie.setDirectors(allDirector.substring(0, allDirector.length()-1));

            }

            /**
             * 获取编剧信息
             */
//            StringBuffer allWriter = new StringBuffer();
            // 当前的 <div class="secPage-actors"> ... </div> 中包含了编剧的信息
            if (containsWriter(selectable)) {
                List<Selectable> writerNodeList = selectable.css("a[class='left-actor']").nodes();
                for (Selectable writerNode : writerNodeList) {
                    String wirter = writerNode.xpath("a/text()").get();
                    performer = new Performer();
                    midMoviePerformer = new MidMoviePerformer();
                    if (performerService.findByName(wirter)==null) {
                        performer.setName(wirter);
                        performerService.savePerformer(performer);

//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
                    }
                    performer=performerService.findByName(wirter);
                    midMoviePerformer.setMovieId(Long.parseLong(id));
                    midMoviePerformer.setPerformerId(performer.getId());
                    midMoviePerformer.setType((long) 2);
                    midMoviePerfromerService.save(midMoviePerformer);
//                    allWriter.append(wirter).append(",");
                }
//                movie.setWriters(allWriter.substring(0,allWriter.length()-1));
            }

            /**
             * 获取演员信息
             */
            // 当前的 <div class="secPage-actors"> ... </div> 中包含了演员的信息
//            StringBuffer allActor = new StringBuffer();
            if (containsActor(selectable)) {
                List<Selectable> actorNodeList = selectable.css("a[class='left-actor']").nodes();
                for (Selectable actorNode : actorNodeList) {
                    String actor = actorNode.xpath("a/text()").get();
                    performer = new Performer();
                    midMoviePerformer = new MidMoviePerformer();
                    if (performerService.findByName(actor)==null) {
                        performer.setName(actor);
                        performerService.savePerformer(performer);
                        System.out.println("**************************");
                        System.out.println(performer.getId());


//                        midMoviePerformer.setMovieId(Long.parseLong(id));
//                        midMoviePerformer.setPerformerId(performer.getId());
//                        midMoviePerformer.setType((long) 1);
//                        midMoviePerfromerService.save(midMoviePerformer);
                    }
                    performer=performerService.findByName(actor);
                    midMoviePerformer.setMovieId(Long.parseLong(id));
                    midMoviePerformer.setPerformerId(performer.getId());
                    midMoviePerformer.setType((long) 3);
                    midMoviePerfromerService.save(midMoviePerformer);
//                    allActor.append(actor).append(",");
                }
//                movie.setActors(allActor.substring(0,allActor.length()-1));
            }

        }
    }

    /**
     * 保存时间
     * @param page
     * @throws ParseException
     */
    private void saveMovieTime(Page page) throws ParseException {
        MovieType movieType;
        MidMovieType midMovieType;
        String id = page.getUrl().toString().substring(30, page.getUrl().toString().length() - 1);
        String time = page.getHtml().css("div.information-list").xpath("/div/span/text()").get().substring(0, 10);
//        String type = page.getHtml().css("div.information-list").xpath("/div/span[2]/a[2]/text()").get();

        System.out.println(time);

        //查询电影详情
        List<Selectable> selectableList = page.getHtml().css("div.plot").nodes();
        Movie movie = movieService.findById(Long.parseLong(id));
//        Movie movie = new Movie();
//        movie.setId(movieId);
//        List<Movie> movies = new ArrayList<>(20);
//        for (Selectable selectable : selectableList) {


        String plot = selectableList.get(0).xpath("/div/p/text()").get();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date =simpleDateFormat.parse(time);
        movie.setReleaseTime(date);
        movie.setPlot(plot);
        movieService.saveMovie(movie);

//        if (movieTypeService.findByName(type)){
//            MovieType movieType = new MovieType();
//            movieType.setName(type);
//            movieTypeService.save(movieType);
//        }

        /**
         * 获取类型
         */
//        List<Selectable> selectableList1 = page.getHtml().css("div.information-list>span a").nodes();
//        Movie movie = new Movie();
//        movie.setId(movieId);


//        for (Selectable selectable : selectableList1) {
//            movieType = new MovieType();
//            String type = selectable.xpath("/a/text()").get();
//            if (movieTypeService.findByName(type)==null){
//                movieType.setName(type);
//                movieTypeService.save(movieType);
//            }
//            movieType = movieTypeService.findByName(type);
//            midMovieType = new MidMovieType();
//            midMovieType.setMovieId(Long.parseLong(id));
//            midMovieType.setMovieTypeId(movieType.getId());
//            midMovieTypeService.save(midMovieType);
//            System.out.println(type);
//        }

        /**
         * 获取类型新
         */
        List<Selectable> selectableList1 = page.getHtml().css("div.information-list").nodes();

//        Movie movie = new Movie();
//        movie.setId(movieId);
        movieType = new MovieType();
        for (Selectable selectable : selectableList1) {
            List<Selectable> aList = selectable.xpath("/div/span[2]/a").nodes();
            for (Selectable a :aList) {
                String type = a.xpath("/a/text()").get();
                if (movieTypeService.findByName(type)==null){
                    movieType.setName(type);
                    movieTypeService.save(movieType);
                }
                movieType = movieTypeService.findByName(type);
                midMovieType = new MidMovieType();
                midMovieType.setMovieId(Long.parseLong(id));
                midMovieType.setMovieTypeId(movieType.getId());
                midMovieTypeService.save(midMovieType);
            }
        }



//        }
    }

    private Site site = Site.me()
            .setCharset("utf8")
            .setTimeOut(10 * 1000)
            .setRetrySleepTime(3);

    @Override
    public Site getSite() {
        return site;
    }

//    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
//    public void process() {
//        Spider.create(movieProcessorFactory.getMovieProcessor())
//                .addUrl(URL)
//                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
//                .thread(10)
//                .run();
//    }

    private boolean containsDirector(Selectable selectable) {
        String content = selectable.xpath("/div/h3/text()").get();
        return StringUtils.contains(content, "导演");
    }

    private boolean containsWriter(Selectable selectable) {
        String content = selectable.xpath("div/h3/text()").get();
        return StringUtils.contains(content, "编剧");

    }

    private boolean containsActor(Selectable selectable) {
        String content = selectable.xpath("div/h3/text()").get();
        return StringUtils.contains(content, "演员");
    }
}
