package life.majiang.community.controller;

import life.majiang.community.entity.AccessTokenDTD;
import life.majiang.community.entity.GithubUser;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.service.UserService;
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

    //okhttp发起请求，待服务器响应后，回调callback接口
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
        //得到Github令牌
        String accessToken = githubProvider.getAccessToken(accessTokenDTD);
        //得到GitHub的用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        //System.out.println(githubUser.getName());
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
            //登录失败，重新登录
            log.error("callback get github error,{}",githubUser);
            System.out.println("登录失败");
            return "redirect:/";
        }

    }

    //退出，用户点击“退出登录”分发到这个接口，实现将cookie信息失效
    @GetMapping("loginout")
    public String loginOut(HttpServletRequest request,
                           HttpServletResponse response){
        request.getSession().removeAttribute("user");
        //把token信息设置为null
        Cookie cookie = new Cookie("token",null);
        //设置cookie立即失效
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
