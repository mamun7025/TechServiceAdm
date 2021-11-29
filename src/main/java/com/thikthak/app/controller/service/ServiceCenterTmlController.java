package com.thikthak.app.controller.service;

import com.thikthak.app.domain.service.ServiceOrders;
import com.thikthak.app.repository.service.ServiceOrdersRepository;
import com.thikthak.app.service.service.ServiceCenterTmlService;
import com.thikthak.app.util.PaginationHelper;
import com.thikthak.app.util.SysMgsStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/serviceCenterTml")
public class ServiceCenterTmlController {

    // g variables
    public Map<String, String> clientParams;

    // services
    private ServiceCenterTmlService service;

    // repository
    private ServiceOrdersRepository repository;

    @Autowired
    public void setInjectedBean(ServiceCenterTmlService service, ServiceOrdersRepository repository) {
        this.service = service;
        this.repository = repository;
    }


    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping("/index")
    public String getAllPaginated(HttpServletRequest request, Model model, @RequestParam Map<String,String> clientParams) {
        this.clientParams = clientParams;

        PaginationHelper pHelper = new PaginationHelper(request);
        Page<ServiceOrders> page = this.service.getAllPaginated(this.clientParams, pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< ServiceOrders > list = page.getContent();

        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("objectList", list);

        return "view/service/service_center/index";
    }



    @GetMapping(value = "/setPartsProvided/{id}")
    public String setPartsProvided(Model model, @PathVariable long id, RedirectAttributes redirAttrs)
    {
        ServiceOrders object = null;
        try {
            object = this.service.findById(id);
            object.setServicePartsProvided(true);
            this.repository.save(object);
            redirAttrs.addFlashAttribute(SysMgsStr.msgKey77, SysMgsStr.msgDesc77 + object.getOrderCode());
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute(SysMgsStr.msgKey99, SysMgsStr.msgDesc99);
        }
        model.addAttribute("object", object);

        return "redirect:/serviceCenterTml/index";
    }

    @GetMapping(value = "/unsetPartsProvided/{id}")
    public String unsetPartsProvided(Model model, @PathVariable long id, RedirectAttributes redirAttrs)
    {
        ServiceOrders object = null;
        try {
            object = this.service.findById(id);
            object.setServicePartsProvided(false);
            this.repository.save(object);
            redirAttrs.addFlashAttribute(SysMgsStr.msgKey77, SysMgsStr.msgDesc77 + object.getOrderCode());
        } catch (Exception ex) {
            redirAttrs.addFlashAttribute(SysMgsStr.msgKey99, SysMgsStr.msgDesc99);
        }
        model.addAttribute("object", object);

        return "redirect:/serviceCenterTml/index" + "?servicePartsProvided=1";
    }




}
