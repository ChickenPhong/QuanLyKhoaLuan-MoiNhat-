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
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.HoiDong;
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
import java.util.ArrayList;
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
    public ResponseEntity<?> getSinhVienByKhoaVaKhoaHoc(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {

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

        // Lấy danh sách detaikhoaluan_sinhvien ứng với khoa và khóa học này
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);

        // Lấy danh sách đề tài
        List<DeTaiKhoaLuan> deTais = dtsvList.stream()
                .map(dtsv -> deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId()))
                .collect(Collectors.toList());

        // Map đề tài id -> username sinh viên
        Map<Integer, String> svMap = new HashMap<>();
        for (var dtsv : dtsvList) {
            var sv = nguoiDungService.getById(dtsv.getSinhVienId());
            svMap.put(dtsv.getDeTaiKhoaLuanId(), sv.getUsername());
        }

        // Map đề tài id -> tên hội đồng (nếu đã giao)
        Map<Integer, String> hdMap = new HashMap<>();
        for (var dtsv : dtsvList) {
            var dthd = deTaiHoiDongService.findByDtsvId(dtsv.getId());
            if (dthd != null) {
                var hd = hoiDongService.getById(dthd.getHoiDongId());
                hdMap.put(dtsv.getDeTaiKhoaLuanId(), hd.getName());
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("deTais", deTais);
        res.put("svMap", svMap);
        res.put("hdMap", hdMap);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/giaodetai/giao")
    public ResponseEntity<?> giaoDeTaiNgauNhien(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        System.out.println("GIAODETAI: principal = " + principal);
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách detaikhoaluan_sinhvien ứng với khóa này
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);

        // Kiểm tra nếu tất cả dtsvId này đã có mặt trong detaikhoaluan_hoidong thì báo đã giao
        boolean daGiao = dtsvList.stream()
                .allMatch(dtsv -> deTaiHoiDongService.isDeTaiAssigned(dtsv.getId()));

        if (daGiao) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Khóa " + khoaHoc + " đã được giao đề tài cho hội đồng trước đó!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        // Lấy danh sách hội đồng
        List<HoiDong> hoiDongs = hoiDongService.getAllHoiDong();
        if (hoiDongs.isEmpty()) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Chưa có hội đồng nào để giao đề tài");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        int hdIndex = 0;
        for (var dtsv : dtsvList) {
            if (deTaiHoiDongService.isDeTaiAssigned(dtsv.getId())) {
                continue;
            }
            var hd = hoiDongs.get(hdIndex % hoiDongs.size());
            deTaiHoiDongService.assignHoiDong(dtsv.getId(), hd.getId());
            hdIndex++;
        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Đã giao đề tài ngẫu nhiên cho hội đồng thành công!");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/danhsach_thuchien")
    public ResponseEntity<?> getDanhSachThucHien(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // ✅ Lấy các bản ghi đã gán sinh viên - đề tài theo đúng khoa và khóa học
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<Map<String, Object>> result = new ArrayList<>();

        for (var dtsv : dtsvList) {
            Map<String, Object> item = new HashMap<>();

            var sv = nguoiDungService.getById(dtsv.getSinhVienId());
            item.put("id", sv.getId());
            item.put("username", sv.getUsername());
            item.put("email", sv.getEmail());

            var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            item.put("deTai", dt != null ? dt.getTitle() : "Chưa có");

            var gvs = deTaiGVHuongDanService.findAllByDeTaiId(dt.getId());
            String tenGVs = gvs.stream()
                    .map(gv -> nguoiDungService.getById(gv.getGiangVienHuongDanId()).getUsername())
                    .collect(Collectors.joining(", "));
            item.put("giangVienHuongDan", tenGVs.isEmpty() ? "Chưa có" : tenGVs);

            result.add(item);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("sinhViens", result);
        res.put("khoa", khoa);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/them_gv2")
    public ResponseEntity<?> themGiangVienThuHai(@RequestBody Map<String, Integer> body) {
        Integer sinhVienId = body.get("sinhVienId");
        if (sinhVienId == null) {
            return ResponseEntity.badRequest().body("Thiếu sinhVienId");
        }

        var dtsv = deTaiSinhVienService.findBySinhVienId(sinhVienId);
        if (dtsv == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sinh viên chưa được xếp đề tài");
        }

        var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
        var currentGVs = deTaiGVHuongDanService.findAllByDeTaiId(dt.getId());

        if (currentGVs.size() >= 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Đề tài đã có đủ 2 giảng viên hướng dẫn");
        }

        var sv = nguoiDungService.getById(sinhVienId);
        var giangViens = nguoiDungService.getGiangVienByKhoa(sv.getKhoa());

        for (NguoiDung gv : giangViens) {
            boolean isAlreadyAssigned = currentGVs.stream()
                    .anyMatch(item -> item.getGiangVienHuongDanId().equals(gv.getId()));
            if (!isAlreadyAssigned) {
                deTaiGVHuongDanService.assign(dt.getId(), gv.getId());
                return ResponseEntity.ok("Đã thêm giảng viên thứ 2 thành công");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm được giảng viên khác để gán");
    }
}
