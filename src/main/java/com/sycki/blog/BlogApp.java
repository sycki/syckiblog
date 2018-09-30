package com.sycki.blog;

import com.sycki.blog.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by kxdmmr on 2017/7/30.
 */
@SpringBootApplication
public class BlogApp {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

                container.addErrorPages(error401Page, error404Page, error500Page);
            }
        };
    }

    public static void main(String[] args) {
        URL url = BlogApp.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
            filePath = filePath.split("!")[0];
            filePath = filePath.split(":")[1];
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Load config file:"+filePath+"/syckiblog.conf");

        Config config = Config.getInstance()
            .addOption("blacklist.entry.max.millisecond")
            .addOption("blacklist.entry.max.num")
            .addOption("blacklist.max.access.num")
            .addOption("blacklist.valid.millisecond")
            .addOption("blacklist.nginx.config.dir")
            .addOption("ui.desc.page.limit")
            .addOption("ui.desc.max.long")
            .load(filePath+"/syckiblog.conf");
        if(config == null) {
            System.exit(11);
        }
        SpringApplication.run(BlogApp.class, args);
    }
}
