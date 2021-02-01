package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.DictionaryDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: DictionaryService
 * @ProjectName com.qingfeng
 * @Description: 字典SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:43
 */
@Service
@Transactional
public class DictionaryService extends CrudService<DictionaryDao,PageData> {

@Autowired
protected DictionaryDao dictionarydao;


}