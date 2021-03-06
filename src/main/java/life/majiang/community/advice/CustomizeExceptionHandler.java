package life.majiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.majiang.community.entity.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*自定义的错误页面*/
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ModelAndView handle(Throwable e,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){

        String contentType = request.getContentType();
        //如果内容类型是json格式，返回json格式，否则就是浏览器的HTML格式
        if ("application/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回json
            if (e instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException)e);
            }else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                //toJSONString，直接将一个object变成一个json对象
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
            }
            return null;
        }else {
            //错误页面跳转
            if (e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR);
            }
            return new ModelAndView("error");
        }
    }

}
