/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.BangDiem;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import com.tqp.services.TieuChiService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tran Quoc Phong
 */
@RestController
@RequestMapping("/api/giangvien")
public class ApiGiangVienController {

    @Autowired
    private PhanCongGiangVienPhanBienService phanCongService;
    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;
    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;
    @Autowired
    private DeTaiService deTaiService;
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private TieuChiService tieuChiService;
    @Autowired
    private BangDiemService bangDiemService;
    @Autowired
    private HoiDongService hoiDongService;
    @Autowired
    private DeTaiHuongDanService deTaiGVHuongDanService;

    @GetMapping("/huongdan/danhsach")
    public ResponseEntity<?> getDanhSachHuongDan(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Không có principal – token không được chấp nhận");

        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null)
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên: " + username);

        int giangVienId = gv.getId();

        // Lấy các bản ghi hướng dẫn của GV này
        var huongDans = deTaiGVHuongDanService.findAllByGiangVienHuongDanId(giangVienId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (var hd : huongDans) {
            var dtsv = deTaiSinhVienService.getById(hd.getDeTaiKhoaLuanSinhVienId());
            if (dtsv == null) continue;
            var sv = nguoiDungService.getById(dtsv.getSinhVienId());
            var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            Map<String, Object> item = new HashMap<>();
            item.put("svId", sv.getId());
            item.put("fullname", sv.getFullname());
            item.put("email", sv.getEmail());
            item.put("khoaHoc", sv.getKhoaHoc());
            item.put("deTai", dt != null ? dt.getTitle() : "");
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // Lấy danh sách đề tài và sinh viên được chấm điểm bởi giảng viên phản biện
    @GetMapping("/phanbien/danhsach")
    public ResponseEntity<?> getDanhSachChamDiem(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Không có principal – token không được chấp nhận");
        }
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null) {
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên: " + username);
        }

        int giangVienPhanBienId = gv.getId();

        var phanCongs = phanCongService.findByGiangVienPhanBienId(giangVienPhanBienId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (var pc : phanCongs) {
            int hoiDongId = pc.getHoiDongId();
            var deTaiHoiDongs = deTaiHoiDongService.findByHoiDongId(hoiDongId);

            for (var dthd : deTaiHoiDongs) {
                var dtsv = deTaiSinhVienService.getById(dthd.getDeTaiKhoaLuanSinhVienId());
                var deTai = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
                var sinhVien = nguoiDungService.getById(dtsv.getSinhVienId());

                Map<String, Object> item = new HashMap<>();
                item.put("dtsvId", dtsv.getId());
                item.put("deTaiId", deTai.getId());
                item.put("deTaiTitle", deTai.getTitle());
                item.put("sinhVienId", sinhVien.getId());
                item.put("sinhVienTen", sinhVien.getFullname());
                item.put("hoiDongId", hoiDongId);
                item.put("hoiDongName", hoiDongService.getById(hoiDongId).getName());
                item.put("isLocked", Boolean.TRUE.equals(dthd.getLocked())); // Thêm trạng thái locked để FE nhận biết
                item.put("khoa", deTai.getKhoa());
                item.put("khoaHoc", sinhVien.getKhoaHoc());
                result.add(item);
            }
        }
        return ResponseEntity.ok(result);
    }

    // Lấy tiêu chí chấm điểm theo khoa
    @GetMapping("/tieuchi")
    public ResponseEntity<?> getTieuChi(@RequestParam(name = "khoa", required = false) String khoa) {
        if (khoa == null || khoa.isBlank())
            return ResponseEntity.ok(tieuChiService.getAll());
        // Nếu có truyền khoa, lọc theo khoa
        return ResponseEntity.ok(tieuChiService.getByKhoa(khoa));
    }

    // Lấy điểm đã chấm cho đề tài của giảng viên này
    @GetMapping("/phanbien/diem")
    public ResponseEntity<?> getDiem(@RequestParam(name = "dtsvId") int dtsvId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Không có thông tin người dùng");
        }
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null) {
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên");
        }
        int giangVienPhanBienId = gv.getId();

        // Lấy trạng thái locked của đề tài
        var dthd = deTaiHoiDongService.findByDtsvId(dtsvId); 
        boolean isLocked = dthd != null && Boolean.TRUE.equals(dthd.getLocked());

        var tieuChis = tieuChiService.getAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (var tc : tieuChis) {
            var diem = bangDiemService.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(dtsvId, giangVienPhanBienId, tc.getTenTieuChi());
            Map<String, Object> item = new HashMap<>();
            item.put("tieuChi", tc.getTenTieuChi());
            item.put("diem", diem != null ? diem.getDiem() : null);
            result.add(item);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("list", result);
        resp.put("isLocked", isLocked);
        return ResponseEntity.ok(resp);
    }

    // Lưu điểm cho từng tiêu chí
    @PostMapping("/phanbien/luudiem")
    public ResponseEntity<?> saveDiem(@RequestBody Map<String, Object> payload, Principal principal) {
        System.out.println(">>> Principal: " + principal);
        if (principal == null) {
            return ResponseEntity.status(401).body("Không có thông tin principal!");
        }
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null) {
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên");
        }
        int giangVienPhanBienId = gv.getId();

        int dtsvId = (int) payload.get("dtsvId");

        // Lấy trạng thái locked của đề tài
        var dthd = deTaiHoiDongService.findByDtsvId(dtsvId);
        boolean isLocked = dthd != null && Boolean.TRUE.equals(dthd.getLocked());
        if (isLocked) {
            return ResponseEntity.status(403).body("Đề tài đã bị khóa, không thể lưu điểm!");
        }

        Map<String, Object> diemMapRaw = (Map<String, Object>) payload.get("diemMap");
        Map<String, Float> diemMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : diemMapRaw.entrySet()) {
            Float diem = null;
            Object val = entry.getValue();
            if (val == null || val.toString().trim().isEmpty()) {
                // Nếu giá trị rỗng, bỏ qua hoặc set giá trị mặc định 
                continue; // hoặc diem = null; 
            } else if (val instanceof Integer) {
                diem = ((Integer) val).floatValue();
            } else if (val instanceof Double) {
                diem = ((Double) val).floatValue();
            } else if (val instanceof Float) {
                diem = (Float) val;
            } else {
                diem = Float.parseFloat(val.toString());
            }
            diemMap.put(entry.getKey(), diem);
        }

        for (Map.Entry<String, Float> entry : diemMap.entrySet()) {
            var tc = entry.getKey();
            float diem = entry.getValue();
            var existing = bangDiemService.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(dtsvId, giangVienPhanBienId, tc);
            if (existing != null) {
                existing.setDiem(diem);
                bangDiemService.update(existing);
            } else {
                BangDiem bd = new BangDiem();
                bd.setDeTaiKhoaLuanSinhVienId(dtsvId);
                bd.setGiangVienPhanBienId(giangVienPhanBienId);
                bd.setTieuChi(tc);
                bd.setDiem(diem);
                bangDiemService.add(bd);
            }
        }
        return ResponseEntity.ok("Lưu điểm thành công");
    }
}
