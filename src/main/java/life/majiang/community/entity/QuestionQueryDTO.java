package life.majiang.community.entity;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
    private String sort;
    private Long time;


}
