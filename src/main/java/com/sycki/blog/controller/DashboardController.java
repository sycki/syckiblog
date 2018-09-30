package com.sycki.blog.controller;

import com.sycki.blog.config.BlogUtils;
import com.sycki.blog.config.RandomValidateCode;
import com.sycki.blog.dao.ArticleDao;
import com.sycki.blog.dao.DashboardDao;
import com.sycki.blog.pojo.Article;
import com.sycki.blog.pojo.ArticleIndex;
import com.sycki.blog.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 后台管理页面，调用本类中所有/dashboard开头的请求均需通过Dashboardinterceptor中的登录认证
 * Created by kxdmmr on 2017/8/19.
 */
@Controller
public class DashboardController implements BaseController {
    static RandomValidateCode randomValidateCode = new RandomValidateCode();

    @Resource
    ArticleDao articleDao;

    @Resource
    DashboardDao dashboardDao;

    @GetMapping(path = "/login")
    String login() {
        return "login";
    }

    @PostMapping(path = "/login/{clientId}")
    String loginPost(HttpServletRequest request, Model model, @PathVariable String clientId) {
        HttpSession session = request.getSession(true);
        String inputCode = request.getParameter("validatecode");
        if (!randomValidateCode.getNowCode().equalsIgnoreCase(inputCode)) {
            LOG.warn(String.format("Login failed of validatecode [%s:%s]", clientId, BlogUtils.getClientIP(request)));
            model.addAttribute("message", "验证码错误！");
            return "login";
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User dbUser = this.dashboardDao.login(username, password);
        if (dbUser != null && dbUser.getUserName().equals(username) && dbUser.getPasswd().equals(password)){
            this.articleDao.insertUserAction(clientId,"login",username+"_"+BlogUtils.getClientIP(request));
            session.setAttribute("user", dbUser);
            model.addAttribute("message", "欢迎回来！");
            return "redirect:/dashboard/notepad";
        }

        LOG.warn(String.format("Login failed of userpasswd [%s:%s:%s]", username, clientId, BlogUtils.getClientIP(request)));
        model.addAttribute("message", "用户名或密码不正确！");
        return "login";
    }

    @GetMapping(path = "/validatecode*")
    void validateCode(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        randomValidateCode.getRandcode(request, response);
    }

    @GetMapping(path = "/dashboard/notepad")
    String notepad(Model model) {
        int id = this.articleDao.getMaxIdByTable("blog_articles") + 1;
        ArrayList<ArticleIndex> index = this.articleDao.selectIndex();
        model.addAttribute("index", index);
        model.addAttribute("newArticleId", id);
        return "dashboard/notepad";
    }

//    @GetMapping(path = "/dashboard/notepad/{articleName}")
//    String articleEdit(Model model, @PathVariable("articleName") String articleName) {
//        System.out.println("articleEdit:"+articleName);
//        Article article = this.articleDao.selectArticleByName(articleName);
//        System.out.println("articleEdit:"+article.getEn_name());
//        model.addAttribute("article", article);
//        return "dashboard/notepad";
//    }

    // @ModelAttribute
    @PostMapping(path = "/dashboard/writer")
    @ResponseBody
    String writer(HttpServletRequest request) {
        try {
            int id = Integer.parseInt(request.getParameter("id").toString());
            int parentId = id;
            String title = (String) request.getParameter("title");
            String en_name = (String) request.getParameter("en_name");
            if (en_name != null)
                en_name = en_name.toLowerCase().replaceAll("[\\.\\s]", "-");
            String content = (String) request.getParameter("content");
            String tags = (String) request.getParameter("tags");

            String createDate = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
            String change_date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
            int like_count = 0;
            int unlike_count = 0;
            int viewer_count = 0;

            Article a = this.articleDao.selectArticleById(id);
            if (a != null && a.getEn_name().equals(en_name)) {
                this.articleDao.updateArticleStatusByName("old", en_name);

                id = this.articleDao.getMaxIdByTable("blog_articles") + 1;
                createDate = a.getCreate_date();
                like_count = a.getLike_count();
                unlike_count = a.getUnlike_count();
                viewer_count = a.getViewer_count();
            }

            this.articleDao.insertArticle(id, parentId, title, en_name, content, 1, createDate, change_date, "publish", tags, like_count, unlike_count, viewer_count);
        }catch(Exception e){
            return e.getMessage();
        }
        return "文章已提交！";
    }

    @GetMapping(path = "/dashboard/preview")
    String preview() {
        return "dashboard/preview";
    }


}
