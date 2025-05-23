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
    public DeTaiKhoaLuan update(@PathVariable int id, @RequestBody DeTaiKhoaLuan deTai) {
        // Cập nhật đề tài với id, dữ liệu trong deTai
        return deTaiService.updateDeTai(id, deTai);
    }
}
