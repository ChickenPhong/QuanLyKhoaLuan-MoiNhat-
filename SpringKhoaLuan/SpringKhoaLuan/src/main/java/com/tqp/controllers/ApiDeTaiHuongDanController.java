/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import com.tqp.services.DeTaiHuongDanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/huongdan")
public class ApiDeTaiHuongDanController {
    @Autowired
    private DeTaiHuongDanService service;

    @GetMapping("/")
    public ResponseEntity<List<DeTaiKhoaLuan_GiangVienHuongDan>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeTaiKhoaLuan_GiangVienHuongDan> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<DeTaiKhoaLuan_GiangVienHuongDan> create(@RequestBody DeTaiKhoaLuan_GiangVienHuongDan d) {
        return ResponseEntity.ok(service.add(d));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
