package life.majiang.community.cache;

import life.majiang.community.entity.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("JavaScript","Java","CSS","html5","node.js","asp.net","shell","php","c++","c语言","html"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","yii","struts","koa","express","flask"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("nginx","linux","apache","ubuntu","tomcat","windows-server","docker"));
        tagDTOS.add(server);

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库");
        database.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","sqlserver","memcached","postgresql","sqlite"));
        tagDTOS.add(database);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("intellij ide","github","eclipse","maven","git","svn","visual-studio","visual-studio-code","vim","sublime-text","ide"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] splits = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(splits).filter(t-> !tagList.contains(t)).collect(Collectors.joining());
        return invalid;
    }
}
