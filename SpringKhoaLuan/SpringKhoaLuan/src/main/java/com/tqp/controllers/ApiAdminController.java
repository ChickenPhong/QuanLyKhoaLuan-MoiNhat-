
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean success = nguoiDungService.deleteUser(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).body("Xóa thất bại");
        }
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
    
    // Thống kê điểm khóa luận cho Admin (lọc theo khoa và khóa học)
    @GetMapping("/thongke_khoaluan")
    public ResponseEntity<?> thongKeKhoaLuanAdmin(
        @RequestParam("khoa") String khoa,
        @RequestParam("khoaHoc") String khoaHoc) {

        List<NguoiDung> sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);

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

        return ResponseEntity.ok(result);
    }

}
