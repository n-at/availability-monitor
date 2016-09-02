package ru.doublebyte.availabilitymonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.types.Monitoring;

import java.util.List;
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
            @RequestParam("name") String name,
            @RequestParam("url") String url,
            @RequestParam("check-interval") int checkInterval,
            @RequestParam("respond-interval") int respondInterval,
            Model model
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
        model.addAttribute("error_message", error);
        model.addAttribute("monitoring", monitoring);
        return "add";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editForm(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Monitoring monitoring = monitoringManager.get(id);

        if (monitoring == null) {
            redirectAttributes.addFlashAttribute("error_message",
                    String.format("Monitoring with id %d not found", id));
            return "redirect:/";
        }

        model.addAttribute("monitoring", monitoring);

        return "edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String edit(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("url") String url,
            @RequestParam("check-interval") int checkInterval,
            @RequestParam("respond-interval") int respondInterval,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Monitoring monitoring = monitoringManager.get(id);
        if (monitoring == null) {
            redirectAttributes.addFlashAttribute("error_message",
                    String.format("Monitoring with id %d not found", id));
            return "redirect:/";
        }

        monitoring.setName(name);
        monitoring.setUrl(url);
        monitoring.setCheckInterval(checkInterval);
        monitoring.setRespondInterval(respondInterval);

        List<String> errors = monitoringManager.validate(monitoring);
        if (errors.isEmpty()) {
            if (monitoringManager.update(monitoring)) {
                return "redirect:/";
            } else {
                errors.add("An error occurred while saving monitoring");
            }
        }

        String error = errors.stream().collect(Collectors.joining(". "));
        model.addAttribute("error_message", error);
        model.addAttribute("monitoring", monitoring);
        return "edit";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        if (monitoringManager.delete(id)) {
            redirectAttributes.addFlashAttribute("success_message", "Monitoring deleted");
        } else {
            redirectAttributes.addFlashAttribute("error_message", "An error occurred while deleting monitoring");
        }
        return "redirect:/";
    }

}
