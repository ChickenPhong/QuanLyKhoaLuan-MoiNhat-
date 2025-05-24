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
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    
    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;
    
    @Autowired
    private HoiDongService hoiDongService;
    
    @GetMapping("/khoahoc")
    public ResponseEntity<?> getKhoaHocList(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        var user = nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        String khoa = user.getKhoa();
        if (khoa == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng chưa khai báo khoa");
        }
        List<String> khoaHocList = nguoiDungService.getAllKhoaHocByKhoa(khoa);
        if (khoaHocList == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không tìm thấy khóa học nào");
        }
        return ResponseEntity.ok(khoaHocList);
    }

    // 1. Lấy danh sách sinh viên theo khoa và khóa học
    @GetMapping("/sinhviens")
    public ResponseEntity<?> getSinhVienByKhoaVaKhoaHoc(@RequestParam("khoaHoc") String khoaHoc, Principal principal ) {

        if (principal == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        NguoiDung user = this.nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        String khoa = user.getKhoa();
        List<NguoiDung> svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        return ResponseEntity.ok(svList);
    }

    // 2. Xếp đề tài cho sinh viên khóa học (theo khoa của người dùng đăng nhập)
    @PostMapping("/xepdetai")
    public ResponseEntity<?> xepDeTaiChoSinhVien(
        @RequestParam("khoaHoc") String khoaHoc,
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
    
    @GetMapping("/giaodetai")
    public ResponseEntity<?> getGiaoDeTai(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách đề tài có sinh viên theo khoa và khóa
        var deTais = deTaiService.getByKhoa(khoa).stream()
            .filter(dt -> {
                var dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
                return dtsv != null && khoaHoc.equals(nguoiDungService.getById(dtsv.getSinhVienId()).getKhoaHoc());
            })
            .collect(Collectors.toList());

        Map<Integer, String> svMap = new HashMap<>();
        for (var dt : deTais) {
            var dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
            if (dtsv != null) {
                var sv = nguoiDungService.getById(dtsv.getSinhVienId());
                svMap.put(dt.getId(), sv.getUsername());
            }
        }

        Map<Integer, String> hdMap = new HashMap<>();
        for (var dt : deTais) {
            var hdh = deTaiHoiDongService.findByDeTaiId(dt.getId());
            if (hdh != null) {
                var hd = hoiDongService.getById(hdh.getHoiDongId());
                hdMap.put(dt.getId(), hd.getName());
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("deTais", deTais);
        res.put("svMap", svMap);
        res.put("hdMap", hdMap);

        return ResponseEntity.ok(res);
    }
}


