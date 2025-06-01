/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */

import com.tqp.pojo.NguoiDung;
import com.tqp.pojo.ThanhVienHoiDong;
import java.util.List;

public interface ThanhVienHoiDongRepository {
    List<ThanhVienHoiDong> getAll();
    ThanhVienHoiDong getById(int id);
    ThanhVienHoiDong save(ThanhVienHoiDong tv);
    void delete(int id);
    
    List<NguoiDung> getGiangVienByHoiDongId(int hoiDongId);
    List<ThanhVienHoiDong> findByHoiDongId(Integer hoiDongId);
}
