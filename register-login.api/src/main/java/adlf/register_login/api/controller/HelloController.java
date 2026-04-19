package adlf.register_login.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "¡ API EN FUNCIONAMIENTO !";
    }
}
