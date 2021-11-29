package com.thikthak.app.controller.geoloc;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.domain.geoloc.UserLocation;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import com.thikthak.app.service.geoloc.UserLocationService;
import com.thikthak.app.util.PaginationHelper;
import com.thikthak.app.util.SysMgsStr;
import com.thikthak.app.util.place.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class UserLocationController {

    // g variables
    public Map<String, String> clientParams;

    // services
    private UserLocationService service;
    private UserLocationRepository userLocationRepository;
    private UserRepository userRepository;
    @Autowired
    public void setInjectedBean(UserLocationService service, UserRepository userRepository, UserLocationRepository userLocationRepository) {
        this.service = service;
        this.userRepository = userRepository;
        this.userLocationRepository = userLocationRepository;
    }


    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
//    @RequestMapping("/index")
//    @RequestMapping(value = "/userlocation/index")
    @RequestMapping(value = {"/userlocation", "/userlocation/index" }, method = GET)
    public String getAllPaginated(HttpServletRequest request, Model model, @RequestParam Map<String,String> clientParams) {
        this.clientParams = clientParams;

        PaginationHelper pHelper = new PaginationHelper(request);
        pHelper.setDefaultSortDir("lastUpdateDateTime", "desc");
        Page<UserLocation> page = this.service.getAllPaginated(this.clientParams, pHelper.pageNum, pHelper.pageSize, pHelper.sortField, pHelper.sortDir);
        List< UserLocation > list = page.getContent();

        // populate for process
        UserLocation fUserLocation = null;
        if(this.clientParams.containsKey("dstFromUser")){
            if (!StringUtils.isEmpty(this.clientParams.get("dstFromUser"))) {
                User userInst = this.userRepository.getUserByUsername(clientParams.get("dstFromUser"));
                fUserLocation = this.userLocationRepository.getByUser(userInst);
            }
        }
        Map<Long, String> calDist = new HashMap<>();
        if(fUserLocation != null){
            for ( UserLocation tUserLocation : list ){
                Double dist = PlaceService.distance(fUserLocation.getLatitude(), fUserLocation.getLongitude(), tUserLocation.getLatitude(), tUserLocation.getLongitude(), "K");
                String distStr = String.format("%,.3f", dist);
                calDist.put(tUserLocation.getId(), distStr);
            }
        }


        model.addAttribute("currentPage", pHelper.pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", pHelper.sortField);
        model.addAttribute("sortDir", pHelper.sortDir);
        model.addAttribute("reverseSortDir", pHelper.sortDir.equals("asc") ? "desc" : "asc");

        // pagination with search field
//        String queryParams = request.getQueryString();
//        if(queryParams != null && !queryParams.equals("")) queryParams = queryParams + "&";
        StringBuilder queryParams = new StringBuilder();
        for( Map.Entry<String, String> entry : this.clientParams.entrySet() ) {
            String pKey = entry.getKey();
            String pVal = entry.getValue();
            if(pKey.equals("pageNum") || pKey.equals("sortField") || pKey.equals("sortDir")) continue;
            queryParams.append(pKey).append("=").append(pVal).append("&");
        }
        model.addAttribute("queryParams", queryParams.toString());

        model.addAttribute("objectList", list);
        model.addAttribute("calDist", calDist);

        return "view/geoloc/userlocation/index";
    }


    @GetMapping(value = "/show/{id}")
    public String show(Model model, @PathVariable long id)
    {
        UserLocation object = null;
        try {
            object = service.findById(id);
        } catch (Exception ex) {
            model.addAttribute(SysMgsStr.msgKey3, SysMgsStr.msgDesc3);
        }
        model.addAttribute("object", object);
        return "view/auth/role/show";
    }

    @RequestMapping(path = "/create")
    public String create(Model model)
    {
        model.addAttribute("object", new UserLocation());
        return "view/auth/role/create";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@Valid UserLocation postObjInst, BindingResult result, Model model, RedirectAttributes redirAttrs)
    {
        if (result.hasErrors()) {
            return "view/auth/role/create";
        }
        postObjInst = service.createOrUpdate(postObjInst);
        model.addAttribute("object", postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1);

        return "redirect:/role/show/" + postObjInst.getId();
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Optional<Long> id) throws Exception
    {
        if (id.isPresent()) {
            UserLocation entity = service.getById(id.get());
            model.addAttribute("object", entity);
        } else {
            model.addAttribute("object", new UserLocation());
        }
        return "view/auth/role/edit";
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String update(UserLocation postObjInst, RedirectAttributes redirAttrs)
    {
        postObjInst = service.createOrUpdate(postObjInst);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey1, SysMgsStr.msgDesc1u);
        return "redirect:/role/show/" + postObjInst.getId();
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirAttrs) throws Exception
    {
        service.deleteById(id);
        redirAttrs.addFlashAttribute(SysMgsStr.msgKey2, SysMgsStr.msgDesc2);
        return "redirect:/role/index";
    }



}
