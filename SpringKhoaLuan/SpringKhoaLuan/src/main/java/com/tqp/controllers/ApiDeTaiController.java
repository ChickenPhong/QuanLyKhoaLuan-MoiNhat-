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
import com.tqp.services.DeTaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/detai")

public class ApiDeTaiController {

    @Autowired
    private DeTaiService deTaiService;

    @GetMapping("/")
    public List<DeTaiKhoaLuan> getAll() {
        return deTaiService.getAllDeTai();
    }

    @PostMapping("/")
    public DeTaiKhoaLuan add(@RequestBody DeTaiKhoaLuan deTai) {
        return deTaiService.addDeTai(deTai);
    }

    @GetMapping("/{id}")
    public DeTaiKhoaLuan getById(@PathVariable int id) {
        return deTaiService.getDeTaiById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        System.out.println("🔥 API DELETE gọi với ID: " + id);
        deTaiService.deleteDeTai(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody DeTaiKhoaLuan deTai) {
        try {
            if (deTai.getTitle() == null || deTai.getKhoa() == null) {
                // Kiểm tra thiếu trường bắt buộc
                return ResponseEntity.badRequest().body("Thiếu trường 'title' hoặc 'khoa'.");
            }
            DeTaiKhoaLuan updated = deTaiService.updateDeTai(id, deTai);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(404).body("Không tìm thấy đề tài để cập nhật.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi cập nhật đề tài: " + e.getMessage());
        }
    }

}
