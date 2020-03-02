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
                        "<div class=\"second-comment col-lg-12 col-md-12 col-sm-12 col-xs-12\">" +
                        "<div class='media col-lg-10 col-md-10 col-sm-10 col-xs-10'>\n" +
                        "      <div class='media-left'>\n" +
                        "          <a href='#'>\n" +
                        "             <img class='media-object img-default' src="+"'"+avatarUrl+"' width='30' height='30'  onerror=\"this.src ='/static/images/error.jpg;'\">"+
                        "          </a>\n" +
                        "      </div>\n" +
                        "      <div class='media-body text-999'>\n" +
                        "           <span class='media-heading text-user' style='font-size: 14px' >"+commentName+"</span><br>\n" +
                        "           <div>"+data.content+"</div>\n" +
                        "      </div>\n" +
                        "</div>\n" +
                        "<div class='col-lg-2 col-md-2 col-sm-2 col-xs-2'>\n" +
                        "      <button class='btn' style='float: right'>回复</button><button class='btn' style='float: right'>删除</button>\n" +
                        "</div>\n" +
                        "</div>";
                })
                $("#full-comment-second"+parentId).html(allContent)
            }
        }

    })
}