package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private final String mWelcomeMessage;

    public WelcomeController(@Value("${WELCOME_MESSAGE}") String pWelcomeMessage) {
        mWelcomeMessage = pWelcomeMessage;
    }

    @GetMapping("/")
    public String welcome() {
        return mWelcomeMessage;
    }
}
