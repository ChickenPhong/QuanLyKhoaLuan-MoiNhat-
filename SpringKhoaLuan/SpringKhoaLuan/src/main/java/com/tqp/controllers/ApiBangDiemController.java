/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.BangDiem;
import com.tqp.services.BangDiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bangdiem")
public class ApiBangDiemController {
    @Autowired
    private BangDiemService bangDiemService;

    @GetMapping("/")
    public ResponseEntity<List<BangDiem>> getAll() {
        return ResponseEntity.ok(bangDiemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BangDiem> getById(@PathVariable int id) {
        return ResponseEntity.ok(bangDiemService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<BangDiem> create(@RequestBody BangDiem diem) {
        return ResponseEntity.ok(bangDiemService.add(diem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        bangDiemService.delete(id);
        return ResponseEntity.ok().build();
    }
}
