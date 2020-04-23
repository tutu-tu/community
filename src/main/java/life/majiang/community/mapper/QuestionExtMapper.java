package life.majiang.community.mapper;

import life.majiang.community.entity.QuestionQueryDTO;
import life.majiang.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    //阅读
    int incView(Question record);
    //评论
    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}