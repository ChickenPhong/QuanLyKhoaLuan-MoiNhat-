/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.ThanhVienHoiDong;
import java.util.List;

public interface ThanhVienHoiDongService {
    List<ThanhVienHoiDong> getAll();
    ThanhVienHoiDong getById(int id);
    ThanhVienHoiDong add(ThanhVienHoiDong tv);
    void delete(int id);
    
    void addThanhVien(int hoiDongId, int userId, String role);
    List<ThanhVienHoiDong> getByHoiDongId(Integer hoiDongId);
}
