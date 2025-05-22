/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */

import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.services.DeTaiSinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detai-sinhvien")
public class ApiDeTaiSinhVienController {
    @Autowired
    private DeTaiSinhVienService service;

    @GetMapping("/")
    public ResponseEntity<List<DeTaiKhoaLuan_SinhVien>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeTaiKhoaLuan_SinhVien> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<DeTaiKhoaLuan_SinhVien> create(@RequestBody DeTaiKhoaLuan_SinhVien dtsv) {
        return ResponseEntity.ok(service.add(dtsv));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
