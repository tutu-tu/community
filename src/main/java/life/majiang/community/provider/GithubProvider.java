package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.entity.AccessTokenDTD;
import life.majiang.community.entity.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//github授权
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTD accessTokenDTD){
        //MediaType是一个响应式设计的媒体类型
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //okhttp是HTTP请求的一个框架
        OkHttpClient client = new OkHttpClient();

        //post请求的时候需要用到requestbody，赋给request对象，get请求则不需要
        //JSON.toJSONString() 底层，传入一个Object对象，返回一个json格式的字符串
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTD));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //通过构造 OkHttpClient 对象发起一次请求并加入队列，待服务端响应后，
            // 回调 Callback 接口触发 onResponse() 方法，
            // 然后在该方法中通过 Response 对象处理返回结果、实现业务逻辑
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //获得user信息
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
