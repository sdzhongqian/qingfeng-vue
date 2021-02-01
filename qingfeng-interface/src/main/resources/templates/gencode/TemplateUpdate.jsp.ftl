<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../system/admin/top.jsp"%>

<#assign richText = 'false'>
<#assign checkboxText = 'false'>
<form class="layui-form" action="" id="form">
    <table width="95%" style="margin: 0 auto">
    <#if tablePd.temp_type == '2'>
        <c:if test="${'$'}{p.parent_name==''||p.parent_name==null}">
            <tr>
                <td width="20%" align="right"><label class="layui-form-label">父级名称：<span style="color: red">*</span></label></td>
                <td colspan="3"><input type="text" readonly value="${tablePd.menu_name}" class="layui-input"></td>
            </tr>
        </c:if>
        <c:if test="${'$'}{p.parent_name!=''&&p.parent_name!=null}">
            <tr>
                <td width="20%" align="right"><label class="layui-form-label">父级名称：<span style="color: red">*</span></label></td>
                <td colspan="3"><input type="text" readonly value="${'$'}{p.parent_name }" class="layui-input"></td>
            </tr>
        </c:if>
    </#if>
<#list fieldList as obj>
    <#if obj.field_operat == 'Y'>
    <#if obj.show_type == '1'||obj.show_type == '7'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3"><input type="text" name="${obj.field_name}" id="${obj.field_name}" value="${'$'}{p.${obj.field_name}}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input"></td>
        </tr>
    </#if>
    <#if obj.show_type == '2'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3"><textarea name="${obj.field_name}" id="${obj.field_name}" lay-verify="${obj.verify_rule?replace(',','|')}" placeholder="请输入${obj.field_comment}" class="layui-textarea">${'$'}{p.${obj.field_name}}</textarea></td>
        </tr>
    </#if>
    <#if obj.show_type == '3'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3">
                <select name="${obj.field_name}" id="${obj.field_name}" lay-verify="${obj.verify_rule?replace(',','|')}" style="height: 32px;" class="layui-input">
                    <option value=""></option>
                <#if obj.option_content?contains(";")>
                    <#list obj.option_content?split(";") as name>
                    <#assign param = name?split("/")>
                    <option <c:if test="${'$'}{p.${obj.field_name}=='${param[0]}'}"> selected </c:if> value="${param[0]}">${param[1]}</option>
                    </#list>
                </#if>
                </select>
            </td>
        </tr>
    </#if>
    <#if obj.show_type == '4'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3">
                <div id="div_${obj.field_name}">
                <#if obj.option_content?contains(";")>
                    <#list obj.option_content?split(";") as name>
                    <#assign param = name?split("/")>
                    <input type="radio" <c:if test="${'$'}{p.${obj.field_name}=='${param[0]}'}"> checked </c:if> name="${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                    </#list>
                </#if>
                </div>
            </td>
        </tr>
    </#if>
    <#if obj.show_type == '5'>
        <#assign checkboxText = 'true'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3">
                <div id="div-${obj.field_name}">
                <#if obj.option_content?contains(";")>
                    <#list obj.option_content?split(";") as name>
                    <#assign param = name?split("/")>
                    <input type="checkbox" lay-filter='checkField' <c:if test="${'$'}{p.${obj.field_name}.indexOf('${param[0]}')!=-1}"> checked </c:if> name="div-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                    </#list>
                </#if>
                </div>
                <input type="hidden" name="${obj.field_name}" id="${obj.field_name}" value="${'$'}{p.${obj.field_name}}" />
            </td>
        </tr>
    </#if>
    <#if obj.show_type == '6'>
        <#assign richText = 'true'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3">
                <script type="text/plain" id="richText_${obj.field_name}" style="width: 100%;height:500px;">${'$'}{p.${obj.field_name}}</script>
                <input type="hidden" name="${obj.field_name}" id="${obj.field_name}">
            </td>
        </tr>
    </#if>
    <#if obj.show_type == '8'>
        <tr>
            <td width="15%" align="right">
                <label class="layui-form-label">${obj.field_comment}：
                    <#if obj.verify_rule?contains("required")>
                        <span style="color: red">*</span>
                    </#if>
                </label>
            </td>
            <td colspan="3">
                <div style="margin-top:5px;">
                    <input type="hidden" name="${obj.field_name}" id="fileIds_${obj.field_name}" value="${'$'}{p.${obj.field_name}}" class="layui-input">
                    <button type="button" class="layui-btn layui-btn-xs" id="upload_file_${obj.field_name}">选择附件</button>
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th>附件名称</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="tbody_file_${obj.field_name}">
                        <c:forEach items="${'$'}{${obj.field_name}FileList}" var="v" varStatus="vs">
                            <tr id="tr_ls${'$'}{v.id}">
                                <td>${'$'}{v.name}</td>
                                <td>
                                    <div class="layui-btn-group">
                                        <button type="button" onclick="downloadFile('${'$'}{v.id}','${'$'}{v.file_path}','${'$'}{v.name}');" class="layui-btn layui-btn-xs">下载</button>
                                        <button type="button" onclick="delMoreFile('ls${'$'}{v.id}','${'$'}{v.id}','${'$'}{v.file_path}');" class="layui-btn layui-btn-xs layui-btn-danger">删除</button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </td>
        </tr>
    </#if>
