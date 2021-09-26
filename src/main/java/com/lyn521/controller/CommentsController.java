package com.lyn521.controller;

import com.lyn521.pojo.Comments;
import com.lyn521.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @RequestMapping("/getAll")
    public Object getAllComments(int page, int limit) {
        return commentsService.getAllComments(page, limit);
    }

    @RequestMapping("/del")
    public Object delCommentsById(String id) {
        return commentsService.delCommentsById(id);
    }

    @GetMapping("/getAllBySId")
    public Object getAllBySId(int page, int limit) {
        int id = request.getIntHeader("id");
        return commentsService.getAllBySId(page, limit, id);
    }

    @PostMapping("/add")
    public Object addComments(Comments comments){
        return commentsService.addComments(comments);
    }

    @PostMapping("/get/BySId")
    public Object getBySId(Comments comments){
        return commentsService.getBySId(comments.getSuserId());
    }
}
