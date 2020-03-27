package com.ruoyi.system.util;

import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.SysUser;

public class PasswordUtil
{
    public static boolean matches(SysUser user, String newPassword)
    {
        String encryptedPassword = encryptPassword(user.getLoginName(), newPassword, user.getSalt());
        return user.getPassword().equals(encryptedPassword);
    }

    public static String encryptPassword(String username, String password, String salt)
    {
        return Md5Utils.hash(username + password + salt);
    }
}
