package userapp.brian.duran.userappapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class TestController {

    @GetMapping
    public String sayHello() {
        return "Hola, de nuevo en la Tierra! :)";
    }
}
