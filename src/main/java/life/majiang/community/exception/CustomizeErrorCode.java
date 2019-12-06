package life.majiang.community.exception;

//为了后面不去操作类爆炸
public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_ONT_FOUND("你找的问题不在了，要不要换一个试试？");

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }


}
