package life.majiang.community.controller;

import life.majiang.community.entity.AccessTokenDTD;
import life.majiang.community.entity.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeCotroller {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.id}")
    private String clientID;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
        AccessTokenDTD accessTokenDTD = new AccessTokenDTD();
        accessTokenDTD.setCode(code);
        accessTokenDTD.setRedirect_uri(redirectUri);
        accessTokenDTD.setState(state);
        accessTokenDTD.setClient_secret(clientSecret);
        accessTokenDTD.setClient_id(clientID);
        String accessToken = githubProvider.getAccessToken(accessTokenDTD);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        if(githubUser != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写cookie和session
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";
        }else{
            System.out.println("登录失败");
            //登录失败，重新登录
            return "redirect:/";
        }

    }
}
