/**
 * 重构一个方法，方便重复调用
 */
function comment2target(targerId,type,content) {
    if (!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId":targerId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
                // $("#comment_section").hide();
            }else {
                if (response.code == 2003){
                    //弹出一个登录框，问是否登录
                    var isAccepted = confirm(response.message);
                    //如果你登录
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=572f119c724905b046f1&redirect_uri=http://localhost:8888/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}

/**
 * 提交回复
 */
function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    comment2target(questionId,1,content)
}

/**
 * 二级评论
 * @param e
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}
/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse){
        //折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {
        $.getJSON("/comment/"+id,function (data) {
            var items = [];
            $.each(data,function (key,vau) {
                items.push("<li id='" + key + "'>" + val + "</li>");
            });
            $("<ul/>", {
                "class": "my-new-list",
                html: items.join("")
            }).appendTo("body");
        });
        //展开二级评论
        comments.addClass("in");
        //标记评论展开状态
        e.setAttribute("data-collapse","in");
        e.classList.add("active");
    }
}