/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.NguoiDung;
import com.tqp.services.DeTaiService;
import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Autowired
    private DeTaiService deTaiService;
    
    @Autowired
    private NguoiDungService nguoiDungService;

    @RequestMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            NguoiDung user = nguoiDungService.getByUsername(username);

            if (user != null) {
                String role = user.getRole().trim();

                if (role.equalsIgnoreCase("ROLE_ADMIN"))
                    return "redirect:/admin";
                else if (role.equalsIgnoreCase("ROLE_GIAOVU"))
                    return "redirect:/khoaluan";
                else if (role.equalsIgnoreCase("ROLE_GIANGVIEN"))
                    return "redirect:/giangvien";
                else if (role.equalsIgnoreCase("ROLE_SINHVIEN"))
                    return "redirect:/sinhvien";
            }
            System.out.println("Redirecting user with role: " + user.getRole());
            System.out.println("Login user: " + authentication.getName());
            System.out.println("Role: " + user.getRole());
        }
        

        model.addAttribute("deTais", deTaiService.getAllDeTai());
        return "index";
    }
}
