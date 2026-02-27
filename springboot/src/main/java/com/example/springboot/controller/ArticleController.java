package com.example.springboot.controller;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Article;
import com.example.springboot.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    //   /selectById/1
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    //  查詢所有的數據：路徑/article/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(Article article){
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            Article article,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){

        PageInfo<Article> pageInfo = articleService.selectPage(article, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    //  RequestBody將前端傳來的jason轉成java對象或數組
    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result update(@PathVariable Integer id) {
        articleService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result update(@RequestBody List<Integer> ids) {
        articleService.deleteBatch(ids);
        return Result.success();
    }


}
