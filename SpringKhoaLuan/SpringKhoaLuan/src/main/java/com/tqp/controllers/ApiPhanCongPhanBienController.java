/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phanbien")
public class ApiPhanCongPhanBienController {
    @Autowired
    private PhanCongGiangVienPhanBienService service;

    @GetMapping("/")
    public ResponseEntity<List<PhanCongGiangVienPhanBien>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhanCongGiangVienPhanBien> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<PhanCongGiangVienPhanBien> create(@RequestBody PhanCongGiangVienPhanBien p) {
        return ResponseEntity.ok(service.add(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
