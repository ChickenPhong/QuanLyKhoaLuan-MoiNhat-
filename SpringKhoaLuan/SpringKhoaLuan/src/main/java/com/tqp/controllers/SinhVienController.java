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
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class SinhVienController {
     @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;

    // Trang chính dành cho sinh viên
    @GetMapping("/sinhvien")
    public String sinhVienView(Model model, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());

        // Lấy danh sách đề tài mà sinh viên này đang thực hiện (nếu có)
        //var deTais = deTaiSinhVienService.getDeTaiBySinhVienId(user.getId());

        //model.addAttribute("deTais", deTais);
        model.addAttribute("sinhVien", user);

        return "sinhvien"; // Trả về file sinhvien.html
    }
}
