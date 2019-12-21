package life.majiang.community.controller;

import life.majiang.community.entity.AccessTokenDTD;
import life.majiang.community.entity.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeCotroller {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.id}")
    private String clientID;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletResponse response){
        AccessTokenDTD accessTokenDTD = new AccessTokenDTD();
        accessTokenDTD.setCode(code);
        accessTokenDTD.setRedirect_uri(redirectUri);
        accessTokenDTD.setState(state);
        accessTokenDTD.setClient_secret(clientSecret);
        accessTokenDTD.setClient_id(clientID);
        String accessToken = githubProvider.getAccessToken(accessTokenDTD);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        if(githubUser != null && githubUser.getId() != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            String token = UUID.randomUUID().toString();
            user.setToken(token);

            user.setAvatarUrl(githubUser.getAvatar_url());
            //登录成功，写cookie和session
            //userMapper.insert(user);
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            //request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";
        }else{
            log.error("callback get github error,{}",githubUser);
            System.out.println("登录失败");
            //登录失败，重新登录
            return "redirect:/";
        }

    }

    @GetMapping("loginout")
    public String loginOut(HttpServletRequest request,
                           HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
