package com.sycki.blog.pojo;

/**
 * Created by kxdmmr on 2017/9/9.
 */
public class ArticleIndex {
    String tag;
    String titles;
    String en_names;
    private String titleArr[];
    private String nameArr[];

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        titleArr = titles.split(",");
        this.titles = titles;
    }

    public String getEn_names() {
        return en_names;
    }

    public void setEn_names(String en_names) {
        nameArr = en_names.split(",");
        this.en_names = en_names;
    }

    public String[] getTitleArr() {
        return titleArr;
    }

    public String[] getNameArr() {
        return nameArr;
    }
}
