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
            var subCommentContainer = $("#comment-"+id);
            //加载过了就不需要再加载了，否则再重新加载
            if (subCommentContainer.children().length != 1){
                //展开二级评论
                comments.addClass("in");
                //标记评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            }else {
                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left",
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body",
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu",
                    })).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                //标记评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            }
        });
    }
}