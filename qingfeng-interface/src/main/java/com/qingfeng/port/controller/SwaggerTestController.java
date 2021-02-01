package com.qingfeng.port.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.util.Json;
import com.qingfeng.util.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Title: MytreeController
 * @ProjectName com.qingfeng
 * @Description: Controller层
 * @author anxingtao
 * @date 2020-9-22 22:45
 */
@Api(tags = "swagger案例接口")
@Controller
@RequestMapping(value = "/port/swaggerTest")
public class SwaggerTestController extends BaseController {


    /**
     * @Description: findList
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-9-22 22:51
     */
	@ApiOperation("查询列表数据")
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public void findList(HttpServletRequest request, HttpServletResponse response) throws IOException  {
    	PageData pd = new PageData(request);
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setData(pd);
        json.setSuccess(true);
        this.writeJson(response,json);
    }



}
