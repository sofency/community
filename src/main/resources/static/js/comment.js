function commonFunc(parentId,comment,type){
    $.ajax({
        url:"/commentSubmit",
        method:"POST",
        contentType:"application/json",
        dataType:"json",
        data:JSON.stringify({
            "parentId":parentId,
            "comment":comment,
            "type":type
        }),
        success:function(data){
            if(data.code==200){
                $("#comment-hidden").hide();
            }else{
                var accept = confirm(data.message);
                console.log(accept)
                if(accept){
                    window.open("https://github.com/login/oauth/authorize?client_id=94d38df2b20a7acb563f&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    window.localStorage.setItem("closeable","true"); //存储的是key-value的形式
                }
            }

        }
    })
}
function comment1(obj) {
    var parentId = $("#parentId").val();
    var comment = $("#content").val();
    commonFunc(parentId,comment,1);
}
function comment2(obj) {
    var parentId = $(obj).attr("data-commentId");
    var comment = $("#evaluate"+parentId).val();
    console.log(parentId+comment);
    commonFunc(parentId,comment,2);
}