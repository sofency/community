function checkPwd() {
    const firstPwd = $("#inputPassword").val();
    const secondPwd = $("#confirm").val();
    console.log(firstPwd+":"+secondPwd);
    if(firstPwd != secondPwd){
        $("#errorPwd").text("两次输入的密码不一致").css({"display":"block"});//text可以动态修改内容 而innerHtml不能
        //注册按钮无效
        $("#register-btn").removeAttr("onclick")
    }else{
        $("#errorPwd").css("display","none");
        $("#register-btn").attr("onclick","submitForm()")
    }
}
//注册表单的提交
function submitForm() {
    const password=$("#inputPassword").val();
    const email=$("#email").val();
    if(password!=""&&email!=""){
        $.ajax({
            url:"/register",
            method:"post",
            contentType:"application/json",
            dataType:"json",
            data:JSON.stringify({
                "password":password,
                "email":email
            }),
            success:function (data) {
                if(data.registered==false){
                    $("#isRegister").text("你已注册过，请登录");
                }else{//成功之后切换到登录选项
                    choose($(".login-btn"));
                }
            }
        })
    }
}
function showUnderline(obj) {
    $(obj).css({
        "border-bottom":"2px solid red"
    })
}
function hiddenLine(obj) {
    $(obj).css({
        "border-bottom":"none"
    })
}
function choose(obj) {
    console.log(obj)
    const register= $(".regist");
    const login = $(".login-btn");
    if(Object.is(obj,register[0])){//注册
        $(login).removeClass("active");
        $(obj).addClass("active");
        $(".register").css("display","block");//显示注解界面
        $(".register-btn").css("display","block");
        $(".login").css("display","none");//显示注解界面
        $(".btn-div-login").css("display","none");

    }else{
        $(obj).addClass("active");
        $(register).removeClass("active");
        $(".register").css("display","none");//显示注解界面
        $(".register-btn").css("display","none");
        $(".login").css("display","block");//显示注解界面
        $(".btn-div-login").css("display","block");
    }
}