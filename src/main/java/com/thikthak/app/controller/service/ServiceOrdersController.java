package com.thikthak.app.controller.service;

import com.thikthak.app.domain.service.ServiceOrders;
import com.thikthak.app.service.service.ServiceOrdersService;
import com.thikthak.app.util.PaginationHelper;
import com.thikthak.app.util.SysMgsStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/serviceorder")
public class ServiceOrdersController {

    public Map<String, String> clientParams;

    private ServiceOrdersService service;
    @Autowired
    public void setInjectedBean(ServiceOrdersService service) {
        this.service = service;
    }


    @RequestMapping("/index1")
    public String getAll(Model model)
    {
        List<ServiceOrders> list = service.getAll();
        model.addAttribute("objectList", list);
        return "view/service/serviceorder/index";
    }

    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    @RequestMapping("/index")
    public String getAllPaginated(HttpServletRequest request, Model model, @RequestParam Map<String,String> clientParams) {

        PaginationHelper pHelper = new PaginationHelper(request);
        Page<ServiceOrders> page = service.getAllPaginated(clientParams, pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< ServiceOrders > list = page.getContent();

        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("objectList", list);

        return "view/service/serviceorder/index";

    }


    @GetMapping(value = "/show/{id}")
    public String show(Model model, @PathVariable long id)
    {
        ServiceOrders object = null;
        try {
            object = service.findById(id);
        } catch (Exception ex) {
            model.addAttribute(SysMgsStr.msgKey3, SysMgsStr.msgDesc3);
        }
        model.addAttribute("object", object);
        return "view/service/serviceorder/show";
    }

    @RequestMapping(path = "/create")
    public String create(Model model)
    {
        model.addAttribute("object", new ServiceOrders());
        return "view/service/serviceorder/create";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@Valid ServiceOrders postObjInst, BindingResult result, Model model, RedirectAttributes redirAttrs)
    {
        if (result.hasErrors()) {
            return "view/service/serviceorder/create";
        }
        postObjInst = service.createOrUpdate(postObjInst);
        model.addAttribute("object", postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1);

        return "redirect:/serviceorder/show/" + postObjInst.getId();
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Optional<Long> id) throws Exception
    {
        if (id.isPresent()) {
            ServiceOrders entity = service.getById(id.get());
            model.addAttribute("object", entity);
        } else {
            model.addAttribute("object", new ServiceOrders());
        }
        return "view/service/serviceorder/edit";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(ServiceOrders postObjInst, RedirectAttributes redirAttrs)
    {
        postObjInst = service.createOrUpdate(postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1u);
        return "redirect:/serviceorder/show/" + postObjInst.getId();
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirAttrs) throws Exception
    {
        service.deleteById(id);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey2, SysMgsStr.msgDesc2);
        return "redirect:/serviceorder/index";
    }


}
