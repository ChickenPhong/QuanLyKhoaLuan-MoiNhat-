/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.NguoiDung;
import com.tqp.pojo.TieuChi;
import com.tqp.services.NguoiDungService;
import com.tqp.services.TieuChiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/tieuchi")
public class ApiTieuChiController {

    @Autowired
    private TieuChiService tieuChiService;

    @Autowired
    private NguoiDungService nguoiDungService;

    // Lấy danh sách tiêu chí (GET /api/tieuchi)
    @GetMapping
    public ResponseEntity<List<TieuChi>> getAll() {
        List<TieuChi> list = tieuChiService.getAll();
        return ResponseEntity.ok(list);
    }

    // Tạo tiêu chí mới (POST /api/tieuchi/add)
    @PostMapping(path ="/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TieuChi> addTieuChi(@RequestParam Map<String, String> params, Principal principal) {
        String tenTieuChi = params.get("tenTieuChi");
    if (tenTieuChi == null || tenTieuChi.trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
    }
    if (principal == null || principal.getName() == null || principal.getName().trim().isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String username = principal.getName();
    NguoiDung user = nguoiDungService.getByUsername(username);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    TieuChi tieuChi = new TieuChi();
    tieuChi.setTenTieuChi(tenTieuChi.trim());
    tieuChi.setKhoa(user.getKhoa());
    tieuChi.setStatus("active");

    TieuChi saved = tieuChiService.addTieuChi(tieuChi);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
