package com.qingfeng.framework.monitor.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.monitor.server.SystemHardwareServer;
import com.qingfeng.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * @Title: SystemHardwareController
 * @ProjectName wdata
 * @Description: 监控服务
 * @author anxingtao
 * @date 2020-10-2 21:43
 */
@Api(tags = "服务监控信息接口")
@Controller
@RequestMapping(value = "/monitor/server")
public class SystemHardwareController extends BaseController {

    /** 
     * @Description: systemHardware 
     * @Param: [headers, response] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2021-1-1 18:22 
     */
    @ApiOperation("查询服务监控数据接口")
    @RequestMapping(value = "/systemHardware", method = RequestMethod.GET)
    public void systemHardware(@RequestHeader HttpHeaders headers, HttpServletResponse response) throws Exception {
        SystemHardwareServer server = new SystemHardwareServer();
        server.copyTo();
        Json json = new Json();
        json.setData(server);
        json.setSuccess(true);
        json.setMsg("操作成功。");
        this.writeJson(response,json);
    }


}
