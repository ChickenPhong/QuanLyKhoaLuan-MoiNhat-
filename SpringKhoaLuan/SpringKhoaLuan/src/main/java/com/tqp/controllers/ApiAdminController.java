
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.NguoiDung;
import com.tqp.services.NguoiDungService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class ApiAdminController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/")
    public ResponseEntity<List<NguoiDung>> getAllUsers() {
        return ResponseEntity.ok(nguoiDungService.getAllUsers());
    }

    @PostMapping(path ="/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NguoiDung> addUser(@RequestParam Map<String, String> params, 
                                             @RequestParam("avatar") MultipartFile avatar) {
        return new ResponseEntity<>(nguoiDungService.addUser(params, avatar), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean success = nguoiDungService.deleteUser(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).body("Xóa thất bại");
        }
    }

}
