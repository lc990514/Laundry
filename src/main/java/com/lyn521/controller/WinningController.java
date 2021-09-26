package com.lyn521.controller;

import com.lyn521.service.WinningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("winning")
public class WinningController {
    @Autowired
    private WinningService winningService;
    @Autowired(required = false)
    HttpServletRequest request;

    @PostMapping("getAll")
    public Object getAll(){
        int id = request.getIntHeader("id");
        return winningService.getAll(id);
    }

    @PostMapping("intoWinning")
    public Object intoWinning(){
        int id = request.getIntHeader("id");
        return winningService.intoWinning(id);
    }
}
