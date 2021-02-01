<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<#assign statusType = ''>
<#assign isContainStatus = 'false'>
<#assign isContainType = 'false'>
<#list fieldList as obj>
	<#if obj.field_name == 'status' && (tablePd.status_type == '0' || tablePd.status_type == '1')>
		<#assign statusType = ' a.status+0 asc,'>
		<#assign isContainStatus = 'true'>
	</#if>
	<#if obj.field_name == 'type'>
		<#assign isContainType = 'true'>
	</#if>
</#list>
<mapper namespace="${tablePd.pack_path}.${tablePd.mod_name}.dao.${tablePd.bus_name?cap_first}Dao">
	<#assign ownOrderBy = 'false'>
	<#assign ownChildOrderBy = 'false'>
	<!--数据权限-->
	<sql id="authPageSql">
		<!-- 数据权限 -->
		<if test="(pd.auth_organize_ids == null or pd.auth_organize_ids.size == 0) and (pd.auth_user != null and pd.auth_user != '')">
			and a.create_user=${'#'}{pd.auth_user}
		</if>
		<if test="pd.auth_organize_ids != null and pd.auth_organize_ids.size > 0">
			and (a.create_user=${'#'}{pd.auth_user} or a.create_organize in
			<foreach collection="pd.auth_organize_ids" item="organize_id" open="(" separator="," close=")">
			${'#'}{organize_id}
			</foreach>
			)
		</if>
	</sql>
	<sql id="authPdSql">
		<!-- 数据权限 -->
		<if test="(auth_organize_ids == null or auth_organize_ids.size == 0) and (auth_user != null and auth_user != '')">
			and a.create_user=${'#'}{auth_user}
		</if>
		<if test="auth_organize_ids != null and auth_organize_ids.size > 0">
			and (a.create_user=${'#'}{auth_user} or a.create_organize in
			<foreach collection="auth_organize_ids" item="organize_id" open="(" separator="," close=")">
			${'#'}{organize_id}
			</foreach>
			)
		</if>
	</sql>


	<!--查询分页列表-->
	<select id="findListPage" parameterType="com.qingfeng.util.Page" resultType="com.qingfeng.util.PageData">
		select
		<#if tablePd.temp_type == '1'>
            a.${tablePd.tree_pid} as "${tablePd.tree_pid}",
            ifnull(b.num,0) as "child_num",
		</#if>
		<#list fieldList as obj>
		<#if obj_has_next>
			a.${obj.field_name} as "${obj.field_name}",
		</#if>
		<#if !obj_has_next>
			a.${obj.field_name} as "${obj.field_name}"
		</#if>
		</#list>
			from ${tablePd.table_name} a
	<#if tablePd.temp_type == '1'>
        left join (
        select
        count(*) as "num",
        a.${tablePd.tree_pid} as "${tablePd.tree_pid}"
        from ${tablePd.table_name} a
        group by a.${tablePd.tree_pid}
        ) b on a.id=b.${tablePd.tree_pid}
	</#if>
			where 1=1
		<#if isContainStatus == 'true'>
			<if test="pd.status != null and pd.status != ''">
				and a.status = ${'#'}{pd.status}
			</if>
		</#if>
		<#if isContainType == 'true'>
			<if test="pd.type != null and pd.type != ''">
				and a.type = ${'#'}{pd.type}
			</if>
		</#if>
		<#list fieldList as obj>
			<#if obj.field_query == 'Y'>
			<if test="pd.${obj.field_name} != null and pd.${obj.field_name} != ''">
				<#if obj.query_type == '=' ||obj.query_type == '>' ||obj.query_type == '>='||obj.query_type == '!='>
				and a.${obj.field_name} ${obj.query_type} ${'#'}{pd.${obj.field_name}}
				</#if>
				<#if obj.query_type == '<' ||obj.query_type == '<='>
					and a.${obj.field_name} <![CDATA[ ${obj.query_type} ]]> ${'#'}{pd.${obj.field_name}}
				</#if>
				<#if obj.query_type == 'like'>
					and a.${obj.field_name} ${obj.query_type} concat('%',concat(${'#'}{pd.${obj.field_name}},'%'))
				</#if>
				<#if obj.query_type == 'is null'||obj.query_type == 'is not null'>
					and a.${obj.field_name} ${obj.query_type}
				</#if>
				<#if obj.query_type == 'time_period'>
					<if test="pd.start_time != null and pd.start_time != ''">
						and a.${obj.field_name} >= ${'#'}{pd.start_time}
					</if>
					<if test="pd.end_time != null and pd.end_time != ''">
						and a.${obj.field_name} <![CDATA[ <= ]]> ${'#'}{pd.end_time}
					</if>
				</#if>
			</if>
			</#if>
			<#if obj.field_query == 'order_by'>
				<#assign ownOrderBy = 'true'>
			</#if>
		</#list>
		<#if tablePd.temp_type == '1'>
            <if test="pd.${tablePd.tree_pid} != null and pd.${tablePd.tree_pid} != ''">
                and a.${tablePd.tree_pid} = ${'#'}{pd.${tablePd.tree_pid}}
            </if>
		</#if>
			<include refid="authPageSql"></include>
		<#if ownOrderBy == 'true'>
			order by ${statusType} a.order_by+0 asc , a.create_time desc
		</#if>
		<#if ownOrderBy == 'false'>
			order by ${statusType} a.create_time desc
		</#if>
	</select>

    <!--查询分页数量-->
    <select id="findListSize" parameterType="com.qingfeng.util.Page" resultType="Integer">
        select
        count(*)
        from ${tablePd.table_name} a
        where 1=1
	<#if isContainStatus == 'true'>
        <if test="pd.status != null and pd.status != ''">
            and a.status = ${'#'}{pd.status}
        </if>
	</#if>
	<#if isContainType == 'true'>
        <if test="pd.type != null and pd.type != ''">
            and a.type = ${'#'}{pd.type}
        </if>
	</#if>
	<#list fieldList as obj>
		<#if obj.field_query == 'Y'>
		<if test="pd.${obj.field_name} != null and pd.${obj.field_name} != ''">
		<#if obj.query_type == '=' ||obj.query_type == '>' ||obj.query_type == '>='||obj.query_type == '!='>
			and a.${obj.field_name} ${obj.query_type} ${'#'}{pd.${obj.field_name}}
		</#if>
		<#if obj.query_type == '<' ||obj.query_type == '<='>
			and a.${obj.field_name} <![CDATA[ ${obj.query_type} ]]> ${'#'}{pd.${obj.field_name}}
		</#if>
		<#if obj.query_type == 'like'>
			and a.${obj.field_name} ${obj.query_type} concat('%',concat(${'#'}{pd.${obj.field_name}},'%'))
		</#if>
		<#if obj.query_type == 'is null'||obj.query_type == 'is not null'>
			and a.${obj.field_name} ${obj.query_type}
		</#if>
		</if>
		</#if>
	</#list>
	<#if tablePd.temp_type == '1'>
        <if test="pd.${tablePd.tree_pid} != null and pd.${tablePd.tree_pid} != ''">
            and a.${tablePd.tree_pid} = ${'#'}{pd.${tablePd.tree_pid}}
        </if>
	</#if>
        <include refid="authPageSql"></include>
    </select>

    <!--查询列表-->
    <select id="findList" parameterType="com.qingfeng.util.PageData" resultType="com.qingfeng.util.PageData">
        select
		<#if tablePd.temp_type == '1'>
			a.${tablePd.tree_pid} as "${tablePd.tree_pid}",
            ifnull(b.num,0) as "child_num",
		</#if>
		<#list fieldList as obj>
		<#if obj_has_next>
			a.${obj.field_name} as "${obj.field_name}",
		</#if>
		<#if !obj_has_next>
			a.${obj.field_name} as "${obj.field_name}"
		</#if>
		</#list>
			from ${tablePd.table_name} a
