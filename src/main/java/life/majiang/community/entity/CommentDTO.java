package life.majiang.community.entity;

import life.majiang.community.model.User;
import lombok.Data;

//传递数据给前端的dto
@Data
public class CommentDTO {

    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCont;
    private String content;
    private User user;
}
