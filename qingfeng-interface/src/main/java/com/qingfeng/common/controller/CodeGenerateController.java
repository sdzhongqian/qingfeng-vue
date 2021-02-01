package com.qingfeng.common.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.util.PageData;
import com.qingfeng.util.freemarker.FreemarkerParaUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @ClassName: AutocodeController
 * @Description: 代码生成
 * @author Administrator
 * @date 2016-7-31 下午10:32:28
 *
 */
@Controller
@RequestMapping("/common/codeGenerate")
public class CodeGenerateController extends BaseController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

//	private PageData pd;

    public static Connection getMySQLConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qingfeng_slave?useUnicode=true&characterEncoding=utf8&autoReconnect=true", "root", "Root@123");
        return conn;
    }

    /**
     * @Description: createCode
     * @Param: [request, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-6-21 9:04
     */
    @RequestMapping("/createCode")
    public void createCode(HttpServletRequest request,HttpSession session){
        try {
            PageData pd = new PageData(request);

            String table = pd.getString("table_Name").trim();
            String isMore = pd.getString("isMore").trim();
            System.out.println(table);

            String fieldColumn = "";
            String insertColumn = "id,";
            String insertParam = "#{id},";
            String updateParam="";
            List<PageData> fieldList = new ArrayList<PageData>();

            Connection conn = getMySQLConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show full columns from " + table);
            while (rs.next()) {
                System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );
                fieldColumn+="a."+rs.getString("Field")+" as \""+rs.getString("Field")+"\",\n";
                if(!rs.getString("Field").equals("id")){
                    insertColumn+=rs.getString("Field")+",";
                    insertParam+="#{"+rs.getString("Field")+",jdbcType=VARCHAR},";
                    if(!rs.getString("Field").equals("update_time")&&!rs.getString("Field").equals("id")&&!rs.getString("Field").equals("create_user")&&!rs.getString("Field").equals("create_organize")&&!rs.getString("Field").equals("create_time")){
                        updateParam+="<if test=\""+rs.getString("Field")+" != null\">"+rs.getString("Field")+"=#{"+rs.getString("Field")+"},</if>\n";
                        if(!rs.getString("Field").equals("update_user")){
                            PageData fPd = new PageData();
                            fPd.put("field",rs.getString("Field"));
                            fPd.put("name",rs.getString("Comment"));
                            fieldList.add(fPd);
                        }
                    }
                }
            }
            updateParam+="update_time=#{update_time}";
            rs.close();
            stmt.close();
            conn.close();

            //处理模块名称首字母大写
            String[] strs = table.split("_");
            StringBuilder modelSb = new StringBuilder(strs[0].toLowerCase());
            StringBuilder classSb = new StringBuilder(strs[1].toLowerCase());
            classSb.setCharAt(0, Character.toUpperCase(classSb.charAt(0)));

            if(fieldColumn.length()>0){
                fieldColumn=fieldColumn.substring(0, fieldColumn.lastIndexOf(','));
            }
            if(insertColumn.length()>0){
                insertColumn=insertColumn.substring(0, insertColumn.length()-1);
            }
            if(insertParam.length()>0){
                insertParam=insertParam.substring(0, insertParam.length()-1);
            }
//            if(updateParam.length()>0){
//                updateParam=updateParam.substring(0, updateParam.length()-1);
//            }

            pd.put("modelName", modelSb.toString());//模块名称
            pd.put("className", classSb.toString());//类名称
            pd.put("fieldColumn", fieldColumn);
            pd.put("tableName", table);//表名称
            pd.put("insertColumn", insertColumn);
            pd.put("insertParam", insertParam);
            pd.put("updateParam", updateParam);
            pd.put("primaryParam", "a.id=#{id}");
            pd.put("mkName", classSb.toString()+"信息");
            pd.put("isMore",isMore);

            //1、创建数据模型
            Map<String,Object> root = new HashMap<String,Object>();
            //2、为数据模型添加值
            root.put("pd", pd);
            root.put("fieldList",fieldList);

            //生成Controller.java
            fprint("single/TemplateController.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+ FreemarkerParaUtil.controller+classSb.toString()+"Controller.java");
            //生成Mapper.java
            fprint("single/TemplateDao.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.dao+classSb.toString()+"Dao.java");
            //生成Service.java
            fprint("single/TemplateService.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.service+classSb.toString()+"Service.java");
            //生成Mapper.xml
            fprint("single/TemplateMapper.xml.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.mapper+classSb.toString()+"Mapper.xml");
            //生成list.jsp
            fprint("single/TemplateList.jsp.ftl", root, FreemarkerParaUtil.outputDir+"jsp/"+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_list.jsp");
            //生成list.jsp
            fprint("single/TemplateAdd.jsp.ftl", root, FreemarkerParaUtil.outputDir+"jsp/"+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_add.jsp");
            //生成list.jsp
            fprint("single/TemplateUpdate.jsp.ftl", root, FreemarkerParaUtil.outputDir+"jsp/"+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_update.jsp");
            //生成list.jsp
            fprint("single/TemplateInfo.jsp.ftl", root, FreemarkerParaUtil.outputDir+"jsp/"+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_info.jsp");
            if(isMore.equals("Y")){
                //生成addMore.jsp
                fprint("single/TemplateAddMore.jsp.ftl", root, FreemarkerParaUtil.outputDir+"jsp/"+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_addMore.jsp");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @Description: createMultiCode
     * @Param: [request, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-6-25 0:10
     */
    @RequestMapping("/createMultiCode")
    public void createMultiCode(HttpServletRequest request,HttpSession session){
        try {
            PageData pd = new PageData(request);

            String table = pd.getString("table_Name").trim();
            String child_table = pd.getString("child_table_Name").trim();
            String link_id = pd.getString("link_id").trim();
            System.out.println(table);

            //主表处理字段
            String fieldColumn = "";
            String insertColumn = "id,";
            String insertParam = "#{id},";
            String updateParam="";
            //子表处理字段
            String childFieldColumn = "";
            String childInsertColumn = "id,";
            String childInsertParam = "#{id},";
            String childUpdateParam="";

            List<PageData> fieldList = new ArrayList<PageData>();
            List<PageData> childFieldList = new ArrayList<PageData>();

            Connection conn = getMySQLConnection();
            Statement stmt = conn.createStatement();
            //查询主表
            ResultSet rs = stmt.executeQuery("show full columns from " + table);
            while (rs.next()) {
                System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );
                fieldColumn+="a."+rs.getString("Field")+" as \""+rs.getString("Field")+"\",\n";
                if(!rs.getString("Field").equals("id")){
                    insertColumn+=rs.getString("Field")+",";
                    insertParam+="#{"+rs.getString("Field")+",jdbcType=VARCHAR},";
                    if(!rs.getString("Field").equals("update_time")&&!rs.getString("Field").equals("id")&&!rs.getString("Field").equals("create_user")&&!rs.getString("Field").equals("create_organize")&&!rs.getString("Field").equals("create_time")){
                        updateParam+="<if test=\""+rs.getString("Field")+" != null\">"+rs.getString("Field")+"=#{"+rs.getString("Field")+"},</if>\n";
                        if(!rs.getString("Field").equals("update_user")){
                            PageData fPd = new PageData();
                            fPd.put("field",rs.getString("Field"));
                            fPd.put("name",rs.getString("Comment"));
                            fieldList.add(fPd);
                        }
                    }
                }
            }
            updateParam+="update_time=#{update_time}";
            rs.close();

            //查询子表
            ResultSet child_rs = stmt.executeQuery("show full columns from " + child_table);
            while (child_rs.next()) {
                System.out.println(child_rs.getString("Field") + "\t:\t"+  child_rs.getString("Comment") );
                childFieldColumn+="a."+child_rs.getString("Field")+" as \""+child_rs.getString("Field")+"\",\n";
                if(!child_rs.getString("Field").equals("id")){
                    childInsertColumn+=child_rs.getString("Field")+",";
                    childInsertParam+="#{"+child_rs.getString("Field")+",jdbcType=VARCHAR},";
                    if(!child_rs.getString("Field").equals("update_time")&&!child_rs.getString("Field").equals("id")&&!child_rs.getString("Field").equals("create_user")&&!child_rs.getString("Field").equals("create_organize")&&!child_rs.getString("Field").equals("create_time")){
                        childUpdateParam+="<if test=\""+child_rs.getString("Field")+" != null\">"+child_rs.getString("Field")+"=#{"+child_rs.getString("Field")+"},</if>\n";
                        if(!child_rs.getString("Field").equals("update_user")&&!child_rs.getString("Field").equals(link_id)){
                            PageData fPd = new PageData();
                            fPd.put("field",child_rs.getString("Field"));
                            fPd.put("name",child_rs.getString("Comment"));
                            childFieldList.add(fPd);
                        }
                    }
                }
            }
            childUpdateParam+="update_time=#{update_time}";

            child_rs.close();

            stmt.close();
            conn.close();

            //处理模块名称首字母大写
            String[] strs = table.split("_");
            StringBuilder modelSb = new StringBuilder(strs[0].toLowerCase());
            StringBuilder classSb = new StringBuilder(strs[1].toLowerCase());
            classSb.setCharAt(0, Character.toUpperCase(classSb.charAt(0)));

            if(fieldColumn.length()>0){
                fieldColumn=fieldColumn.substring(0, fieldColumn.lastIndexOf(','));
            }
            if(insertColumn.length()>0){
                insertColumn=insertColumn.substring(0, insertColumn.length()-1);
            }
            if(insertParam.length()>0){
                insertParam=insertParam.substring(0, insertParam.length()-1);
            }

            if(childFieldColumn.length()>0){
                childFieldColumn=childFieldColumn.substring(0, childFieldColumn.lastIndexOf(','));
            }
            if(childInsertColumn.length()>0){
                childInsertColumn=childInsertColumn.substring(0, childInsertColumn.length()-1);
            }
            if(childInsertParam.length()>0){
                childInsertParam=childInsertParam.substring(0, childInsertParam.length()-1);
            }

            pd.put("modelName", modelSb.toString());//模块名称
            pd.put("className", classSb.toString());//类名称
            pd.put("fieldColumn", fieldColumn);
            pd.put("tableName", table);//表名称
            pd.put("insertColumn", insertColumn);
            pd.put("insertParam", insertParam);
            pd.put("updateParam", updateParam);
            pd.put("primaryParam", "a.id=#{id}");
            pd.put("mkName", classSb.toString()+"信息");

            pd.put("childTableName", child_table);//子表名称
            pd.put("childFieldColumn", childFieldColumn);
            pd.put("childInsertColumn", childInsertColumn);
            pd.put("childInsertParam", childInsertParam);
            pd.put("childUpdateParam", childUpdateParam);
            pd.put("link_id",link_id);


            //1、创建数据模型
            Map<String,Object> root = new HashMap<String,Object>();
            //2、为数据模型添加值
            root.put("pd", pd);
            root.put("fieldList",fieldList);
            root.put("childFieldList",childFieldList);

            //生成Controller.java
            fprint("multi/TemplateController.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.controller+classSb.toString()+"Controller.java");
            //生成Mapper.java
            fprint("multi/TemplateDao.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.dao+classSb.toString()+"Dao.java");
            //生成Service.java
            fprint("multi/TemplateService.java.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.service+classSb.toString()+"Service.java");
            //生成Mapper.xml
            fprint("multi/TemplateMapper.xml.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+FreemarkerParaUtil.mapper+classSb.toString()+"Mapper.xml");
            //生成list.jsp
            fprint("multi/TemplateList.jsp.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_list.jsp");
            //生成list.jsp
            fprint("multi/TemplateAdd.jsp.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_add.jsp");
            //生成list.jsp
            fprint("multi/TemplateUpdate.jsp.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_update.jsp");
            //生成list.jsp
            fprint("multi/TemplateInfo.jsp.ftl", root, FreemarkerParaUtil.outputDir+modelSb+File.separator+classSb.toString().toLowerCase()+"/"+classSb.toString().toLowerCase()+"_info.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public void fprint(String templatePath,Object obj,String outPath) throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");

        //ContextLoader loader = new ContextLoader();
        Template template = configuration.getTemplate(templatePath);

        String dirpath = outPath.substring(0,outPath.lastIndexOf("/"));
        System.out.println(dirpath);
        File dirFile = new File(dirpath);
        if(!dirFile.exists()){
            dirFile.mkdir();
            dirFile.mkdirs();
        }

        File file = new File(outPath);
        Writer out = new FileWriter(file);
        template.process(obj, out);//输出
        out.close();
    }


    public static void main(String[] args) {
        String dirpath = "E:/autoCode/cngzz/controller";
        System.out.println(dirpath);
        File dirFile = new File(dirpath);
        System.out.println(!dirFile.exists());
        if(!dirFile.exists()){
            dirFile.mkdir();
            dirFile.mkdirs();
        }
    }

}
