/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoidong")
public class ApiHoiDongController {

    @Autowired
    private HoiDongService hoiDongService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/")
    public ResponseEntity<List<HoiDong>> getAllHoiDong() {
        return ResponseEntity.ok(hoiDongService.getAllHoiDong());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoiDong> getHoiDongById(@PathVariable int id) {
        return ResponseEntity.ok(hoiDongService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<HoiDong> createHoiDong(@RequestBody HoiDong hd, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        NguoiDung user = nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        // Gán các trường bổ sung trước khi lưu
        hd.setKhoa(user.getKhoa());
        hd.setStatus("active"); // Hoặc trạng thái mặc định

        HoiDong savedHoiDong = hoiDongService.addHoiDong(hd);

        // Có thể xử lý thêm tạo các thành viên Chủ Tịch, Thư Ký, Phản Biện (nếu cần) ở đây
        return ResponseEntity.ok(savedHoiDong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoiDong(@PathVariable int id) {
        hoiDongService.deleteHoiDong(id);
        return ResponseEntity.ok().build();
    }

    // Thêm API trả danh sách giảng viên theo khoa của user đăng nhập
    @GetMapping("/giangvien")
    public ResponseEntity<List<NguoiDung>> getGiangVienTheoKhoa(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String username = principal.getName();
        NguoiDung user = nguoiDungService.getByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }
        List<NguoiDung> giangViens = nguoiDungService.getGiangVienByKhoa(user.getKhoa());
        return ResponseEntity.ok(giangViens);
    }
}
