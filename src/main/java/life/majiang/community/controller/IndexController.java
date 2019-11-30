package life.majiang.community.controller;

import life.majiang.community.entity.QuestionDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.sevice.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionService questionService;
    
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0)
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){

                //如果是前后端分离项目，可以在这里加一个验证信息，返回一个验证错误，登录一超时等等
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if(user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions",questionList);
        return "index";
    }
}
