package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.example.springboot.entity.Article;
import com.example.springboot.mapper.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    public List<Article> selectList(Article article) {
        System.out.println(article);
        return null;
    }


    public void add(Article article) {
        // 設定當前時間
        article.setTime(DateUtil.now());
        // insert
        articleMapper.insert(article);
    }

    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Integer selectCountByDate(String dateStr) {
        return articleMapper.selectCountByDate(dateStr);
    }
}
