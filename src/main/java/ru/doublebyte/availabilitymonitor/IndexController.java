package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.doublebyte.availabilitymonitor.entities.Email;
import ru.doublebyte.availabilitymonitor.managers.EmailManager;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.managers.TestResultManager;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.entities.TestResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final int STATUS_PAGE_SIZE = 20;

    private final MonitoringManager monitoringManager;
    private final TestResultManager testResultManager;
    private final EmailManager emailManager;

    ///////////////////////////////////////////////////////////////////////////

    @Autowired
    public IndexController(
            MonitoringManager monitoringManager,
            TestResultManager testResultManager,
            EmailManager emailManager
    ) {
        this.monitoringManager = monitoringManager;
        this.testResultManager = testResultManager;
        this.emailManager = emailManager;
    }

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<Monitoring> monitorings = monitoringManager.getAll()
                .stream()
                .peek(it -> {
                    TestResult testResult = testResultManager.getById(it.getId());
                    it.setLatestTestResult(testResult);
                })
                .collect(Collectors.toList());

        model.addAttribute("monitorings", monitorings);
        return "index";
    }

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addForm() {
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
        monitoring.setActive(true);

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

    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/active/{id}", method = RequestMethod.GET)
    public String active(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        Monitoring monitoring = monitoringManager.get(id);
        if (monitoring == null) {
            redirectAttributes.addFlashAttribute("error_message",
                    String.format("Monitoring with id %d not found", id));
            return "redirect:/";
        }

        monitoring.setActive(monitoring.isActive() == null || !monitoring.isActive());
        if (!monitoringManager.update(monitoring)) {
            redirectAttributes.addFlashAttribute("error_message", "An error occurred while changing activity status");
        }

        return "redirect:/";
    }

    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/status/{id}/{page}", method = RequestMethod.GET)
    public String status(
            @PathVariable("id") Long id,
            @PathVariable("page") int page,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Monitoring monitoring = monitoringManager.get(id);
        if (monitoring == null) {
            redirectAttributes.addFlashAttribute("error_message",
                    String.format("Monitoring with id %d not found", id));
            return "redirect:/";
        }

        if (page <= 0) {
            page = 1;
        }

        List<TestResult> testResults =
                Collections.singletonList(testResultManager.getById(id));

        model.addAttribute("monitoring", monitoring);
        model.addAttribute("test_results", testResults);

        addPagesModelVariables(model, page, testResults.size() != STATUS_PAGE_SIZE);

        return "status";
    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
    public String status(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return status(id, 1, model, redirectAttributes);
    }

    ///////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public String emailForm(Model model) {
        model.addAttribute("emails", emailManager.getAll());
        return "email";
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public String email(
            @RequestParam("address") String address,
            RedirectAttributes redirectAttributes
    ) {
        if (address == null || address.isEmpty()) {
            redirectAttributes.addFlashAttribute("error_message", "Address should not be empty");
            return "redirect:/email";
        }

        Email email = new Email(address);
        if (emailManager.add(email)) {
            redirectAttributes.addFlashAttribute("success_message", "Email added");
        } else {
            redirectAttributes.addFlashAttribute("error_message", "An error occurred while adding new email");
        }

        return "redirect:/email";
    }

    @RequestMapping(value = "/email/delete/{id}", method = RequestMethod.GET)
    public String emailDelete(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        if (emailManager.delete(id)) {
            redirectAttributes.addFlashAttribute("success_message", "Email removed");
        } else {
            redirectAttributes.addFlashAttribute("error_message", "An error occurred while removing email");
        }

        return "redirect:/email";
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Add to model attributes with page numbers
     * @param model
     * @param currentPage
     * @param isLast
     */
    private void addPagesModelVariables(Model model, int currentPage, boolean isLast) {
        model.addAttribute("current_page", currentPage);

        if (currentPage > 1) {
            model.addAttribute("prev_page", currentPage - 1);
        }

        if (!isLast) {
            model.addAttribute("next_page", currentPage + 1);
        }
    }
}
