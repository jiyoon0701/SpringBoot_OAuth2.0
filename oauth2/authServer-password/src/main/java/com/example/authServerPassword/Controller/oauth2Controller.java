package com.example.authServerPassword.Controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("oauth2")
@Log4j
public class oauth2Controller {

    @RequestMapping("check_token")
    public void check_token() {
        log.info("oauth2Controller :::: check_token 호출");

    }
}
