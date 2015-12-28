// JavaScript Document
//滚动插件
function AutoScroll(obj){
		var height = $(obj).find("ul:first")[0].childNodes[0].offsetHeight;
        $(obj).find("ul:first").animate({
                marginTop:"-"+height+"px"
        },500,function(){
               $(this).css({marginTop:"0px"}).find("li:first").appendTo(this);
        });
}
$(document).ready(function(){
setInterval('AutoScroll("#scrollDivPic")',5000)
});