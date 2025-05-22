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
import com.tqp.pojo.TieuChi;
import com.tqp.services.NguoiDungService;
import com.tqp.services.TieuChiService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tieuchi")
public class TieuChiController {
    @Autowired
    private TieuChiService tieuChiService;
    
    @Autowired
    private NguoiDungService nguoiDungService;


    @GetMapping
    public String index(Model model) {
        model.addAttribute("tieuchis", tieuChiService.getAll());
        return "tieuchi";  // View hiển thị danh sách tiêu chí + form thêm
    }

    @PostMapping("/add")
    public String addTieuChi(@ModelAttribute TieuChi tieuChi,  Principal principal) {
        // Lấy user hiện tại
        NguoiDung user = nguoiDungService.getByUsername(principal.getName());

        // Gán khoa của giáo vụ
        tieuChi.setKhoa(user.getKhoa());

        // Trạng thái mặc định
        tieuChi.setStatus("active");

        tieuChiService.addTieuChi(tieuChi);

        return "redirect:/tieuchi";
    }
}
