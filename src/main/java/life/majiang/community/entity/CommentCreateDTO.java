package life.majiang.community.entity;

import lombok.Data;

//从前端传递过来数据的dto
@Data
public class CommentCreateDTO {

    private Long parentId;
    private String content;
    private Integer type;
}
