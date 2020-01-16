package com.epxing.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 替换掉spring security中的loginPage地址
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String, Object> defaultLoginPage(HttpServletRequest request, HttpServletResponse response) {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是:" + targetUrl);
        }
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("msg", "访问的是受保护的地址，请先登录后在访问");
        return returnMap;

    }

    /**
     * 定义用来接收授权码code的接口
     *
     * @param request
     * @param response
     * @param code
     * @return
     */
    @GetMapping("/public/getCode")
    public String getCode(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code) {
        logger.info("code:" + code);
        return code;
    }

}
