package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.SysConfigMapper;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configKey);
        SysConfig retConfig = baseMapper.selectOne(wrapper);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }

    @Override
    public List<SysConfig> list(SysConfig config) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        if(config != null){
            if(StringUtils.isNotEmpty(config.getConfigType())){
                wrapper.eq(SysConfig::getConfigType, config.getConfigType());
            }
            if(StringUtils.isNotEmpty(config.getConfigName())){
                wrapper.like(SysConfig::getConfigName, config.getConfigName());
            }
            if(StringUtils.isNotEmpty(config.getConfigKey())){
                wrapper.like(SysConfig::getConfigKey, config.getConfigKey());
            }

            if(StringUtils.isNotEmpty(config.getParams())){
                if(StringUtils.isNotEmpty(config.getParams().get(Constants.BEGIN_TIME))){
                    wrapper.ge(SysConfig::getCreateTime, config.getParams().get(Constants.BEGIN_TIME));
                }
                if(StringUtils.isNotEmpty(config.getParams().get(Constants.END_TIME))){
                    wrapper.le(SysConfig::getCreateTime, config.getParams().get(Constants.END_TIME));
                }
            }
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotNull(config.getConfigId())){
            wrapper.ne(SysConfig::getConfigId, config.getConfigId());
        }
        wrapper.eq(SysConfig::getConfigKey, config.getConfigKey());
        int result = baseMapper.selectCount(wrapper);
        return result == 0;
    }
}
