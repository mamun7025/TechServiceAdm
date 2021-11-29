package com.thikthak.app.controller.service;

import com.thikthak.app.domain.service.ServiceOrders;
import com.thikthak.app.service.service.CompanyChargeService;
import com.thikthak.app.util.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/companyCharge")
public class CompanyChargeController {

    // g variables
    public Map<String, String> clientParams;

    // services
    private CompanyChargeService service;
    @Autowired
    public void setInjectedBean(CompanyChargeService service) {
        this.service = service;
    }


    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
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

        return "view/service/company_charge/index";
    }




}
