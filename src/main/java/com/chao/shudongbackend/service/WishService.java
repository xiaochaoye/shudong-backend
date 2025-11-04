package com.chao.shudongbackend.service;

import com.chao.shudongbackend.model.dto.WishCreateDTO;
import com.chao.shudongbackend.model.dto.WishUpdateDTO;
import com.chao.shudongbackend.model.vo.WishVO;
import com.chao.shudongbackend.model.dto.WishCompleteDTO;

import java.util.List;

/**
 * 许愿池服务接口
 */
public interface WishService {

    /**
     * 查看我的愿望
     * @param userId 用户ID
     * @return 愿望列表
     */
    List<WishVO> getMyWishes(Long userId);

    /**
     * 愿望编辑
     * @param wishId 愿望ID
     * @param updateDTO 更新数据
     * @param userId 用户ID
     * @return 更新后的愿望
     */
    WishVO updateWish(Long wishId, WishUpdateDTO updateDTO, Long userId);

    /**
     * 愿望删除
     * @param wishId 愿望ID
     * @param userId 用户ID
     * @return 删除是否成功
     */
    Boolean deleteWish(Long wishId, Long userId);

    /**
     * 随机取一个愿望
     * @param userId 用户ID
     * @return 随机愿望
     */
    WishVO getRandomWish(Long userId);

    /**
     * 发送心愿
     * @param createDTO 创建数据
     * @param userId 用户ID
     * @return 创建的愿望
     */
    WishVO createWish(WishCreateDTO createDTO, Long userId);

    /**
     * 实现心愿后从许愿池捞出
     * @param completeDTO 完成数据
     * @param userId 用户ID
     * @return 完成的愿望
     */
    WishVO completeWish(WishCompleteDTO completeDTO, Long userId);

    /**
     * 愿望统计
     * @return 统计结果
     */
    Object getWishStats();
}
