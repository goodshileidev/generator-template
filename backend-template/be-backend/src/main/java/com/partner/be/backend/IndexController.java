package com.partner.be.backend;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api("页面动作")
@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public Object index(@RequestParam(required = false, defaultValue = "zhang") String name) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("message", "hello " + name);
        return jsonObject;

    }
}
