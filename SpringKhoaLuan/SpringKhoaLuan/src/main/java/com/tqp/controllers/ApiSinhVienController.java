/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sinhvien")
public class ApiSinhVienController {
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;
    @Autowired
    private DeTaiHuongDanService deTaiHuongDanService;
    @Autowired
    private DeTaiService deTaiService;

    // API: Lấy đề tài & giảng viên hướng dẫn cho sinh viên đang đăng nhập
    @GetMapping("/detai-huongdan")
    public ResponseEntity<?> getDeTaiVaGiangVien(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Không có principal");

        String username = principal.getName();
        NguoiDung sv = nguoiDungService.getByUsername(username);
        if (sv == null)
            return ResponseEntity.status(401).body("Không tìm thấy sinh viên");

        // Lấy đề tài sinh viên này được giao (giả sử mỗi sinh viên chỉ có 1 đề tài)
        DeTaiKhoaLuan_SinhVien dtsv = deTaiSinhVienService.findBySinhVienId(sv.getId());
        if (dtsv == null)
            return ResponseEntity.ok(new HashMap<>()); // Chưa có đề tài

        DeTaiKhoaLuan deTai = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());

        // Lấy giảng viên hướng dẫn
        DeTaiKhoaLuan_GiangVienHuongDan huongDan = deTaiHuongDanService.findByDeTaiKhoaLuanSinhVienId(dtsv.getId());
        NguoiDung giangVien = (huongDan != null)
            ? nguoiDungService.getById(huongDan.getGiangVienHuongDanId())
            : null;

        Map<String, Object> resp = new HashMap<>();
        resp.put("deTai", deTai != null ? deTai.getTitle() : null);
        resp.put("giangVien", giangVien != null ? giangVien.getFullname() : null);
        resp.put("giangVienEmail", giangVien != null ? giangVien.getEmail() : null);
        resp.put("khoaHoc", sv.getKhoaHoc());
        return ResponseEntity.ok(resp);
    }
}
