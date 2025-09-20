package com.shenmegui.inklessServer.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shenmegui.inklessServer.entity.Schedule;
import com.shenmegui.inklessServer.mapper.ScheduleMapper;

/**
 * @Title: ScheduleService
 * @Author qiangjun.chen
 * @Package com.shenmegui.inklessServer.service
 * @Date 2025/9/20 12:58
 * @description:
 */
public class ScheduleService {
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * 更新 schedule 表的版本信息
     */
    private void updateScheduleVersion(Schedule schedule) {
        if (schedule == null) {
            return;
        }
        LambdaUpdateWrapper<Schedule> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Schedule::getActiveVersion, schedule.getCurrentVersion());
        scheduleMapper.update(updateWrapper);
    }
}
