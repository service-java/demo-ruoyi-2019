package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author ruoyi
 */
@ToString
@Data
public class SysRoleDept {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
