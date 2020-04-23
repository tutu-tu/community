package life.majiang.community.entity;

import life.majiang.community.model.User;
import lombok.Data;

/*基本和mapper中的question一样，但是多个个User对象，减少表的关联，做传输用的*/
@Data
public class QuestionDTO {
        private Long id;
        private String title;
        private String description;
        private Long gmtCreate;
        private Long gmtModified;
        private Long creator;
        private Integer commentCount;
        private Integer viewCount;
        private Integer likeCount;
        private String tag;
        private User user;

}


