package com.sycki.blog.controller;

import com.sycki.blog.config.Config;
import com.sycki.blog.pojo.Article;
import com.sycki.blog.pojo.ArticleIndex;
import com.sycki.blog.pojo.UserAction;
import com.sycki.blog.security.BlackList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sycki.blog.dao.ArticleDao;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class ArticleController implements BaseController {
	Config config = Config.getInstance();
	BlackList blackList = BlackList.getInstance();

	@Resource
	ArticleDao articleDao;

	@GetMapping("/")
	String index(Model model){
		ArrayList<Article> articles = this.articleDao.selectArticles(config.getInt("ui.desc.page.limit"));
		ArrayList<ArticleIndex> index = this.articleDao.selectIndex();
		model.addAttribute("articles", articles);
		model.addAttribute("index", index);
		return "index";
	}

	@GetMapping("/copyright")
	String copyright(Model model){
		return "copyright";
	}

	@GetMapping(path = "/article/{articleName}")
	String article(Model model, @PathVariable("articleName") String articleName){
		Article article = this.articleDao.selectArticleByName(articleName);
		model.addAttribute("article", article);
		return "article";
	}

	@GetMapping(path = "/article/{articleId}/like/{clientId}")
	@ResponseBody
	String like(@PathVariable("articleId") int articleId,@PathVariable("clientId") String clientId){
		UserAction e = this.articleDao.selectUserAction(clientId,""+articleId,"article_like_up");
		if(e != null)
			return "您已过赞过该文章！";
		this.articleDao.setLikePlus(articleId);
		this.articleDao.insertUserAction(clientId,""+articleId,"article_like_up");
		return "success";
	}

	@GetMapping(path = "/article/{articleId}/unlike/{clientId}")
	@ResponseBody
	String unlike(@PathVariable("articleId") int articleId,@PathVariable("clientId") String clientId){
		UserAction e = this.articleDao.selectUserAction(clientId,""+articleId,"article_like_down");
		if(e == null)
			return "您没有赞过该文章！";
		this.articleDao.setLikeMinus(articleId);
		this.articleDao.deleteUserAction(clientId,""+articleId,"article_like_up");
		return "success";
	}

	/**
	 * 当用户读完一篇文章时，会触发该请求
	 */
	@PostMapping(path = "/article/viewer")
	@ResponseBody
	String viewerPlus(HttpServletRequest request){
		String clientId = request.getParameter("name");
		String v = request.getParameter("articleId");
		int articleId;
		try{
			articleId = Integer.valueOf(v);
		}catch(NumberFormatException e){
			LOG.error(String.format("URL [/article/viewer] format number error [%s], Message: ", v, e.getMessage()));
			return "failed";
		}
		this.articleDao.setViewerPlus(articleId);
		return "success";
	}

	/**
	 * 当用户在某一页面停留超过20秒时，触发该请求
	 * 这些页面不包含：article
	 */
	@PostMapping(path = "/reshow")
	@ResponseBody
	String reshow(HttpServletRequest request){
		String clientId = request.getParameter("name");
		String url = request.getParameter("url");
		this.articleDao.insertUserAction(clientId, url, "page_viewer");
		return "success";
	}


}
