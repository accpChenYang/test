<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	pageContext.setAttribute("APP_PATH",request.getContextPath());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.css" rel="stylesheet" > 
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap-table.min.css" rel="stylesheet" > 
<script type="text/javascript" src="${APP_PATH }/static/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${APP_PATH }/static/js/bootstrap-table.min.js"></script>
<script type="text/javascript" src="${APP_PATH }/static/js/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript">
	var total,pagenum;
	$(function(){
		tabInit();
		//新增按钮
		$("#add_emp_btn").click(function(){
			form_reset("#add_emp_form");//清空表单内容
			dept("#select_add_Dept");
			$('#empAddModal').modal({
				backdrop:'static'
			});
		});
		//判断用户名是否可用
		$("#emp_empName").change(function(){
			$.ajax({
				type:"post",
				url:"${APP_PATH}/chckName",
				data:"empName="+$(this).val(),
				success:function(result){
					if(result.code==100){
						validata_form_msg("#emp_empName","success",result.extend.validata_msg);
						$("#add_emp_save_btn").attr("ajax-validata","success");
					}
					else{
						validata_form_msg("#emp_empName" ,"error",result.extend.validata_msg);
						$("#add_emp_save_btn").attr("ajax-validata","error");
					}
				}
			});
		});
		$("#add_emp_save_btn").click(function(){
			var array=new Array();
			array[0]="#emp_empName";
			array[1]="#emp_email";
			if(!validata_form(array)){
				return;
			}
			if($("#add_emp_save_btn").attr("ajax-validata")=="error"){
				return false;
			}
			var param=$("#add_emp_form").serialize();
			$.ajax({
				type:"post",
				url:'${APP_PATH }/emp',
				data:param,
				success:function(result){
					//alert(result.message);
					if(result.code==100){
						$("#empAddModal").modal("hide");
						$("#table").bootstrapTable('refresh', {query:{pageno:total} });//ajax请求第几页的数据
					}else{//失败
						var $message=result.extend.message;
						if(undefined!=$message.email){
							validata_form_msg("#emp_email","error",$message.email);
						}
						if(undefined!=$message.empName){
							validata_form_msg("#emp_empName","error",$message.empName);
						}
					}
					
				}
			});
		});
	});
	//清空表单内容
	function form_reset(ele){
		$(ele)[0].reset();//清空表单内容
		$(ele).find("*").removeClass("has-error has-success");//样式
		$(ele).find("span.help-block").text("");//提示信息
	}
	//添加员工 form 提示信息
	function validata_form_msg(ele,status,msg){
		$(ele).parent().removeClass("has-error has-success");
		$(ele).next("span").text("");
		if("success"==status){
			$(ele).next("span").text(msg);
			$(ele).parent().addClass("has-success");
		}else{
			$(ele).parent().addClass("has-error");
			$(ele).next("span").text(msg);
		}
	}
	//部门信息
	function dept(ele){
		$(ele).empty();//下拉列表
		$.ajax({
			type:"get",
			url:"${APP_PATH}/depts",
			success:function(result){
				var $list=result.extend.depts;
				$.each($list,function(index,item){
					var option=$("<option></option>").append(item.deptName).attr("value",item.deptId);
					$(ele).append(option);
				});
			}
		});
	}
	function check_email(ele){
		var email=$(ele).val();
		var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
		if(!regEmail.test(email)){
			validata_form_msg(ele,"error","邮箱格式不正确");
			//alert("邮箱格式不正确");
			return false;
		}else{
			validata_form_msg(ele,"success","");
		} 
		return true;
	}
	
	function check_empName(ele){
		var empName=$(ele).val();
		var empNamereg=/(^[a-zA-Z0-9_-]{6,16})|(^[\u2E80-\u9FFF]{2,5})$/;
		if(!empNamereg.test(empName)){
			validata_form_msg(ele,"error","用户名必须是2-5位中文或者6-16位英文和数字的组合");
			//alert("用户名可以是2-5位中文或者6-16位英文和数字的组合");
			return false;
		}else{
			validata_form_msg(ele,"success","");
		}
		return true;
	}
	//校验添加员工表单数据是否合法
	function validata_form(ele){
		alert("1");
		var empname=check_empName(ele[0]);
		var email=check_email(ele[1]);
		if(!empname){
			return false;
		}
		if(!email){
			return false;
		}
		return true;
	}
	//分页信息
	function pageDesc(res){
		//页码信息 start
		$("#desc").empty();
    	$("#desc").append("当前页码：<span class='label label-default'>"+res.extend.page.pageNum+"</span> "+
    					"总页数:<span class='label label-default'>"+res.extend.page.pages+"</span> "+
    				"总记录数：<span class='label label-default'>"+res.extend.page.total+"</span>");
    	total=res.extend.page.total;
    	pagenum=res.extend.page.pageNum;
    	////页码信息 ends
    	//分页条信息 start
    	$("#page").empty();//清空page元素内容
    	var ul=$("<ul></ul>").addClass("pagination");
    	var sli=$("<li></li>").append($("<a></a>").append("首页").attr("href","#"));//首页li
    	var prevli=$("<li></li>").append($("<a></a>").append($("<span></span>").append("&laquo;").attr("aria-hidden","true")).attr("href","#"));//上一页li
    	if(!res.extend.page.hasPreviousPage){//如果是首页禁用上一页和首页按钮
    		prevli.addClass("disabled");
    		sli.addClass("disabled");
    	}else{
	    	//首页点击事件
	    	sli.click(function(){
	    		$("#table").bootstrapTable('refresh', {query:{pageno:1} });//ajax请求第几页的数据
	    	});
	    	//上一页事件
	    	prevli.click(function(){
	    		$("#table").bootstrapTable('refresh', {query:{pageno:res.extend.page.prePage} });//ajax请求第几页的数据
	    	});
    	}
    	var nextli=$("<li></li>").append($("<a></a>").append($("<span></span>").append("&raquo;").attr("aria-hidden","true")).attr("href","#"));//下一页li
    	var moli=$("<li></li>").append($("<a></a>").append("末页").attr("href","#"));//末页li
    	if(!res.extend.page.hasNextPage){//如果是首页禁用上一页和首页按钮
    		nextli.addClass("disabled");
    		moli.addClass("disabled");
    	}else{
    		//末页点击事件
        	moli.click(function(){
        		$("#table").bootstrapTable('refresh', {query:{pageno:res.extend.page.pages} });//ajax请求第几页的数据
        	});
        	//下一页事件
        	nextli.click(function(){
        		$("#table").bootstrapTable('refresh', {query:{pageno:res.extend.page.nextPage} });//ajax请求第几页的数据
        	});
    	}
    	ul.append(sli).append(prevli);
    	//遍历显示的页码数
    	$.each(res.extend.page.navigatepageNums,function(index,item){
    		var num=$("<li></li>").append($("<a></a>").append(item).attr("href","#"));//页码li
			if(res.extend.page.pageNum==item){//是否是当前页 true：选中
				num.addClass("active");//页码li
    		}
    		num.click(function(){//给每一个页码创建一个点击事件
    			$("#table").bootstrapTable('refresh', {query:{pageno:item} });//ajax请求第几页的数据
    		});
    		ul.append(num);
    	});
    	ul.append(nextli).append(moli);
    	$("#page").append(ul);
    	//分页条信息 end
	}
	
	//表格初始化
	function tabInit(){
		$('#table').bootstrapTable({
		    url: '${APP_PATH }/emp',
		    striped:true,
		    showToggle:true,
		    clickToSelect:true,
		    responseHandler:function(res){//数据从服务器端返回执行
		    	pageDesc(res);//分页相关的信息
		    
		    	//返回表格中显示的数据
		    	return res.extend.page.list;
		    },
		    columns: [{
		    	checkbox:true,
		        field: 'che'
		    }
		    , {
		        field: 'empName',
		        title: 'EmpName',
		        align:"center"
		    }, {
		        field: 'gender',
		        title: 'Gender',
		        formatter:function(data,row,index){
		        	return data==1?"男":"女";
		        },
		        align:"center"
		    }, 
		    {
		        field: 'email',
		        title: 'Email',
		        align:"center"
		    },
		   {
		        field: 'dept',
		        title: 'DepartName',
		        align:"center",
		        formatter:function(data,row,index){
		        	return row.dept.deptName;
		        }
		    },
		    {
		        field: 'xx',
		        title: '操作',
		        align:"center",
		        formatter:function(data,row,index){
		        	return '<button type="button" edit-id="'+row.empId+'" class="btn btn-primary btn-sm edit-btn" aria-label="Left Align"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑</button>&nbsp;'+
		        	'<button class="btn btn-danger btn-sm del-btn"  del-id="'+row.empId+'" type="button"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除</button>';
		        }
		    }]
		});
	}
	
	$(function(){
		//编辑
		$(document).on("click","button.edit-btn",function(){
			dept("#select_update_Dept");
			edit($(this).attr("edit-id"));
			$("#update_emp_save_btn").attr("update-id",$(this).attr("edit-id"));
			$('#empUpdateModal').modal({
				backdrop:'static'
			});
		});
		//修改保存
		$("#update_emp_save_btn").click(function(){
			if(!check_email("#emp_update_email")){
				return;
			}
			$.ajax({
				type:"put",
				url:"${APP_PATH}/emp/"+$(this).attr("update-id"),
				data:$("#update_emp_form").serialize(),
				success:function(result){
					$("#empUpdateModal").modal("hide");
					$("#table").bootstrapTable('refresh', {query:{pageno:pagenum} });//ajax请求第几页的数据
				}
			});
			
		});
		
		//单元格-删除
		$(document).on("click","button.del-btn",function(){
			var empname=$(this).parents("tr").find("td:eq(1)").text();
			if(confirm("确定要删除 【"+empname+"】 吗?")){
				$.ajax({
					type:"delete",
					url:"${APP_PATH}/emp/"+$(this).attr("del-id"),
					success:function(result){
						$("#table").bootstrapTable('refresh', {query:{pageno:pagenum} });//ajax请求第几页的数据
					}
				});
			}
		});
		//删除
		$("#btn-all-del").click(function(){
			var delRow=$("#table").bootstrapTable('getSelections');
			if(delRow.length>0){
				if(confirm("确定要删除吗?")){
					var s=new Array();
					$.each(delRow,function(index,item){
						//console.log(item.empName);
						s.push(item.empId);
					});
					$.ajax({
						type:"delete",
						url:"${APP_PATH}/emp/"+s,
						success:function(result){
							$("#table").bootstrapTable('refresh', {query:{pageno:pagenum} });//ajax请求第几页的数据
						}
					});
				}
			}
		});
	});
	//点击编辑按钮，回显要修改的数据
	function edit(id){
		$.ajax({
			type:"get",
			url:"${APP_PATH}/emp/"+id,
			success:function(result){
				var r=result.extend.emp;
				$("#empName").text(r.empName);
				$("#emp_update_email").val(r.email);
				$("#emp_update_gender").val([r.gender]);
				$("#select_update_Dept").val([r.dId]);
			}
		});
	}
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<h1>SSM-CRUD</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4 col-md-offset-4">
				<button type="button" class="btn btn-primary btn-sm" id="add_emp_btn">新增</button>
				<button type="button" class="btn btn-danger btn-sm" id="btn-all-del">删除</button>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="table"></table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6" id="desc"></div>
			<div class="col-md-6">
				<nav aria-label="Page navigation" id="page">
				  <%-- <ul class="pagination">
				  	 <li><a href="${APP_PATH }/emp?pageno=1">首页</a></li>
				  	 <c:if test="${page.hasPreviousPage }">
				  	 	<li>
					      <a href="${APP_PATH }/emp?pageno=${page.prePage }" aria-label="Previous">
					        <span aria-hidden="true">&laquo;</span>
					      </a>
					    </li>
				  	 </c:if>
				    <c:forEach items="${page.navigatepageNums }" var="num">
				    	<c:if test="${page.pageNum==num }" var="active">
				    		<li class="active"><a href="#">${num } <span class="sr-only">(current)</span></a></li>
				    	</c:if>
				    	<c:if test="${not active }">
				    		<li><a href="${APP_PATH }/emp?pageno=${num }">${num }</a></li>
				    	</c:if>
				    </c:forEach>
				    <c:if test="${page.hasNextPage }">
				  	 	<li>
					      <a href="${APP_PATH }/emp?pageno=${page.nextPage }" aria-label="Next">
					        <span aria-hidden="true">&raquo;</span>
					      </a>
					    </li>
				  	 </c:if>
				     <li><a href="${APP_PATH }/emp?pageno=${page.pages}">末页</a></li>
				  </ul> --%>
				</nav>
			</div>
		</div>
	</div>
	<!-- 新增 -->
	<div class="modal fade" id="empAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">新增员工</h4>
	      </div>
	      <div class="modal-body">
	        <form class="form-horizontal" id="add_emp_form">
			  <div class="form-group">
			    <label  class="col-sm-2 control-label">EmpName</label>
			    <div class="col-sm-8">
			      <input type="text" name="empName" id="emp_empName" class="form-control" placeholder="EmpName">
			      <span class="help-block"></span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Email</label>
			    <div class="col-sm-8">
			      <input type="text" class="form-control" id="emp_email" name="email"  placeholder="xxxx@xx.com">
			       <span class="help-block"></span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Gender</label>
			    <div class="col-sm-8">
			      	<label class="radio-inline">
					  <input type="radio" name="gender" checked="checked"  id="emp_gender" value="1"> 男
					</label>
					<label class="radio-inline">
					  <input type="radio" name="gender" id="emp_gender" value="0"> 女
					</label>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">DeptName</label>
			    <div class="col-sm-4">
			      	<select class="form-control" name="dId" id="select_add_Dept">
					  <option value="-1">请选择</option>
					</select>
			    </div>
			  </div>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" id="add_emp_save_btn">保存</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	<!-- 修改 -->
	<div class="modal fade" id="empUpdateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">修改员工信息</h4>
	      </div>
	      <div class="modal-body">
	        <form class="form-horizontal" id="update_emp_form">
			  <div class="form-group">
			    <label  class="col-sm-2 control-label">EmpName</label>
			    <div class="col-sm-8">
			      	<p class="form-control-static" id="empName"></p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Email</label>
			    <div class="col-sm-8">
			      <input type="text" class="form-control" id="emp_update_email" name="email"  placeholder="xxxx@xx.com">
			       <span class="help-block"></span>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Gender</label>
			    <div class="col-sm-8">
			      	<label class="radio-inline">
					  <input type="radio" name="gender" checked="checked"  id="emp_update_gender" value="1"> 男
					</label>
					<label class="radio-inline">
					  <input type="radio" name="gender" id="emp_update_gender" value="0"> 女
					</label>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">DeptName</label>
			    <div class="col-sm-4">
			      	<select class="form-control" name="dId" id="select_update_Dept">
					</select>
			    </div>
			  </div>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" id="update_emp_save_btn">保存</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>