
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.BangDiem;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class ApiAdminController {

    @Autowired
    private NguoiDungService nguoiDungService;
    
    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;
    
    @Autowired
    private DeTaiService deTaiService;
    
    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;
    
    @Autowired
    private HoiDongService hoiDongService;
    
    @Autowired
    private BangDiemService bangDiemService;

    @GetMapping("/")
    public ResponseEntity<List<NguoiDung>> getAllUsers() {
        return ResponseEntity.ok(nguoiDungService.getAllUsers());
    }

    @PostMapping(path ="/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NguoiDung> addUser(@RequestParam Map<String, String> params, 
                                             @RequestParam("avatar") MultipartFile avatar) {
        return new ResponseEntity<>(nguoiDungService.addUser(params, avatar), HttpStatus.CREATED);
    }
    
    @GetMapping("/khoa")
    public ResponseEntity<?> getAllKhoa() {
        List<String> dsKhoa = nguoiDungService.getAllKhoa();
        // Lọc null và distinct
        dsKhoa = dsKhoa.stream().filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList());
        return ResponseEntity.ok(dsKhoa);
    }
    
    @GetMapping("/khoahoc")
    public ResponseEntity<?> getKhoaHocByKhoa(@RequestParam("khoa") String khoa) {
        List<String> dsKhoaHoc = nguoiDungService.getAllKhoaHocByKhoa(khoa);
        dsKhoaHoc = dsKhoaHoc.stream().filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList());
        return ResponseEntity.ok(dsKhoaHoc);
    }
    
    // Lấy danh sách ngành theo khoa + khóa học
    @GetMapping("/nganh")
    public ResponseEntity<?> getAllNganhByKhoaVaKhoaHoc(
        @RequestParam("khoa") String khoa,
        @RequestParam("khoaHoc") String khoaHoc
    ) {
        List<String> nganhList = nguoiDungService.getAllNganhByKhoaVaKhoaHoc(khoa, khoaHoc);
        if (nganhList == null) return ResponseEntity.ok(List.of());
        nganhList = nganhList.stream().filter(x -> x != null && !x.trim().isEmpty()).distinct().collect(Collectors.toList());
        return ResponseEntity.ok(nganhList);
    }
    
    // Thống kê điểm khóa luận cho Admin (lọc theo khoa, khóa học, ngành)
    @GetMapping("/thongke_khoaluan")
    public ResponseEntity<?> thongKeKhoaLuanAdmin(
        @RequestParam("khoa") String khoa,
        @RequestParam("khoaHoc") String khoaHoc,
        @RequestParam(value = "nganh", required = false) String nganh
    ) {
        List<NguoiDung> sinhViens;
        if (nganh == null || nganh.isEmpty()) {
            sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        } else {
            sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHocVaNganh(khoa, khoaHoc, nganh);
        }
        int tongSinhVien = sinhViens.size();

        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);
        int soSinhVienThamGia = dtsvList.size();

        List<Map<String, Object>> result = new ArrayList<>();
        for (DeTaiKhoaLuan_SinhVien dtsv : dtsvList) {
            NguoiDung sv = nguoiDungService.getById(dtsv.getSinhVienId());
            DeTaiKhoaLuan dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            DeTaiKhoaLuan_HoiDong dthd = deTaiHoiDongService.findByDtsvId(dtsv.getId());
            HoiDong hd = dthd != null ? hoiDongService.getById(dthd.getHoiDongId()) : null;

            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsv.getId());
            Double diemTB = (diemList != null && !diemList.isEmpty()) ?
                diemList.stream().mapToDouble(BangDiem::getDiem).average().orElse(0.0) : null;

            Map<String, Object> map = new HashMap<>();
            map.put("tenSinhVien", sv != null ? sv.getFullname() : "");
            map.put("tenDeTai", dt != null ? dt.getTitle() : "");
            map.put("tenHoiDong", hd != null ? hd.getName() : "");
            map.put("diemTrungBinh", diemTB);

            result.add(map);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("tongSinhVien", tongSinhVien);
        res.put("soSinhVienThamGia", soSinhVienThamGia);
        res.put("dsSinhVien", result);
        return ResponseEntity.ok(res);
    }

}
