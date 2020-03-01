function comment(type) {
    var parentId = $("#parentId").val();
    var comment = $("#content").val();
    $.ajax({
        url:"/comment",
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