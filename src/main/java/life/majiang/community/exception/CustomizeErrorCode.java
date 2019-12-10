package life.majiang.community.exception;

//为了后面不去操作类爆炸
public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_ONT_FOUND(2001,"你找的问题不在了，要不要换一个试试？"),
    TARGET_PARAM_ONT_FOUND(2002,"未选中任何问题或则评论进行回复"),
    NO_LOGIN(2003,"当前操作要登录，请登录后在重试"),
    SYS_ERROR(2004,"服务冒烟了，请骚后再试试！"),
    TYPE_PARM_MRONG(2005,"评论类型错误或者不存在"),
    COMMENT_ONT_FOUND(2006,"你回复的评论不在了，要不要换一个试试？"),
    CONTENT_IS_EMPTY(2007,"回复内容不能为空"),

    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() { return code; }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
