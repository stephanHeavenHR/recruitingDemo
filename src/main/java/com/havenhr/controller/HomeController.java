package com.havenhr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class HomeController {

    @RequestMapping("/")
    @ApiIgnore
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
