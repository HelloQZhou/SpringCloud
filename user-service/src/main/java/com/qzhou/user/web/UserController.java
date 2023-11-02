package com.qzhou.user.web;

import com.qzhou.user.config.PatternProperties;
import com.qzhou.user.pojo.User;
import com.qzhou.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user")
@RefreshScope //配置热更新
public class UserController {

    @Autowired
    private UserService userService;

//    @ConfigurationProperties("${pattern.dateformat}")
//    @Value("${pattern.dateformat}")
//    private String dateformat;
    @Autowired
    private PatternProperties patternProperties;

    @GetMapping("/now")
    public String now(){
        //return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.getDateformat()));
    }
    @GetMapping("/share")
    public PatternProperties share(){
        return patternProperties;
    }

    /**
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
}
