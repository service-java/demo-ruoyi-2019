package com.ruoyi.system.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    private final SysUserMapper sysUserMapper;

    @Autowired
    public SysDeptServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public SysDept getById(Serializable id) {
        return baseMapper.selectDeptById((Long) id);
    }

    @Override
    public List<SysDept> list(SysDept dept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDelFlag, Constants.DELFLAG_NORMAL);
        if(dept != null){
            if(null != dept.getParentId() && dept.getParentId() != 0){
                wrapper.eq(SysDept::getParentId, dept.getParentId());
            }
            if(StringUtils.isNotEmpty(dept.getStatus())){
                wrapper.eq(SysDept::getStatus, dept.getStatus());
            }
            if(StringUtils.isNotEmpty(dept.getDeptName())){
                wrapper.like(SysDept::getDeptName, dept.getDeptName());
            }
            if(null != dept.getParams().get("dataScope")){
                wrapper.last(dept.getParams().get("dataScope").toString());
            }
        }
        wrapper.orderByAsc(SysDept::getOrderNum);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<Map<String, Object>> selectDeptTree(SysDept dept) {
        List<Map<String, Object>> trees;
        List<SysDept> deptList = list(dept);
        trees = getTrees(deptList, false, null);
        return trees;
    }

    /**
     * 根据角色ID查询部门（数据权限）
     *
     * @param role 角色对象
     * @return 部门列表（数据权限）
     */
    @Override
    public List<Map<String, Object>> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Map<String, Object>> trees;
        List<SysDept> deptList = list((SysDept) null);
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleDeptList = baseMapper.selectRoleDeptTree(roleId);
            trees = getTrees(deptList, true, roleDeptList);
        } else {
            trees = getTrees(deptList, false, null);
        }
        return trees;
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param isCheck      是否需要选中
     * @param roleDeptList 角色已存在菜单列表
     * @return
     */
    private List<Map<String, Object>> getTrees(List<SysDept> deptList, boolean isCheck, List<String> roleDeptList) {

        List<Map<String, Object>> trees = new ArrayList<>();
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                Map<String, Object> deptMap = new HashMap<>(5);
                deptMap.put("id", dept.getDeptId());
                deptMap.put("pId", dept.getParentId());
                deptMap.put("name", dept.getDeptName());
                deptMap.put("title", dept.getDeptName());
                if (isCheck) {
                    deptMap.put("checked", roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                } else {
                    deptMap.put("checked", false);
                }
                trees.add(deptMap);
            }
        }
        return trees;
    }

    /**
     * 查询部门人数
     *
     * @param parentId 部门ID
     * @return 结果
     */
    @Override
    public int selectDeptCount(Long parentId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDelFlag, Constants.DELFLAG_NORMAL);
        wrapper.eq(SysDept::getParentId, parentId);
        return baseMapper.selectCount(wrapper);
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = baseMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        SysDept dept = new SysDept();
        dept.setDeptId(deptId);
        dept.setDelFlag(Constants.DELFLAG_DELETED);
        return baseMapper.updateById(dept);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = getById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        baseMapper.updateById(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId    部门ID
     * @param ancestors 元素列表
     */
    public void updateDeptChildren(Long deptId, String ancestors) {
        SysDept dept = new SysDept();
        dept.setParentId(deptId);
        List<SysDept> childrens = list(dept);
        for (SysDept children : childrens) {
            children.setAncestors(ancestors + "," + dept.getParentId());
        }
        if (childrens.size() > 0) {
            baseMapper.updateDeptChildren(childrens);
        }
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotNull(dept.getDeptId())){
            wrapper.ne(SysDept::getDeptId, dept.getDeptId());
        }
        wrapper.eq(SysDept::getParentId, dept.getParentId());
        wrapper.eq(SysDept::getDeptName, dept.getDeptName());
        int result = baseMapper.selectCount(wrapper);
        return result == 0;
    }
}
