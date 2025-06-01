/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.HoiDongWithMembersDTO;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import com.tqp.pojo.ThanhVienHoiDong;
import com.tqp.services.EmailService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import com.tqp.services.ThanhVienHoiDongService;
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

    @Autowired
    private ThanhVienHoiDongService thanhVienHoiDongService;

    @Autowired
    private PhanCongGiangVienPhanBienService phanCongService;
    
    @Autowired
    private EmailService emailService;

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

        // Gán các trường bổ sung
        hd.setKhoa(user.getKhoa());
        hd.setStatus("active");

        HoiDong savedHoiDong = hoiDongService.addHoiDong(hd);

        // Tạo Chủ tịch
        ThanhVienHoiDong chuTich = new ThanhVienHoiDong();
        chuTich.setHoiDongId(savedHoiDong.getId());
        chuTich.setUserId(hd.getChuTichUserId());
        chuTich.setRole("chu_tich");
        thanhVienHoiDongService.add(chuTich);

        // Tạo Thư ký
        ThanhVienHoiDong thuKy = new ThanhVienHoiDong();
        thuKy.setHoiDongId(savedHoiDong.getId());
        thuKy.setUserId(hd.getThuKyUserId());
        thuKy.setRole("thu_ky");
        thanhVienHoiDongService.add(thuKy);

        // Tạo thành viên phản biện
        for (Integer gvId : hd.getGiangVienPhanBienIds()) {
            ThanhVienHoiDong phanBien = new ThanhVienHoiDong();
            phanBien.setHoiDongId(savedHoiDong.getId());
            phanBien.setUserId(gvId);
            phanBien.setRole("phan_bien");  
            thanhVienHoiDongService.add(phanBien);
        }

        // Tạo các thành viên phản biện trong bảng PhanCongGiangVienPhanBien
        for (Integer gvId : hd.getGiangVienPhanBienIds()) {
            PhanCongGiangVienPhanBien p = new PhanCongGiangVienPhanBien();
            p.setHoiDongId(savedHoiDong.getId());
            p.setGiangVienPhanBienId(gvId);
            phanCongService.add(p);
        }
        
        // Gửi email cho giảng viên phản biện ---
        for (Integer gvId : hd.getGiangVienPhanBienIds()) {
            NguoiDung gv = nguoiDungService.getById(gvId);
            if (gv != null && gv.getEmail() != null && !gv.getEmail().isEmpty()) {
                emailService.sendEmail(
                    gv.getEmail(),
                    "Thông báo phân công phản biện",
                    "Chào thầy/cô " + gv.getUsername() + ",\nThầy/cô đã được phân công làm giảng viên phản biện cho hội đồng \"" + savedHoiDong.getName() + "\"."
                );
            }
        }

        return ResponseEntity.ok(savedHoiDong);
    }
    
    @GetMapping("/with-members")
    public ResponseEntity<List<HoiDongWithMembersDTO>> getHoiDongWithMembers(Principal principal) {
        NguoiDung user = nguoiDungService.getByUsername(principal.getName());
        
        List<HoiDongWithMembersDTO> list = hoiDongService.getHoiDongWithMembersByKhoa(user.getKhoa());
        return ResponseEntity.ok(list);
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
