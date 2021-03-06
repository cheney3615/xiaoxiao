<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>

<script>
//全选全不选
function checkBox(id,check)
{
	$("input[name='"+id+"']").attr("checked",check);
}

//删除品牌（多选情况下）
function optDelete()
{
	var size =  $("input[name='ids']:checked").size();
	if(size<1)
	{
		alert('请选择项目');	
		return;	
	}
	
	if(!confirm("你确定删除吗？"))
	{
		return;	
	}
	
	//提交表单
	$("#brandForm")[0].action= "doDelete.do";
	$("#brandForm").submit();
	
}
</script>

</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 品牌管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加" onclick="javascript:window.location.href='add.jsp'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="list.do" method="get" style="padding-top:5px;">
品牌名称: <input type="text" name="name" value="${name}"/>
	<select name="isDisplay">
		<option value="1">是</option>
		<option value="0">否</option>
	</select>
	
	<script>
		$("select[name='isDisplay']").val('${isDisplay}');	
	</script>
	
	<input type="submit" class="query" value="查询"/>
</form>
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
<form id="brandForm">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="checkBox('ids',this.checked)"/></th>
			<th>品牌ID</th>
			<th>品牌名称</th>
			<th>品牌图片</th>
			<th>品牌描述</th>
			<th>排序</th>
			<th>是否可用</th>
			<th>操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
	
	<c:forEach items="${pageBrands.result}" var="brand">
	
		<tr bgcolor="#ffffff" onmouseout="this.bgColor='#ffffff'" onmouseover="this.bgColor='#eeeeee'">
			<td><input type="checkbox" value="${brand.id}" name="ids"/></td>
			<td align="center">${brand.id}</td>
			<td align="center">${brand.name}</td>
			<td align="center"><img width="40" height="40" src="${brand.imgUrl}"/></td>
			<td align="center">
			<c:out value="${brand.description}"></c:out>
			
			</td>
			<td align="center">${brand.sort}</td>
			<td align="center">${brand.isDisplay}</td>
			<td align="center">
			<a class="pn-opt" href="showEdit.do?brandId=${brand.id}">修改</a> | <a class="pn-opt" onclick="if(!confirm('您确定删除吗？')) {return false;}" href="#">删除</a>
			</td>
		</tr>
	
	</c:forEach>
	
	</tbody>

</form>

</table>
<div class="page pb15">
	<span class="r inb_a page_b">
	
		<font size="2">
		<a href="list.do?pageNum=1&name=${name}&isDisplay=${isDisplay}">
			首页
		</a>
		</font>
	
		<font size="2">
		
		<c:if test="${pageBrands.pageNum>1}">
			<a href="list.do?pageNum=${pageBrands.pageNum-1}&name=${name}&isDisplay=${isDisplay}">
			上一页
			</a>
		</c:if>
		
		<c:if test="${pageBrands.pageNum<=1}">
			
			上一页
		
		</c:if>
		
		</font>
		
		<c:forEach begin="1" end="${pageBrands.pages}" var="pNum">
		
		<c:if test="${pNum==pageBrands.pageNum}">
	
		<strong>${pNum}</strong>
		</c:if>
	
		<c:if test="${pNum!=pageBrands.pageNum}">
		<a href="list.do?pageNum=${pNum}&name=${name}&isDisplay=${isDisplay}">
		${pNum}
		</a>
		</c:if>
		
		</c:forEach>
	
		<font size="2">
		
		<c:if test="${pageBrands.pageNum<pageBrands.pages}">
			<a href="list.do?pageNum=${pageBrands.pageNum+1}&name=${name}&isDisplay=${isDisplay}">
			下一页
			</a>
		</c:if>
		
		<c:if test="${pageBrands.pageNum>=pageBrands.pages}">
			
			下一页
		
		</c:if>
		
		</font>
	
		<font size="2">
		<a href="list.do?pageNum=${pageBrands.pages}&name=${name}&isDisplay=${isDisplay}">
		尾页
		</a>
		</font>
	
		共<var>${pageBrands.pages}</var>页 到第<input type="text" size="3" id="PAGENO"/>页 <input type="button" onclick="javascript:window.location.href = '/product/list.do?&amp;isShow=0&amp;pageNo=' + $('#PAGENO').val() " value="确定" class="hand btn60x20" id="skip"/>
	
	</span>
</div>
<div style="margin-top:15px;"><input class="del-button" type="button" value="删除" onclick="optDelete();"/></div>
</div>
</body>
</html>