<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<script>
    $(function () {
        <c:if test="${!empty msg}">
        $("span.errorMessage").html("${msg}");
        $("div.loginErrorMessageDiv").show();
        </c:if>

        $("form.loginForm").submit(function(){
            if(0==$("#name").val().length||0==$("#password").val().length){
                $("span.errorMessage").html("请输入账号密码");
                $("div.loginErrorMessageDiv").show();
                return false;
            }
            return true;
        });

        $("form.loginForm input").keyup(function () {
            $("div.loginErrorMessageDiv").hide();
        });

        var left = window.innerWidth/2+162;
        $("div.loginSmallDiv").css("left",left);
    })
</script>

<div id="loginDiv" style="position: relative">
    <div class="simpleLogo">
        <a href="${contextPath}"><img src="img/site/simpleLogo.png"></a>
    </div>

    <img id="loginBackgroundImg" class="loginBackgroundImg" src="img/site/loginBackground.png">

    <form action="forelogin" class="loginForm" method="post">
        <div class="loginSmallDiv" id="loginSmallDiv">
            <div class="loginErrorMessageDiv">
                <div class="alert alert-danger">
                    <button type="button" class="close" data-dismiss="alter" aria-label="Close"></button>
                    <span class="errorMessage"></span>
                </div>
            </div>

            <div class="login_acount_text">账户登入</div>
            <div class="loginInput">
            <span class="loginInputIcon">
                <span class="glyphicon glyphicon-user"></span>
            </span>
                <input id="name" name="name" placeholder="手机/会员名/邮箱" type="text">
            </div>

            <div class="loginInput">
            <span class="loginInputIcon">
                <span class="glyphicon glyphicon-lock"></span>
            </span>
                <input id="password" name="password" type="password" placeholder="密码" >
            </div>
            <span class="text-danger">不要输入真实的天猫账号密码</span>

            <div>
                <a href="#nowhere" class="notImplementLink">忘记登入密码</a>
                <a href="registerPage" class="pull-right">免费注册</a>
            </div>
            <div class="margin-top:20px">
                <button class="btn btn-block redButton" type="submit">登 入</button>
            </div>
        </div>
    </form>
</div>