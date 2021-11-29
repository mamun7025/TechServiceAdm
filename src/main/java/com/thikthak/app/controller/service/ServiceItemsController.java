package com.thikthak.app.controller.service;

import com.thikthak.app.domain.service.ServiceItems;
import com.thikthak.app.service.service.ServiceItemsService;
import com.thikthak.app.util.PaginationHelper;
import com.thikthak.app.util.SysMgsStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/serviceitem")
public class ServiceItemsController {


    private ServiceItemsService service;
    @Autowired
    public void setInjectedBean(ServiceItemsService service) {
        this.service = service;
    }


    @RequestMapping("/index1")
    public String getAll(Model model)
    {
        List<ServiceItems> list = service.getAll();
        model.addAttribute("objectList", list);
        return "view/service/serviceitem/index";
    }

    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    @RequestMapping("/index")
    public String getAllPaginated(HttpServletRequest request, Model model) {

        PaginationHelper pHelper = new PaginationHelper(request);
        Page<ServiceItems> page = service.getAllPaginated(pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< ServiceItems > list = page.getContent();

        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("objectList", list);

        return "view/service/serviceitem/index";
    }


    @GetMapping(value = "/show/{id}")
    public String show(Model model, @PathVariable long id)
    {
        ServiceItems object = null;
        try {
            object = service.findById(id);
        } catch (Exception ex) {
            model.addAttribute(SysMgsStr.msgKey3, SysMgsStr.msgDesc3);
        }
        model.addAttribute("object", object);
        return "view/service/serviceitem/show";
    }

    @RequestMapping(path = "/create")
    public String create(Model model)
    {
        model.addAttribute("object", new ServiceItems());
        return "view/service/serviceitem/create";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@Valid ServiceItems postObjInst, BindingResult result, Model model, RedirectAttributes redirAttrs)
    {
        if (result.hasErrors()) {
            return "view/service/serviceitem/create";
        }
        postObjInst = service.createOrUpdate(postObjInst);
        model.addAttribute("object", postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1);

        return "redirect:/serviceitem/show/" + postObjInst.getId();
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Long id) throws Exception
    {
        if (id != null) {
            ServiceItems entity = service.getById(id);
            model.addAttribute("object", entity);
        } else {
            model.addAttribute("object", new ServiceItems());
        }
        return "view/service/serviceitem/edit";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(ServiceItems postObjInst, RedirectAttributes redirAttrs)
    {
        postObjInst = service.createOrUpdate(postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1u);
        return "redirect:/serviceitem/show/" + postObjInst.getId();
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirAttrs) throws Exception
    {
        service.deleteById(id);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey2, SysMgsStr.msgDesc2);
        return "redirect:/serviceitem/index";
    }



}
