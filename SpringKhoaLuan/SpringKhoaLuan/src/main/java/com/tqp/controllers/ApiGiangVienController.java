/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.BangDiem;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
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

    // Lấy danh sách đề tài và sinh viên được chấm điểm bởi giảng viên phản biện
    @GetMapping("/phanbien/danhsach")
    public ResponseEntity<?> getDanhSachChamDiem(Principal principal) {
        // Lấy username từ principal (được thiết lập từ token)
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null)
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên");

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
                result.add(item);
            }
        }
        return ResponseEntity.ok(result);
    }

    // Lấy tiêu chí chấm điểm (giữ nguyên, ai cũng lấy được tiêu chí)
    @GetMapping("/tieuchi")
    public ResponseEntity<?> getTieuChi() {
        return ResponseEntity.ok(tieuChiService.getAll());
    }

    // Lấy điểm đã chấm cho đề tài của giảng viên này
    @GetMapping("/phanbien/diem")
    public ResponseEntity<?> getDiem(@RequestParam int dtsvId, Principal principal) {
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null)
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên");

        int giangVienPhanBienId = gv.getId();

        var tieuChis = tieuChiService.getAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (var tc : tieuChis) {
            var diem = bangDiemService.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(dtsvId, giangVienPhanBienId, tc.getTenTieuChi());
            Map<String, Object> item = new HashMap<>();
            item.put("tieuChi", tc.getTenTieuChi());
            item.put("diem", diem != null ? diem.getDiem() : null);
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    // Lưu điểm cho từng tiêu chí
    @PostMapping("/phanbien/luudiem")
    public ResponseEntity<?> saveDiem(@RequestBody Map<String, Object> payload, Principal principal) {
        String username = principal.getName();
        var gv = nguoiDungService.getByUsername(username);
        if (gv == null)
            return ResponseEntity.status(401).body("Không tìm thấy giảng viên");

        int giangVienPhanBienId = gv.getId();

        int dtsvId = (int) payload.get("dtsvId");
        Map<String, Object> diemMapRaw = (Map<String, Object>) payload.get("diemMap");
        Map<String, Float> diemMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : diemMapRaw.entrySet()) {
            Float diem;
            if (entry.getValue() instanceof Integer) {
                diem = ((Integer) entry.getValue()).floatValue();
            } else if (entry.getValue() instanceof Double) {
                diem = ((Double) entry.getValue()).floatValue();
            } else if (entry.getValue() instanceof Float) {
                diem = (Float) entry.getValue();
            } else {
                diem = Float.parseFloat(entry.getValue().toString());
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
