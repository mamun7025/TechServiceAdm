package com.thikthak.app.controller.service;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.service.service.TechUserService;
import com.thikthak.app.util.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/techuser")
public class TechUserController {

    public Map<String, String> clientParams;

    private TechUserService service;
    @Autowired
    public void setInjectedBean(TechUserService service) {
        this.service = service;
    }



    @RequestMapping("/index")
    public String getAllPaginated(HttpServletRequest request, Model model, @RequestParam Map<String,String> clientParams) {
        this.clientParams = clientParams;

        PaginationHelper pHelper = new PaginationHelper(request);
        Page<User> page = this.service.getAllPaginated(clientParams, pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< User > list = page.getContent();

        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("objectList", list);

        return "view/service/techuser/index";

    }


    @GetMapping(value = "/show/{id}")
    public String show(Model model, @PathVariable long id)
    {
        User user = null;
        try {
            user = service.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Entity not found");
        }
        model.addAttribute("object", user);
        model.addAttribute("timestamp", Instant.now());
        return "view/service/techuser/show";
    }

    @RequestMapping(path = "/create")
    public String create(Model model)
    {
        model.addAttribute("user", new User());
        return "view/service/techuser/create";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@Valid User user, BindingResult result, Model model, RedirectAttributes redirAttrs)
    {
        if (result.hasErrors()) {
            return "view/service/techuser/create";
        }
        user = service.createOrUpdate(user);
        model.addAttribute("object", user);
        redirAttrs.addFlashAttribute("successMgs", "Successfully save transaction");

        return "redirect:/techuser/show/" + user.getId();
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Optional<Long> id) throws Exception
    {
        if (id.isPresent()) {
            User entity = service.getById(id.get());
            model.addAttribute("user", entity);
        } else {
            model.addAttribute("user", new User());
        }
        return "view/service/techuser/edit";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(User user, RedirectAttributes redirAttrs)
    {
        user = service.createOrUpdate(user);
        redirAttrs.addFlashAttribute("successMgs", "Successfully update transaction");
        // return "redirect:/user/index";
        return "redirect:/techuser/show/" + user.getId();
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirAttrs) throws Exception
    {
        service.deleteById(id);
        redirAttrs.addFlashAttribute("warningMgs", "Successfully delete transaction");
        return "redirect:/techuser/index";
    }



}
