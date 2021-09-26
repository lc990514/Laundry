package com.lyn521.service;

public interface WinningService {
    /**
     * 获取所有抽经记录
     * @param id
     * @return
     */
    Object getAll(int id);

    /**
     * 进行抽奖
     * @return
     */
    Object intoWinning(int id);
}
