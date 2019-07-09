package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.mapper.SysUserPostMapper;
import com.ruoyi.system.service.ISysPostService;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Override
    public List<SysPost> list(SysPost post) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        if(post != null){
            if(StringUtils.isNotEmpty(post.getStatus())){
                wrapper.eq(SysPost::getStatus, post.getStatus());
            }
            if(StringUtils.isNotEmpty(post.getPostCode())){
                wrapper.like(SysPost::getPostCode, post.getPostCode());
            }
            if(StringUtils.isNotEmpty(post.getPostName())){
                wrapper.like(SysPost::getPostName, post.getPostName());
            }
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostsByUserId(Long userId) {
        List<SysPost> userPosts = baseMapper.selectPostsByUserId(userId);
        List<SysPost> posts = baseMapper.selectList(null);
        for (SysPost post : posts) {
            for (SysPost userRole : userPosts) {
                if (post.getPostId().longValue() == userRole.getPostId().longValue()) {
                    post.setFlag(true);
                    break;
                }
            }
        }
        return posts;
    }

    /**
     * 批量删除岗位信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deletePostByIds(String ids) throws BusinessException {
        Long[] postIds = Convert.toLongArray(ids);
        SysPost post;
        for (Long postId : postIds) {
            post = baseMapper.selectById(postId);
            if (countUserPostById(postId) > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return baseMapper.deleteBatchIds(Arrays.asList(postIds));
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotNull(post.getPostId())){
            wrapper.ne(SysPost::getPostId, post.getPostId());
        }
        wrapper.eq(SysPost::getPostName, post.getPostName());
        int result = baseMapper.selectCount(wrapper);
        if (result > 0) {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotNull(post.getPostId())){
            wrapper.ne(SysPost::getPostId, post.getPostId());
        }
        wrapper.eq(SysPost::getPostCode, post.getPostCode());
        int result = baseMapper.selectCount(wrapper);
        if (result > 0) {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }
}
