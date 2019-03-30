package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysDept;

/**
 * 部门管理 数据层
 *
 * @author ruoyi
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
     int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
     SysDept selectDeptById(Long deptId);

    /**
     * 根据角色ID查询部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
     List<String> selectRoleDeptTree(Long roleId);

    /**
     * 查询部门是否存在用户
     */
    int checkDeptExistUser(Long deptId);
}
