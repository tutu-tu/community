package life.majiang.community.controller;

import life.majiang.community.entity.AccessTokenDTD;
import life.majiang.community.entity.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeCotroller {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state){
        AccessTokenDTD accessTokenDTD = new AccessTokenDTD();
        accessTokenDTD.setCode(code);
        accessTokenDTD.setRedirect_uri("http://localhost:8888/callback");
        accessTokenDTD.setState(state);
        accessTokenDTD.setClient_secret("fdabf177dea0caf4a19aa707a1a7dee7b5b07103");
        accessTokenDTD.setClient_id("572f119c724905b046f1");
        String accessToken = githubProvider.getAccessToken(accessTokenDTD);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
