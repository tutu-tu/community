package life.majiang.community.sevice;

import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_ONT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARM_MRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_ONT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
            //回复问题
           Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
           if (question == null){
               throw new CustomizeException(CustomizeErrorCode.QUESTION_ONT_FOUND);
           }
           //插入评论，让这两个操作同时成功，添加事务
           commentMapper.insert(comment);
           question.setCommentCount(1);
           //给问题增加评论数
           questionExtMapper.incCommentCount(question);
        }
    }
}
