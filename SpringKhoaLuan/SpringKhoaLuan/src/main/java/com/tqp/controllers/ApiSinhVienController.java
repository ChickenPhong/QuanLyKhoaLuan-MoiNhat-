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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sinhvien")
public class ApiSinhVienController {

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;
    @Autowired
    private DeTaiService deTaiService;
    @Autowired
    private DeTaiHuongDanService deTaiHuongDanService;
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/detai")
    public ResponseEntity<?> getDeTaiVaGvHuongDan(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Không có thông tin người dùng"));
        }

        String username = principal.getName();
        var sv = nguoiDungService.getByUsername(username);
        if (sv == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Không tìm thấy sinh viên"));
        }

        var deTaiSinhVien = deTaiSinhVienService.findBySinhVienId(sv.getId());
        if (deTaiSinhVien == null) {
            return ResponseEntity.ok(Map.of(
                "message", "Chưa được phân công đề tài",
                "khoaHoc", sv.getKhoaHoc(),
                "deTai", null,
                "giangVienHuongDan", List.of()
            ));
        }

        var deTai = deTaiService.getDeTaiById(deTaiSinhVien.getDeTaiKhoaLuanId());
        if (deTai == null) {
            return ResponseEntity.ok(Map.of(
                "message", "Chưa có thông tin đề tài",
                "khoaHoc", sv.getKhoaHoc(),
                "deTai", null,
                "giangVienHuongDan", List.of()
            ));
        }

        var huongDans = deTaiHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(deTaiSinhVien.getId());

        List<Map<String, Object>> gvList = new ArrayList<>();
        for (var hd : huongDans) {
            var gv = nguoiDungService.getById(hd.getGiangVienHuongDanId());
            if (gv == null) continue;

            Map<String, Object> gvMap = new HashMap<>();
            gvMap.put("id", gv.getId());
            gvMap.put("fullname", gv.getFullname());
            gvMap.put("email", gv.getEmail());
            gvMap.put("avatar", gv.getAvatar());
            gvList.add(gvMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("khoaHoc", sv.getKhoaHoc());
        result.put("deTai", Map.of(
            "id", deTai.getId(),
            "title", deTai.getTitle(),
            "khoa", deTai.getKhoa()
        ));
        result.put("giangVienHuongDan", gvList);
        result.put("message", "success");

        return ResponseEntity.ok(result);
    }
}


