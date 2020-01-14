package com.ruofan.demo;

import com.ruofan.demo.config.MovieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

    private static final String URL = "https://www.1905.com/mdb/film/list/year-2019";

    @Autowired
    private MovieProcessor movieProcessor;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        Spider.create(movieProcessor)
                .addUrl(URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .run();
    }
}
