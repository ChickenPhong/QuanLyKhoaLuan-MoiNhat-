/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */

import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private NguoiDungService nguoiDungService;

    // Trang quản trị dành cho admin
    @GetMapping("/admin")
    public String adminView(Model model) {
        model.addAttribute("users", nguoiDungService.getAllUsers());
        return "admin"; // Trả về admin.html
    }

    // Form thêm người dùng
    @GetMapping("/admin/add-user")
    public String addUserForm() {
        return "add-user"; // Trả về form thêm người dùng
    }

    // Xử lý thêm người dùng
    @PostMapping("/admin/add-user")
    public String addUser(@RequestParam(value = "fullname") String fullname,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("role") String role,
                          @RequestParam("avatar") MultipartFile avatar,
                          @RequestParam(name = "khoa", required = false) String khoa,
                          @RequestParam(name = "khoaHoc", required = false) String khoaHoc,
                          RedirectAttributes redirectAttrs) {

        Map<String, String> params = new HashMap<>();
        params.put("fullname", fullname);
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("khoa", role.equals("ROLE_ADMIN") ? null : khoa);
        params.put("khoaHoc", role.equals("ROLE_SINHVIEN") ? khoaHoc : null);
        params.put("role", role);
        
        System.out.println("ROLE: " + role);
        System.out.println("KhoaHoc: " + khoaHoc);
        System.out.println("Đã truyền vào: " + params);

        nguoiDungService.addUser(params, avatar);
        redirectAttrs.addFlashAttribute("message", "Thêm người dùng thành công!");

        return "redirect:/admin";
    }

    // Xóa người dùng
    @PostMapping("/admin/delete-user")
    public String deleteUser(@RequestParam("userId") int id, RedirectAttributes redirectAttrs) {
        boolean result = nguoiDungService.deleteUser(id);
        redirectAttrs.addFlashAttribute("message", result ? "Xóa người dùng thành công!" : "Xóa người dùng thất bại!");
        return "redirect:/admin";
    }
}
