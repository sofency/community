var i=0;
for(; i <  $(".img").length; i++){
    $(".img").eq(i).onerror = function(obj){
        console.log("sofency");
        obj.src = "/static/images/error.jpg";
    }
}


