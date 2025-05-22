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
import com.tqp.services.ThongKeService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tqp.dto.ThongKeDTO;

@Controller
public class ThongKeController {
    @Autowired
    private ThongKeService thongKeService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/thongke")
    public String viewThongKe(
        @RequestParam(value = "khoaHoc", required = false) String khoaHoc,
        @RequestParam(value = "khoa", required = false) String khoa,
        Model model,
        Principal principal
    ) {
        List<String> allKhoaHoc = thongKeService.getAllKhoaHoc();
        List<String> allKhoa = thongKeService.getAllKhoa();

        model.addAttribute("allKhoaHoc", allKhoaHoc);
        model.addAttribute("allKhoa", allKhoa);
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("khoa", khoa);

        List<ThongKeDTO> thongKeList;
        var user = nguoiDungService.getByUsername(principal.getName());
        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            thongKeList = thongKeService.thongKeLoc(khoaHoc, khoa);
        } else if (user.getRole().equals("ROLE_GIAOVU")) {
            // Giáo vụ chỉ xem được khoa của mình
            thongKeList = thongKeService.thongKeLoc(khoaHoc, user.getKhoa());
            model.addAttribute("khoa", user.getKhoa());
        } else {
            thongKeList = List.of();
        }
        model.addAttribute("thongKeList", thongKeList);
        return "thongke";
    }

}
