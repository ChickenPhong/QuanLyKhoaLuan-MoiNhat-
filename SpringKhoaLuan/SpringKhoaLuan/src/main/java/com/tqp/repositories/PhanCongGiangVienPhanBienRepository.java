/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import java.util.List;

public interface PhanCongGiangVienPhanBienRepository {
    List<PhanCongGiangVienPhanBien> getAll();
    PhanCongGiangVienPhanBien getById(int id);
    PhanCongGiangVienPhanBien save(PhanCongGiangVienPhanBien p); //dùng cho addPhanBien
    void delete(int id);
    void assignPhanBien(int giangVienId, int hoiDongId);
    
    List<PhanCongGiangVienPhanBien> findByGiangVienPhanBienId(int giangVienPhanBienId);
    List<PhanCongGiangVienPhanBien> findByHoiDongId(int hoiDongId);
}