<#if tablePd.temp_type == '1'>
			left join (
				select
				count(*) as "num",
				a.${tablePd.tree_pid} as "${tablePd.tree_pid}"
				from ${tablePd.table_name} a
				group by a.${tablePd.tree_pid}
			) b on a.id=b.${tablePd.tree_pid}
</#if>
			where 1=1
		<#if isContainStatus == 'true'>
			<if test="status != null and status != ''">
				and a.status = ${'#'}{status}
			</if>
		</#if>
		<#if isContainType == 'true'>
			<if test="type != null and type != ''">
				and a.type = ${'#'}{type}
			</if>
		</#if>
		<#list fieldList as obj>
			<#if obj.field_query == 'Y'>
			<if test="${obj.field_name} != null and ${obj.field_name} != ''">
			<#if obj.query_type == '=' ||obj.query_type == '>' ||obj.query_type == '>='||obj.query_type == '!='>
				and a.${obj.field_name} ${obj.query_type} ${'#'}{${obj.field_name}}
			</#if>
			<#if obj.query_type == '<' ||obj.query_type == '<='>
				and a.${obj.field_name} <![CDATA[ ${obj.query_type} ]]> ${'#'}{${obj.field_name}}
			</#if>
			<#if obj.query_type == 'like'>
				and a.${obj.field_name} ${obj.query_type} concat('%',concat(${'#'}{${obj.field_name}},'%'))
			</#if>
			<#if obj.query_type == 'is null'||obj.query_type == 'is not null'>
				and a.${obj.field_name} ${obj.query_type}
			</#if>
			</if>
			</#if>
			<#if obj.field_query == 'order_by'>
				<#assign ownOrderBy = 'true'>
			</#if>
		</#list>
		<#if tablePd.temp_type == '1'>
			<if test="${tablePd.tree_pid} != null and ${tablePd.tree_pid} != ''">
				and a.${tablePd.tree_pid} = ${'#'}{${tablePd.tree_pid}}
			</if>
		</#if>
			<include refid="authPdSql"></include>
		<#if ownOrderBy == 'true'>
			order by ${statusType} a.order_by+0 asc , a.create_time desc
		</#if>
		<#if ownOrderBy == 'false'>
			order by ${statusType} a.create_time desc
		</#if>
    </select>
	
	<!--查询详情-->
	<select id="findInfo" parameterType="com.qingfeng.util.PageData" resultType="com.qingfeng.util.PageData">
		select
		<#if tablePd.temp_type == '1'>
			a.${tablePd.tree_pid} as "${tablePd.tree_pid}",
		</#if>
		<#if tablePd.temp_type == '1'>
			b.${tablePd.tree_name} as "parent_name",
		</#if>
		<#list fieldList as obj>
		<#if obj_has_next>
			a.${obj.field_name} as "${obj.field_name}",
		</#if>
		<#if !obj_has_next>
			a.${obj.field_name} as "${obj.field_name}"
		</#if>
		</#list>
			from ${tablePd.table_name} a
		<#if tablePd.temp_type == '1'>
			left join ${tablePd.table_name} b on a.${tablePd.tree_pid}=b.id
		</#if>
			where a.id=${'#'}{id}
	</select>
	
	<!--保存-->
	<insert id="save" parameterType="com.qingfeng.util.PageData">
		insert into ${tablePd.table_name} (
		<#if tablePd.temp_type == '1'>
			${tablePd.tree_pid},
		</#if>
	<#list fieldList as obj>
		<#if obj_has_next>
			${obj.field_name},
		</#if>
		<#if !obj_has_next>
			${obj.field_name}
		</#if>
	</#list>
		) values (
		<#if tablePd.temp_type == '1'>
			${'#'}{${tablePd.tree_pid},jdbcType=VARCHAR},
		</#if>
	<#list fieldList as obj>
		<#if obj_has_next>
			${'#'}{${obj.field_name},jdbcType=VARCHAR},
		</#if>
		<#if !obj_has_next>
			${'#'}{${obj.field_name},jdbcType=VARCHAR}
		</#if>
	</#list>
		)
	</insert>
	
	<!--更新-->
	<update id="update" parameterType="com.qingfeng.util.PageData">
		update ${tablePd.table_name} set
		<#list fieldList as obj>
		<#if obj.field_name != 'update_time'&&obj.field_name != 'create_user'&&obj.field_name != 'create_organize'&&obj.field_name != 'create_time'>
			<if test="${obj.field_name} != null">${obj.field_name}=${'#'}{${obj.field_name}},</if>
		</#if>
		</#list>
			update_time=${'#'}{update_time}
			where id=${'#'}{id}
	</update>
	
    <!--删除-->
    <delete id="del" parameterType="String">
        delete from ${tablePd.table_name} where id in
        <foreach collection="array" item="id" open="(" separator="," close=")">
			${'#'}{id}
        </foreach>
    </delete>

	<!--更新状态-->
	<update id="updateStatus" parameterType="com.qingfeng.util.PageData">
		update ${tablePd.table_name} set
			<if test="status != null">status=${'#'}{status},</if>
			<if test="update_user != null">update_user=${'#'}{update_user},</if>
			update_time=${'#'}{update_time}
			where 1=1
			<if test="id != null and id != ''">
				and id=${'#'}{id}
			</if>
	</update>


<#if tablePd.temp_type == '1'>
    <!--查询是否存在子节点-->
	<select id="findContainChildList" parameterType="com.qingfeng.util.PageData" resultType="com.qingfeng.util.PageData">
		select
			a.id as "id",
			a.name as "name",
			b.num as "num"
			from ${tablePd.table_name} a
			left join (
				select
				count(*) as "num",
				a.${tablePd.tree_pid} as "parent_id"
				from
				${tablePd.table_name} a
				group by a.${tablePd.tree_pid}
			) b on a.id=b.parent_id
			where a.id in
			<foreach collection="idList" item="id" open="(" separator="," close=")">
				${'#'}{id}
			</foreach>
	</select>
</#if>
</mapper>