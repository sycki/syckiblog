package com.sycki.blog.restful;

import com.sycki.blog.dao.ArticleDao;
import com.sycki.blog.pojo.Article;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 负责GET类型的API请求，需验证请求头：x-requested-by:sycki
 * Created by kxdmmr on 2017/9/9.
 */
@RestController
public class GreetingController {
    private static final String template = "{\"status\": %s, \"message\": \"%s\"}";

    @Resource
    ArticleDao articleDao;

    /**
     * 根据文章名返回文章json串
     * @param articleName
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/api/articles/{articleName}")
    public Article greeting(@PathVariable("articleName") String articleName, HttpServletRequest request, HttpServletResponse response) {
        if(! "sycki".equals(request.getHeader("x-requested-by"))){
            response.setStatus(403);
            return null;
        }
        Article article = this.articleDao.selectArticleByName(articleName);
        return article;
    }

}
