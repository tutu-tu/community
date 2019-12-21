package life.majiang.community.cache;

import life.majiang.community.entity.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();
    public void updateTags(Map<String ,Integer>tags){
        int max =5;
        //priorityQueue基于Java的优先队列
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name,priority)->{
           HotTagDTO hotTagDTO = new HotTagDTO();
           hotTagDTO.setName(name);
           hotTagDTO.setPriority(priority);
           if (priorityQueue.size()<max){
               priorityQueue.add(hotTagDTO);
           }else{
               HotTagDTO minHot = priorityQueue.peek();
               //如果当前标签的优先级大于最小热度的，就放进去
               if (hotTagDTO.compareTo(minHot)>0){
                   priorityQueue.poll();
                   priorityQueue.add(hotTagDTO);
               }
           }
        });
        List<String> sortedTags = new ArrayList<>();
        HotTagDTO poll = priorityQueue.poll();
        while (poll !=null){
            sortedTags.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }

}
