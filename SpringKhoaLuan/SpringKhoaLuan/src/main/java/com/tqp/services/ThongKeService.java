/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.ThongKeDTO;
import java.util.List;

public interface ThongKeService {
    
    List<String> getAllKhoaHoc(); // Lấy các khóa học (năm)
    List<String> getAllKhoa(); // Lấy danh sách các khoa
    List<ThongKeDTO> thongKeLoc(String khoaHoc, String khoa); // Lọc theo khoa/khoaHoc
}
