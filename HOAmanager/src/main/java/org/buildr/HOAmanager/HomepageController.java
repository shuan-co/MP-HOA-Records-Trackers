package org.buildr.HOAmanager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String getGreeting() {
        return "index";
    }
}
