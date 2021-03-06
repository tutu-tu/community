package life.majiang.community.schedule;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 30000)
    //@Scheduled(cron = "0 0 1 * * *")
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 20;
        log.info("hotTagSchedule start: {}",new Date());
        Map<String,Integer> priorities = new HashMap<>();
        List<Question> list = new ArrayList<>();
        while (offset == 0 || list.size() == limit){
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
            for (Question question : list) {

                String[] tags = StringUtils.split(question.getTag(),',');
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if (priority != null){
                        priorities.put(tag,priority + 5 +question.getCommentCount());
                    }else {
                        priorities.put(tag,5+question.getCommentCount());
                    }

                }
            }
            offset +=limit;
        }
        /*priorities.forEach(
                (k,v)->{
                    System.out.print(k);
                    System.out.print(":");
                    System.out.print(v);
                    System.out.println();
                }
        );*/
        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop: {}",new Date());
    }
}
