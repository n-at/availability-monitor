package ru.doublebyte.availabilitymonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/test/")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/timeout/{interval}", method = RequestMethod.GET)
    public String timeout(
            @PathVariable("interval") int interval
    ) {
        logger.info("Timeout method, interval: {}", interval);

        try {
            Thread.sleep(interval);
        } catch (Exception ignored) {}

        return "ok";
    }

    @RequestMapping(value = "/status/{code}", method = RequestMethod.GET)
    public String status(
            @PathVariable("code") int code,
            HttpServletResponse response
    ) {
        logger.info("Status method, code: {}", code);
        response.setStatus(code);
        return "ok";
    }

}
