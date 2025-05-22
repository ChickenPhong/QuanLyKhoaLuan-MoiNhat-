
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
import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class NguoiDungController {

    // Giao diện đăng nhập
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String username = principal.getName();
        NguoiDung user = nguoiDungService.getByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để đổi mật khẩu.");
            return "redirect:/login";
        }

        String username = principal.getName();
        NguoiDung user = nguoiDungService.getByUsername(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại.");
            return "redirect:/login";
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng");
            return "redirect:/profile";  // Nếu mật khẩu cũ không đúng, chuyển về trang profile
        }

        // Kiểm tra mật khẩu mới và xác nhận có khớp không
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp");
            return "redirect:/profile";  // Nếu mật khẩu mới và xác nhận không khớp
        }

        // Kiểm tra độ dài mật khẩu mới (tuỳ chọn)
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            return "redirect:/profile";
        }

        // Băm mật khẩu mới và cập nhật vào cơ sở dữ liệu
        user.setPassword(passwordEncoder.encode(newPassword));  // Băm mật khẩu mới
        nguoiDungService.mergeUser(user);  // Cập nhật mật khẩu mới vào cơ sở dữ liệu

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        return "redirect:/profile";  // Quay lại trang thông tin cá nhân
    }
}