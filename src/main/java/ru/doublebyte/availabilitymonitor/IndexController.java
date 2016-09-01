package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;

@Controller
@RequestMapping("/")
public class IndexController {

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
    public String add(Model model) {
        return "add";
    }

}
