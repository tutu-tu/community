package life.majiang.community.controller;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.entity.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired(required = false)
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;

    //主页面
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "search",required = false)String search,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "tag",required = false)String tag,
                        @RequestParam(name = "sort", required = false) String sort
                        ) {
        PaginationDTO pagination = questionService.list(search,page,size,tag,sort);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tags", tags);
        model.addAttribute("tag", tag);
        model.addAttribute("sort", sort);
        return "index";
    }
}
