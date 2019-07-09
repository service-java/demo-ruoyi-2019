package com.ruoyi.web.controller.system;

import java.util.Arrays;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.Result;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    private String prefix = "system/config";

    private final ISysConfigService configService;

    @Autowired
    public SysConfigController(ISysConfigService configService) {
        this.configService = configService;
    }

    /**
     * 参数列表页面
     * @return html路径
     */
    @RequiresPermissions("system:config:view")
    @GetMapping
    public String config() {
        return prefix + "/config";
    }

    /**
     * 新增参数配置页面
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 修改参数配置页面
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
        mmap.put("config", configService.getById(configId));
        return prefix + "/edit";
    }

    /**
     * 查询参数配置列表
     */
    @RequiresPermissions("system:config:list")
    @PostMapping("/list")
    @ResponseBody
    public Result list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.list(config);
        return Result.success(getDataTable(list));
    }

    /**
     * 导出Excel
     */
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:config:export")
    @PostMapping("/export")
    @ResponseBody
    public Result export(SysConfig config) {
        List<SysConfig> list = configService.list(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(list, "参数数据");
        return Result.success();
    }

    /**
     * 新增保存参数配置
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public Result addSave(SysConfig config) {
        configService.save(config);
        return Result.success();
    }

    /**
     * 修改保存参数配置
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public Result editSave(SysConfig config) {
        configService.updateById(config);
        return Result.success();
    }

    /**
     * 删除参数配置
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public Result remove(String ids) {
        configService.removeByIds(Arrays.asList(Convert.toLongArray(ids)));
        return Result.success();
    }

    /**
     * 校验参数键名
     */
    @PostMapping("/checkConfigKeyUnique")
    @ResponseBody
    public Result checkConfigKeyUnique(SysConfig config) {
        return configService.checkConfigKeyUnique(config)?Result.success():Result.failed("");
    }
}
