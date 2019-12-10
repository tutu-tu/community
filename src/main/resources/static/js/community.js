function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    if (!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            if (response.code == 200){
                $("#comment_section").hide();
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