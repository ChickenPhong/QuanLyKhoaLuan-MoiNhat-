/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/giaovu")
public class ApiGiaoVuController {
     @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;

    @Autowired
    private DeTaiHuongDanService deTaiGVHuongDanService;
    
    @GetMapping("/khoahoc")
    public ResponseEntity<List<String>> getKhoaHocList(Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách các năm khóa của sinh viên theo khoa
        List<String> khoaHocList = nguoiDungService.getAllKhoaHocByKhoa(khoa);

        return ResponseEntity.ok(khoaHocList);
    }

    // 1. Lấy danh sách sinh viên theo khoa và khóa học
    @GetMapping("/sinhviens")
    public ResponseEntity<?> getSinhVienByKhoaVaKhoaHoc(@RequestParam Map<String, String> params ) {

        NguoiDung user = this.nguoiDungService.getByUsername(params.get("username"));
        String khoa = user.getKhoa();
        String khoaHoc = params.get("khoaHoc");

        List<NguoiDung> svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);

        return ResponseEntity.ok(svList);
    }

    // 2. Xếp đề tài cho sinh viên khóa học (theo khoa của người dùng đăng nhập)
    @PostMapping("/xepdetai")
    public ResponseEntity<?> xepDeTaiChoSinhVien(
        @RequestParam String khoaHoc,
        Principal principal) {

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        List<NguoiDung> svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<DeTaiKhoaLuan> deTaiList = deTaiService.getByKhoa(khoa);
        List<NguoiDung> giangVienList = nguoiDungService.getGiangVienByKhoa(khoa);

        // Kiểm tra sinh viên đã được xếp đề tài chưa
        boolean daXep = svList.stream()
            .anyMatch(sv -> deTaiSinhVienService.isSinhVienDaXepDeTai(sv.getId()));

        if (daXep) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Khóa " + khoaHoc + " đã được xếp danh sách trước đó!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        // Thực hiện xếp đề tài
        for (int i = 0; i < svList.size(); i++) {
            var sv = svList.get(i);
            var dt = deTaiList.get(i % deTaiList.size());
            var gv = giangVienList.get(i % giangVienList.size());

            deTaiSinhVienService.assign(sv.getId(), dt.getId());
            deTaiGVHuongDanService.assign(dt.getId(), gv.getId());
        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Đã xếp danh sách thành công cho khóa " + khoaHoc);
        return ResponseEntity.ok(res);
    }
}
