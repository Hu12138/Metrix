package site.ahzx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/node")
public class NodeController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

}
