package ru.doublebyte.availabilitymonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.types.Monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final MonitoringManager monitoringManager;

    ///////////////////////////////////////////////////////////////////////////

    @Autowired
    public IndexController(MonitoringManager monitoringManager) {
        this.monitoringManager = monitoringManager;
    }

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("monitorings", monitoringManager.getAll());
        return "index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addForm(Model model) {
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "url") String url,
            @RequestParam(name = "check-interval") int checkInterval,
            @RequestParam(name = "respond-interval") int respondInterval,
            RedirectAttributes redirectAttributes
    ) {
        Monitoring monitoring = new Monitoring(url, name, checkInterval, respondInterval);
        List<String> errors = monitoringManager.validate(monitoring);

        if (errors.isEmpty()) {
            if (monitoringManager.add(monitoring)) {
                return "redirect:/";
            } else {
                errors.add("An error occurred while adding new monitoring");
            }
        }

        String error = errors.stream().collect(Collectors.joining(". "));
        redirectAttributes.addFlashAttribute("error_message", error);
        redirectAttributes.addFlashAttribute("monitoring", monitoring);
        return "redirect:/add";
    }

}
