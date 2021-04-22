<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@page import="util.*"%>
<%@page import="net.jntoo.db.*"%>
<%@ taglib prefix="ssm" uri="http://ssm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header><meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
        <div class="main_header sticky-header">
            <div class="container-fluid">
                <div class="header_container">
                    <div class="row align-items-center">
    
                        <div class="col-lg-2">
    
                            <div class="logo">
                                <a href="./">智慧课堂教学系统</a>
                            </div>
                        
</div>
                        <div class="col-lg-8">
    
                            <!--main menu start-->
                            <div class="main_menu menu_position">
                                <nav>
                                    <ul id="navbar-str">
                                        <!-- 前端导航 -->
                                        <li>
<a href="./">首页
</a>
</li>
<li>
<a href="kechenglist.do">课程信息
<i class="fa fa-angle-down"></i>
</a>

<ul class="sub_menu">
                    <ssm:sql var="mapkechengleixing1" type="select">SELECT kechengleixing,kechengleixing FROM kechengleixing</ssm:sql>
<c:forEach items="${mapkechengleixing1}" var="m"><c:set var="m" value="${m}" scope="request" />
<li><a href="kechenglist.do?kechengleixing=${m.kechengleixing}">${m.kechengleixing}</a></li>
</c:forEach>
</ul>
</li>
<li>
<a href="gonggaolist.do">公告信息
</a>
</li>
<li>
<a href="suitangceshilist.do">随堂测试
</a>
</li>
<li>
<a href="xueshenglist.do">学生信息
</a>
</li>
<li>
<a href="login.do">后台管理
</a>
</li>
<li>
<a href="tiezilist.do">论坛
</a>
</li>                                        <!-- 前端导航结束 -->
                                    </ul>
                                </nav>
                            </div>
                            <!--main menu end-->
                        
</div>
                        <div class="col-lg-2">
    
                            <div class="header_account_area">
                                <div class="header_account-list user-info">
                                    <c:choose>
<c:when test="${sessionScope.username != null && sessionScope.username !=  '' }">

                                    <div class="dropdown">
    <a href="javascript:;" class="dropdown-toggle" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${sessionScope.username}
        <span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
        
                                        <li href="main.do"><a href="main.do">个人中心</a></li>
                                        <li href="logout.do" onclick="return confirm('你确定退出？')"><a href="logout.do" onclick="return confirm('你确定退出？')">退出</a></li>
                                    
    </ul>
    
</div>
                                    </c:when><c:otherwise>
                                    <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#loginModel">
    登录
</button>
                                    </c:otherwise></c:choose>
                                </div>
                                <div class="header_account-list search_bar">
                                    <a href="javascript:void(0)"><span class="icondianzi icondianzi-Search"></span></a>
                                    <div class="dropdown_search">
                                        <div class="search_close_btn">
                                            <a href="javascript:;"><i class="fa fa-close"></i></a>
                                        </div>
                                        <form action="kechenglist.do">
                                            <input type="text" value="${param.kechengmingcheng}" name="kechengmingcheng" placeholder="输入关键词搜索"/>
                                            <button type="submit"><span class="icondianzi icondianzi-Search"></span></button>
                                        </form>
                                    </div>
                                </div>
                                <div class="header_account-list mini_cart_wrapper" id="mini-cart-wrapper-patch">

                                    <!--mini cart end-->
                                </div>

                            </div>
                        
</div>
                    
</div>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="0" id="loginModel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title" id="0">登录</h4>
            </div>
            <div class="modal-body">
                <div class="pa10">
                    
            <form action="authLogin.do?ac=login&a=a" method="post"><!-- form 标签开始 -->
    
                <div class="form-group">
    
    
                    <input type="text" class="form-control" name="username" placeholder="输入帐号"/>
                
</div>
                <div class="form-group">
    
    
                    <input type="password" class="form-control" name="pwd" placeholder="密码"/>
                
</div>
                <div class="form-group">
    
    
                    <select class="form-control" name="cx">

                        <option value="学生">学生</option>  
  <option value="教师">教师</option>                    
</select>
                
</div>
                <div class="form-group">
    
    
                    <div class="input-group">
    
                        <input type="text" class="form-control" name="pagerandom" placeholder="输入验证码"/>
                        
                    
    <span class="input-group-addon"><img src="captcha.do" style="width: 60px;height: 20px;max-width: none; max-height: none;" onclick="this.src='captcha.do?rand='+new Date().getTime()"/></span>
</div>
                
</div>
                <div class="form-group">
    <div class="row">
        <label style="max-width: 120px;min-height:18px;text-align: right" class="col-sm-2 hiddex-xs"> </label>
        <div class="col-sm-10">
            
                    <button type="submit" class="btn btn-primary">
    登录
</button>
                
        </div>
        
    </div>
</div>
            
<!--form标签结束--></form>
        
                </div>
            </div>
        </div>
    </div>
</div>
        <script>
            (function () {
                var path =location.pathname.substr(1);
                var search = location.search;
                if(search!=''){
                    path += decodeURIComponent(search);
                }
                if(path == '')
                {
                    $('#navbar-str>li:eq(0) a').addClass('active');
                }else{
                    $('#navbar-str>li').each(function () {
                        var href = $(this).find('>a').attr('href');
                        if(href == path)
                        {
                            $(this).find('>a').addClass('active');
                        }
                        if($(this).find('>ul.sub_menu').length > 0)
                        {
                            var that = this;
                            $(this).find('li').each(function () {
                                var href = $(this).find('>a').attr('href');
                                if(href == path)
                                {
                                    $(this).parent().addClass('active');
                                    $(that).addClass('active');
                                }
                            })
                        }
                    })
                }

                /*---dropdown search activation---*/
                $('.search_bar > a').on('click', function(){
                    $('.dropdown_search').addClass('active')
                });

                $('.search_close_btn > a').on('click', function(){
                    $('.dropdown_search').removeClass('active')
                });

            })();
        </script>
    </header>


