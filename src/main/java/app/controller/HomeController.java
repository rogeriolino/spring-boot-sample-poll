package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class HomeController {

    @RequestMapping({
        "/",
        "/registration",
        "/my-polls",
        "/polls",
        "/polls/new"
    })
    String index() {
        return "index";
    }

    @RequestMapping({
        "/polls/{id}/edit",
        "/polls/{id}"
    })
    String pollId(@PathVariable String id) {
        return "index";
    }
}