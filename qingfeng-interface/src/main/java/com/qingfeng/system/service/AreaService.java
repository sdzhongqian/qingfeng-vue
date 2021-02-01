package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.AreaDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: AreaService
 * @ProjectName com.qingfeng
 * @Description: 地区SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:43
 */
@Service
@Transactional
public class AreaService extends CrudService<AreaDao,PageData> {

@Autowired
protected AreaDao areadao;


}