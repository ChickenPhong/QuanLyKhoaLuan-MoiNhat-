/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.services.DeTaiHoiDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detai-hoidong")
public class ApiDeTaiHoiDongController {
    @Autowired
    private DeTaiHoiDongService service;

    @GetMapping("/")
    public ResponseEntity<List<DeTaiKhoaLuan_HoiDong>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeTaiKhoaLuan_HoiDong> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<DeTaiKhoaLuan_HoiDong> create(@RequestBody DeTaiKhoaLuan_HoiDong dthd) {
        return ResponseEntity.ok(service.add(dthd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
