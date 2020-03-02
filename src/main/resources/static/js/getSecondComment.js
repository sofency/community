function watchComment(obj){
    var parentId = $(obj).attr('data-commentId');
    console.log(parentId);
    $.ajax({
        url:"/commentGetSecond",
        dataType:'json',
        method:'GET',
        data:{
            "parentId":parentId,//一级评论的id
        },
        success:function(response){
            console.log(response)
            console.log(response.data)
            var allContent="";
            if(response.code==200){
                $.each(response.data,function (index,data) {
                    var commentName = data.user.name;
                    var avatarUrl = data.user.avatarUrl;
                    allContent = allContent +
                        "<div class=\"second-comment col-lg-12 col-md-12 col-sm-12 col-xs-12\"><div class='media col-lg-10 col-md-10 col-sm-10 col-xs-10'><div class='media-left'><a href='#'><img class='media-object img-default' src="+"'"+avatarUrl+"' width='30' height='30'  onerror=\"nofind();\"</a></div><div class='media-body text-999'><span class='media-heading text-user' style='font-size: 14px' >"+commentName+"</span><br><div>"+data.content+"</div></div> </div><div class='col-lg-2 col-md-2 col-sm-2 col-xs-2'><button class='btn' style='float: right'>回复</button><button class='btn' style='float: right'>删除</button></div></div>";
                })
                $("#full-comment-second"+parentId).html(allContent)
            }
        }

    })
}