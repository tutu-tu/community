package life.majiang.community.sevice;

import javafx.scene.effect.SepiaTone;
import life.majiang.community.entity.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

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


    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        //按照创建时间倒叙
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //java8的语法,除掉重复的评论，例如四条评论有五个人重复，那就拿掉五个人的id就行了
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人，并转换为map，
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
