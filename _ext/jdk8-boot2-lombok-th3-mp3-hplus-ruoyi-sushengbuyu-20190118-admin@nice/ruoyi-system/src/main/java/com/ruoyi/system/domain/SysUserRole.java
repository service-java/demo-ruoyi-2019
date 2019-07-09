package com.ruoyi.system.domain;

import lombok.Data;
import lombok.ToString;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author ruoyi
 */
@ToString
@Data
public class SysUserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
