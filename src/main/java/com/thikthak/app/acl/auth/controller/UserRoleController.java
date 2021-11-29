package com.thikthak.app.acl.auth.controller;

import com.thikthak.app.acl.auth.domain.UserRole;
import com.thikthak.app.acl.auth.service.RoleService;
import com.thikthak.app.acl.auth.service.UserRoleService;
import com.thikthak.app.acl.auth.service.UserService;
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
import java.util.Optional;

@Controller
@RequestMapping("/userrole")
public class UserRoleController {

    private UserRoleService service;
    private UserService userService;
    private RoleService roleService;
    @Autowired
    public void setInjectedBean(
            UserRoleService service,
            UserService userService,
            RoleService roleService) {
        this.service = service;
        this.userService = userService;
        this.roleService = roleService;
    }


    @RequestMapping("/index1")
    public String getAll(Model model)
    {
        List<UserRole> list = service.getAll();
        model.addAttribute("objectList", list);
        return "view/auth/userrole/index";
    }

    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    @RequestMapping("/index")
    public String getAllPaginated(HttpServletRequest request, Model model) {

        PaginationHelper pHelper = new PaginationHelper(request);
        Page<UserRole> page = service.getAllPaginated(pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< UserRole > list = page.getContent();

        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("objectList", list);

        return "view/auth/userrole/index";
    }


    @GetMapping(value = "/show/{id}")
    public String show(Model model, @PathVariable long id)
    {
        UserRole object = null;
        try {
            object = service.findById(id);
        } catch (Exception ex) {
            model.addAttribute(SysMgsStr.msgKey3, SysMgsStr.msgDesc3);
        }
        model.addAttribute("object", object);
        return "view/auth/userrole/show";
    }

    @RequestMapping(path = "/create")
    public String create(Model model)
    {
        model.addAttribute("allUser", this.userService.getAll());
        model.addAttribute("allRoles", this.roleService.getAll());
        model.addAttribute("object", new UserRole());
        return "view/auth/userrole/create";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@Valid UserRole postObjInst, BindingResult result, Model model, RedirectAttributes redirAttrs)
    {
//        if (result.hasErrors()) {
//            return "view/auth/userrole/create";
//        }
        System.out.println(postObjInst);
        postObjInst = service.createOrUpdate(postObjInst);
        model.addAttribute("object", postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1);

        return "redirect:/userrole/show/" + postObjInst.getId();
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Optional<Long> id) throws Exception
    {
        if (id.isPresent()) {
            model.addAttribute("allUser", this.userService.getAll());
            model.addAttribute("allRoles", this.roleService.getAll());
            UserRole entity = service.getById(id.get());
            model.addAttribute("object", entity);
        } else {
            model.addAttribute("object", new UserRole());
        }
        return "view/auth/userrole/edit";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(UserRole postObjInst, RedirectAttributes redirAttrs)
    {
        postObjInst = service.createOrUpdate(postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1u);
        return "redirect:/userrole/show/" + postObjInst.getId();
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirAttrs) throws Exception
    {
        service.deleteById(id);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey2, SysMgsStr.msgDesc2);
        return "redirect:/userrole/index";
    }



}
