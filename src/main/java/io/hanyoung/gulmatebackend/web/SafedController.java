package io.hanyoung.gulmatebackend.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafedController {

    @GetMapping("/safe")
    public String safe() {
        return "safe page...";
    }
}