</#if>
</#list>

    <#if tablePd.temp_type == '1'>
        <tr>
            <td colspan="4">
                <table class="layui-table" style="width:95%;margin:10px auto;">
                    <thead>
                    <#list linkFieldList as obj>
                        <#if obj.field_operat == 'Y' && obj.show_type != '0'>
                        <th>${obj.field_comment}<#if obj.verify_rule?contains("required")><span style="color: red">*</span></#if></th>
                        </#if>
                    </#list>
                    <th>操作</th>
                    </thead>
                    <tbody id="child_table">
                    <c:if test="${'$'}{fn:length(list)>0}">
                        <c:forEach items="${'$'}{list}" var="var" varStatus="status">
                            <tr id="child_tr_ls${'$'}{var.id}">
                                <#list linkFieldList as obj>
                                    <#if obj.field_operat == 'Y'>
                                        <#if obj.show_type == '1'||obj.show_type == '7'>
                                            <td><input type="text" name="child_${obj.field_name}" id="${obj.field_name}_1" value="${'$'}{var.${obj.field_name}}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input"></td>
                                        </#if>
                                        <#if obj.show_type == '2'||obj.show_type == '6'>
                                            <td><textarea name="child_${obj.field_name}" id="${obj.field_name}_1" lay-verify="${obj.verify_rule?replace(',','|')}" placeholder="请输入${obj.field_comment}" class="layui-textarea">${'$'}{var.${obj.field_name}}</textarea></td>
                                        </#if>
                                        <#if obj.show_type == '3'>
                                            <td>
                                                <select name="child_${obj.field_name}" id="${obj.field_name}_1" lay-verify="${obj.verify_rule?replace(',','|')}" style="height: 32px;" class="layui-input">
                                                    <option value=""></option>
                                                    <#if obj.option_content?contains(";")>
                                                        <#list obj.option_content?split(";") as name>
                                                            <#assign param = name?split("/")>
                                                            <option <c:if test="${'$'}{var.${obj.field_name}=='${param[0]}'}"> selected </c:if> value="${param[0]}">${param[1]}</option>
                                                        </#list>
                                                    </#if>
                                                </select>
                                            </td>
                                        </#if>
                                        <#if obj.show_type == '4'>
                                            <td>
                                                <div id="div_${'$'}{var.id}_${obj.field_name}">
                                                    <#if obj.option_content?contains(";")>
                                                        <#list obj.option_content?split(";") as name>
                                                            <#assign param = name?split("/")>
                                                            <input type="radio" <c:if test="${'$'}{var.${obj.field_name}=='${param[0]}'}"> checked </c:if> name="child_${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#list>
                                                    </#if>
                                                </div>
                                            </td>
                                        </#if>
                                        <#if obj.show_type == '5'>
                                            <#assign checkboxText = 'true'>
                                            <td>
                                                <div id="div-${'$'}{var.id}-${obj.field_name}">
                                                    <#if obj.option_content?contains(";")>
                                                        <#list obj.option_content?split(";") as name>
                                                            <#assign param = name?split("/")>
                                                            <input type="checkbox" lay-filter='checkField' <c:if test="${'$'}{var.${obj.field_name}.indexOf('${param[0]}')!=-1}"> checked </c:if> name="div-${'$'}{var.id}-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#list>
                                                    </#if>
                                                </div>
                                                <input type="hidden" name="child_${obj.field_name}" id="${obj.field_name}_${'$'}{var.id}" value="${'$'}{var.${obj.field_name}}" />
                                            </td>
                                        </#if>
                                        <#if obj.show_type == '8'>
                                            <td>
                                                <div style="margin-top:5px;">
                                                    <input type="hidden" name="child_${obj.field_name}" id="fileIds_${obj.field_name}_${'$'}{var.id}" value="${'$'}{var.${obj.field_name}}" class="layui-input">
                                                    <button type="button" class="layui-btn layui-btn-xs" id="upload_file_${obj.field_name}_${'$'}{var.id}">选择附件</button>
                                                    <table class="layui-table">
                                                        <thead>
                                                        <tr>
                                                            <th>附件名称</th>
                                                            <th>操作</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody id="tbody_file_${obj.field_name}_${'$'}{var.id}">
                                                        <c:forEach items="${'$'}{var.${obj.field_name}FileList}" var="v" varStatus="vs">
                                                            <tr id="tr_ls${'$'}{v.id}">
                                                                <td>${'$'}{v.name}</td>
                                                                <td>
                                                                    <div class="layui-btn-group">
                                                                        <button type="button" onclick="downloadFile('${'$'}{v.id}','${'$'}{v.file_path}','${'$'}{v.name}');" class="layui-btn layui-btn-xs">下载</button>
                                                                        <button type="button" onclick="delMoreFile('ls${'$'}{v.id}','${'$'}{v.id}','${'$'}{v.file_path}');" class="layui-btn layui-btn-xs layui-btn-danger">删除</button>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </td>
                                        </#if>
                                        <#if obj.show_type == '0'>
                                            <input type="hidden" name="child_${obj.field_name}" value="${obj.default_value}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input">
                                        </#if>
                                    </#if>
                                </#list>
                                <td style="width:60px;">
                                    <div class="layui-btn-group">
                                        <input type="hidden" name="c_ids" value="${'$'}{var.id}">
                                        <button type="button" onclick="addLine();" class="layui-btn layui-btn-xs"><i class="layui-icon"></i></button>
                                        <c:if test="${'$'}{status.index!=0}">
                                            <button type="button" onclick="delLine('ls${'$'}{var.id}');" class="layui-btn layui-btn-xs layui-btn-danger"><i class="layui-icon"></i></button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${'$'}{fn:length(list)==0}">
                        <tr>
                            <#list linkFieldList as obj>
                                <#if obj.field_operat == 'Y'>
                                    <#if obj.show_type == '1'||obj.show_type == '7'>
                                        <td><input type="text" name="child_${obj.field_name}" id="${obj.field_name}_1" value="${obj.default_value}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input"></td>
                                    </#if>
                                    <#if obj.show_type == '2'||obj.show_type == '6'>
                                        <td><textarea name="child_${obj.field_name}" id="${obj.field_name}_1" lay-verify="${obj.verify_rule?replace(',','|')}" placeholder="请输入${obj.field_comment}" class="layui-textarea">${obj.default_value}</textarea></td>
                                    </#if>
                                    <#if obj.show_type == '3'>
                                        <td>
                                            <select name="child_${obj.field_name}" id="${obj.field_name}_1" lay-verify="${obj.verify_rule?replace(',','|')}" style="height: 32px;" class="layui-input">
                                                <option value=""></option>
                                                <#if obj.option_content?contains(";")>
                                                    <#list obj.option_content?split(";") as name>
                                                        <#assign param = name?split("/")>
                                                        <#if obj.default_value == param[0]>
                                                            <option selected value="${param[0]}">${param[1]}</option>
                                                        </#if>
                                                        <#if obj.default_value != param[0]>
                                                            <option value="${param[0]}">${param[1]}</option>
                                                        </#if>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </td>
                                    </#if>
                                    <#if obj.show_type == '4'>
                                        <td>
                                            <div id="div_1_${obj.field_name}">
                                                <#if obj.option_content?contains(";")>
                                                    <#list obj.option_content?split(";") as name>
                                                        <#assign param = name?split("/")>
                                                        <#if obj.default_value == param[0]>
                                                            <input type="radio" checked name="child_${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#if>
                                                        <#if obj.default_value != param[0]>
                                                            <input type="radio" name="child_${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#if>
                                                    </#list>
                                                </#if>
                                            </div>
                                        </td>
                                    </#if>
                                    <#if obj.show_type == '5'>
                                        <#assign checkboxText = 'true'>
                                        <td>
                                            <div id="div-1-${obj.field_name}">
                                                <#if obj.option_content?contains(";")>
                                                    <#list obj.option_content?split(";") as name>
                                                        <#assign param = name?split("/")>
                                                        <#if obj.default_value?contains(param[0])>
                                                            <input type="checkbox" lay-filter='checkField' checked name="div-1-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#if>
                                                        <#if !obj.default_value?contains(param[0])>
                                                            <input type="checkbox" lay-filter='checkField' name="div-1-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">
                                                        </#if>
                                                    </#list>
                                                </#if>
                                            </div>
                                            <input type="hidden" name="child_${obj.field_name}" id="${obj.field_name}_1" value="${obj.default_value}" />
                                        </td>
                                    </#if>
                                    <#if obj.show_type == '8'>
                                        <td>
                                            <div style="margin-top:5px;">
                                                <input type="hidden" name="child_${obj.field_name}" id="fileIds_${obj.field_name}_1" value="" class="layui-input">
                                                <button type="button" class="layui-btn layui-btn-xs" id="upload_file_${obj.field_name}_1">选择附件</button>
                                                <table class="layui-table">
                                                    <thead>
                                                    <tr>
                                                        <th>附件名称</th>
                                                        <th>操作</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody id="tbody_file_${obj.field_name}_1">
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </#if>
                                    <#if obj.show_type == '0'>
                                        <input type="hidden" name="child_${obj.field_name}" value="${obj.default_value}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input">
                                    </#if>
                                </#if>
                            </#list>
                            <td style="width:60px;">
                                <div class="layui-btn-group">
                                    <input type="hidden" name="c_ids" value="">
                                    <button type="button" onclick="addLine();" class="layui-btn layui-btn-xs"><i class="layui-icon"></i></button>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    <tr></tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </#if>
        <tr>
            <td style="text-align: center; padding-top: 10px;" colspan="4">
                <div class="layui-form-item">
                    <input type="hidden" name="id" id="id" value="${'$'}{p.id}" />
                    <button type="button" class="layui-btn layui-btn-sm" id="submit_button" lay-submit="" lay-filter="submit_form">保存</button>
                    <button type="button" class="layui-btn layui-btn-danger layui-btn-sm" id="cancel">取消</button>
                </div>
            </td>
        </tr>
    </table>
