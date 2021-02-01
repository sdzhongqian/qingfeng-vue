<!DOCTYPE html>
<html>
<head></head>
<body>
<#list list as p>
<div id="print_content" style="margin:40px;">
    <p style="text-align:center;margin-bottom: 20px;">
        <strong><span style="font-family: 宋体;font-size: 35px"><span style="font-family:宋体">催费函</span></span></strong>
    </p>
    <p style="font-family: 宋体;text-indent:50px;font-size: 24px">
        尊敬的<span style="text-decoration:underline;">&nbsp;${p.name!""}&nbsp;</span>业主，您好！
        截止<span style="text-decoration:underline;">&nbsp;${p.nowDate_year!""}&nbsp;</span>年<span style="text-decoration:underline;">&nbsp;${p.nowDate_month!""}&nbsp;</span> 月<span style="text-decoration:underline;">&nbsp;${p.nowDate_day!""}&nbsp;</span>日，
        您已欠交<span style="text-decoration:underline;">&nbsp;${p.start_month_year!""}&nbsp;</span> 年<span style="text-decoration:underline;">&nbsp;${p.start_month_month!""}&nbsp;</span>月至<span style="text-decoration:underline;">&nbsp;${p.end_month_year!""}&nbsp;</span>年<span style="text-decoration:underline;">&nbsp;${p.end_month_month!""}&nbsp;</span>月共<span style="text-decoration:underline;">${p.month_num}</span>月物业服务费用共<span style="text-decoration:underline;">&nbsp;${p.wyFree}&nbsp;</span>元。
    </p>
    <p style="font-family: 宋体;text-indent:50px;font-size: 24px">
        上述欠款已超出缴纳期限，根据《物业管理服务协议》，逾期缴纳产生逾期违约金计：￥<span style="text-decoration:underline;">${p.znjFree}</span>元（每日 ${p.znj_ratio}‰），共￥<span style="text-decoration:underline;">${p.wyjFree}</span>元。  为维护物业管理区域内全体业主共同利益，保证各项物业服务工作正常开展。请您收到通知<span style="text-decoration:underline;">7</span>日内缴纳物业服务费，如有疑问请致电物业管理处电话，再次感谢您对物业工作的理解与支持！
    </p>
</div>
</#list>
</body>