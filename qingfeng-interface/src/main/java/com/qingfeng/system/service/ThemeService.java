package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.ThemeDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: ThemeService
 * @ProjectName wdata
 * @Description: 主题设置Service
 * @author anxingtao
 * @date 2020-9-28 17:44
 */
@Service
@Transactional
public class ThemeService extends CrudService<ThemeDao,PageData> {

    @Autowired
    protected ThemeDao themedao;


}