</form>

<script type="text/javascript" src="${'$'}{pageContext.request.contextPath}/resources/plugins/xadmin/lib/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="${'$'}{pageContext.request.contextPath}/resources/js/qfverify.js"></script>
<script type="text/javascript" src="${'$'}{pageContext.request.contextPath}/resources/js/qfAjaxReq.js"></script>
<script type="text/javascript" src="${'$'}{pageContext.request.contextPath}/resources/js/uploadFile.js"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<#if richText == 'true'>
<link href="${'$'}{pageContext.request.contextPath}/resources/plugins/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${'$'}{pageContext.request.contextPath}/resources/plugins/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${'$'}{pageContext.request.contextPath}/resources/plugins/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="${'$'}{pageContext.request.contextPath}/resources/plugins/umeditor/lang/zh-cn/zh-cn.js"></script>
</#if>

<script>
    var form,layer,layedit,laydate,upload;
    layui.use(['form', 'layedit', 'laydate','upload'], function(){
        form = layui.form;
        layer = layui.layer;
        layedit = layui.layedit;
        laydate = layui.laydate;
        upload = layui.upload;

        ${'$'}('#cancel').on('click',function (){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        })

        //初始化
<#list fieldList as obj>
    <#if obj.field_operat == 'Y'>
    <#if obj.show_type == '3'>
        <#if !obj.option_content?contains(";")>
        findSelectDictionary('${obj.option_content}','${obj.field_name}','${'$'}{p.${obj.field_name}}');
        </#if>
    </#if>
    <#if obj.show_type == '4'>
        <#if !obj.option_content?contains(";")>
        findRadioDictionary('${obj.option_content}','div_','${obj.field_name}','${'$'}{p.${obj.field_name}}');
        </#if>
    </#if>
    <#if obj.show_type == '5'>
        <#assign checkboxText = 'true'>
        <#if !obj.option_content?contains(";")>
        findCheckboxDictionary('${obj.option_content}','div-','${obj.field_name}','${'$'}{p.${obj.field_name}}');
        </#if>
    </#if>
    <#if obj.show_type == '7'>
        initDateType("${obj.field_name}",false);
    </#if>
    <#if obj.show_type == '6'>
        //实例化编辑器
        var richText_${obj.field_name} = UM.getEditor('richText_${obj.field_name}', {zIndex: 1});
    </#if>
    <#if obj.show_type == '8'>
        uploadMoreFile('${obj.field_name}','${obj.field_name}');
    </#if>
    </#if>
</#list>
<#if tablePd.temp_type == '1'>
    <#list linkFieldList as obj>
        <#if obj.field_operat == 'Y'>
            <#if obj.show_type == '3'>
                <#if !obj.option_content?contains(";")>
                    findSelectDictionary('${obj.option_content}','${obj.field_name}_1','${obj.default_value}');
                </#if>
            </#if>
            <#if obj.show_type == '4'>
                <#if !obj.option_content?contains(";")>
                    findRadioDictionary('${obj.option_content}','div_1_','${obj.field_name}','${obj.default_value}');
                </#if>
            </#if>
            <#if obj.show_type == '5'>
                <#assign checkboxText = 'true'>
                <#if !obj.option_content?contains(";")>
                    findCheckboxDictionary('${obj.option_content}','div-1-','${obj.field_name}','${obj.default_value}');
                </#if>
            </#if>
            <#if obj.show_type == '7'>
                initDateType("${obj.field_name}_1",true);
            </#if>
            <#if obj.show_type == '8'>
                uploadMoreFile('${obj.field_name}_1','${obj.field_name}');
            </#if>
        </#if>
    </#list>
${'$'}.each(${'$'}{listJson},function(i,n){
        <#list linkFieldList as obj>
            <#if obj.field_operat == 'Y'>
                <#if obj.show_type == '3'>
                    <#if !obj.option_content?contains(";")>
                        findSelectDictionary('${obj.option_content}','${obj.field_name}_'+ n.id,'${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '4'>
                    <#if !obj.option_content?contains(";")>
                        findRadioDictionary('${obj.option_content}','div_'+ n.id+'_','${obj.field_name}_'+ n.id,'${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '5'>
                    <#assign checkboxText = 'true'>
                    <#if !obj.option_content?contains(";")>
                        findCheckboxDictionary('${obj.option_content}','div-'+ n.id+'-','${obj.field_name}_'+ n.id,'${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '7'>
                    initDateType("${obj.field_name}_"+id,true);
                </#if>
                <#if obj.show_type == '8'>
                    uploadMoreFile('${obj.field_name}_'+ n.id,'${obj.field_name}');
                </#if>
            </#if>
        </#list>
    })
</#if>
        //自定义验证规则
        form.verify(form_verify);

        //监听提交
        form.on('submit(submit_form)', function(data){
            layer.msg('正在提交数据。');

        <#list fieldList as obj>
            <#if obj.field_operat == 'Y'>
            <#if obj.show_type == '6'>
            ${'$'}("#${obj.field_name}").val(UM.getEditor('richText_${obj.field_name}').getContent());
            </#if>
        </#if>
        </#list>
            ${'$'}("#submit_button").attr('disabled',true);
            ${'$'}.ajax({
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "${'$'}{pageContext.request.contextPath}/${tablePd.mod_name}/${tablePd.bus_name}/update" ,//url
                data: ${'$'}('#form').serialize(),
                success: function (res) {
                    if (res.success) {
                        layer.msg("数据更新成功。", {time: 2000},function(){
                            setOpenCloseParam("reload");
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        });
                    }else{
                        ${'$'}("#submit_button").attr('disabled',false);
                        if(res.loseSession=='loseSession'){
                            loseSession(res.msg,res.url)
                        }else{
                            layer.msg(res.msg, {time: 2000});
                        }
                    }
                },
                error : function() {
                    ${'$'}("#submit_button").attr('disabled',false);
                    layer.msg("异常！");
                }
            });
            return false;
        });

    <#if checkboxText == 'true'>
        //处理复选框
        form.on('checkbox(checkField)', function(obj){
            var obj_name = obj.elem.name;
            var values = obj_name.split('-');
            //获取值
            var checkboxValue="";
            ${'$'}("input:checkbox[name='"+obj_name+"']:checked").each(function() { // 遍历name=standard的多选框
                checkboxValue += ',' + $(this).val();
            });
            if(values.length==2){//添加、编辑
                ${'$'}("#"+values[1]).val(checkboxValue);
            }else if(values.length==3){//批量添加
                ${'$'}("#"+values[2]+"_"+values[1]).val(checkboxValue);
            }
            form.render();
            return false;
        });
    </#if>

    });


    <#if tablePd.temp_type == '1'>
    var index = 1;
    function addLine(){
        index ++;
        var tt = '<tr id="tr_'+index+'">';
        <#list linkFieldList as obj>
            <#if obj.field_operat == 'Y'>
                <#if obj.show_type == '1'||obj.show_type == '7'>
                    tt += '<td><input type="text" name="child_${obj.field_name}" id="${obj.field_name}_'+index+'" value="${obj.default_value}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input"></td>';
                </#if>
                <#if obj.show_type == '2'||obj.show_type == '6'>
                    tt += '<td><textarea name="child_${obj.field_name}" id="${obj.field_name}_'+index+'" lay-verify="${obj.verify_rule?replace(',','|')}" placeholder="请输入${obj.field_comment}" class="layui-textarea">${obj.default_value}</textarea></td>';
                </#if>
                <#if obj.show_type == '3'>
                    tt += '<td>';
                    tt += '<select name="child_${obj.field_name}" id="${obj.field_name}_'+index+'" lay-verify="${obj.verify_rule?replace(',','|')}" style="height: 32px;" class="layui-input">';
                    tt += '<option value=""></option>';
                    <#if obj.option_content?contains(";")>
                        <#list obj.option_content?split(";") as name>
                            <#assign param = name?split("/")>
                            <#if obj.default_value == param[0]>
                                tt += '<option selected value="${param[0]}">${param[1]}</option>';
                            </#if>
                            <#if obj.default_value != param[0]>
                                tt += '<option value="${param[0]}">${param[1]}</option>';
                            </#if>
                        </#list>
                    </#if>
                    tt += '</select>';
                    tt += '</td>';
                </#if>
                <#if obj.show_type == '4'>
                    tt += '<td>';
                    tt += '<div id="div_'+index+'_${obj.field_name}">';
                    <#if obj.option_content?contains(";")>
                        <#list obj.option_content?split(";") as name>
                            <#assign param = name?split("/")>
                            <#if obj.default_value == param[0]>
                                tt += '<input type="radio" checked name="child_${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">';
                            </#if>
                            <#if obj.default_value != param[0]>
                                tt += '<input type="radio" name="child_${obj.field_name}" value="${param[0]}" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">';
                            </#if>
                        </#list>
                    </#if>
                    tt += '</div>';
                    tt += '</td>';
                </#if>
                <#if obj.show_type == '5'>
                    tt += '<td>';
                    tt += '<div id="div-'+index+'-${obj.field_name}">';
                    <#if obj.option_content?contains(";")>
                        <#list obj.option_content?split(";") as name>
                            <#assign param = name?split("/")>
                            <#if obj.default_value?contains(param[0])>
                                tt += '<input type="checkbox" checked name="div-'+index+'-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">';
                            </#if>
                            <#if !obj.default_value?contains(param[0])>
                                tt += '<input type="checkbox" name="div-'+index+'-${obj.field_name}" value="${param[0]}" lay-skin="primary" title="${param[1]}" lay-verify="${obj.verify_rule?replace(',','|')}">';
                            </#if>
                        </#list>
                    </#if>
                    tt += '</div>';
                    tt += '<input type="hidden" name="child_${obj.field_name}" id="${obj.field_name}_'+index+'" value="${obj.default_value}" />';
                    tt += '</td>';
                </#if>
                <#if obj.show_type == '8'>
                    tt += '<td>';
                    tt += '<div style="margin-top:5px;">';
                    tt += '<input type="hidden" name="child_${obj.field_name}" id="fileIds_${obj.field_name}_'+index+'" value="" class="layui-input">';
                    tt += '<button type="button" class="layui-btn layui-btn-xs" id="upload_file_${obj.field_name}_'+index+'">选择附件</button>';
                    tt += '<table class="layui-table">';
                    tt += '<thead>';
                    tt += '<tr><th>附件名称</th><th>操作</th></tr>';
                    tt += '</thead>';
                    tt += '<tbody id="tbody_file_${obj.field_name}_'+index+'">';
                    tt += '</tbody>';
                    tt += '</table>';
                    tt += '</div>';
                    tt += '</td>';
                </#if>
                <#if obj.show_type == '0'>
                    tt += '<input type="hidden" name="child_${obj.field_name}" value="${obj.default_value}" lay-verify="${obj.verify_rule?replace(',','|')}" autocomplete="off" placeholder="${obj.field_comment}" class="layui-input">';
                </#if>
            </#if>
        </#list>
        tt += '<td style="width: 78px;"><input type="hidden" name="c_ids" value=""><div class="layui-btn-group"><button type="button" onclick="addLine();" class="layui-btn layui-btn-xs"><i class="layui-icon"></i></button><button type="button" onclick="delLine(\''+index+'\');" class="layui-btn layui-btn-xs layui-btn-danger"><i class="layui-icon"></i></button></div></td>';
        tt += '</tr>';
        ${'$'}("#child_table tr:last").before(tt);

        //初始化
        <#list linkFieldList as obj>
            <#if obj.field_operat == 'Y'>
                <#if obj.show_type == '3'>
                    <#if !obj.option_content?contains(";")>
                        findSelectDictionary('${obj.option_content}','${obj.field_name}_'+index,'${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '4'>
                    <#if !obj.option_content?contains(";")>
                        findRadioDictionary('${obj.option_content}','div_'+index+'_','${obj.field_name}','${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '5'>
                    <#assign checkboxText = 'true'>
                    <#if !obj.option_content?contains(";")>
                        findCheckboxDictionary('${obj.option_content}','div-'+index+'-','${obj.field_name}','${obj.default_value}');
                    </#if>
                </#if>
                <#if obj.show_type == '7'>
                    initDateType("${obj.field_name}_"+index,true);
                </#if>
                <#if obj.show_type == '6'>
                </#if>
                <#if obj.show_type == '8'>
                    uploadMoreFile('${obj.field_name}_'+index,'${obj.field_name}');
                </#if>
            </#if>
        </#list>
        form.render();
    }


    function delLine(index){
        if(index.indexOf('ls')!=-1){
            var id = index.substring(2);
            //删库
        ${'$'}.ajax({
                type: "GET",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "${'$'}{pageContext.request.contextPath}/${tablePd.mod_name}/${tablePd.bus_name}/delChild?ids="+id,//url
                data: "",
                success: function (res) {
                    if (res.success) {
                        ${'$'}("#child_tr_"+index).remove();
                        layer.msg("数据删除成功。", {time: 2000});
                    }else{
                        if(res.loseSession=='loseSession'){
                            loseSession(res.msg,res.url)
                        }else{
                            layer.msg(res.msg, {time: 2000});
                        }
                    }

                },
                error : function() {
                    layer.msg("数据删除失败。", {time: 2000});
                }
            });
        }else{
            ${'$'}("#child_tr_"+index).remove();
        }
    }
    </#if>


</script>

<%@include file="../../system/admin/bottom.jsp"%>