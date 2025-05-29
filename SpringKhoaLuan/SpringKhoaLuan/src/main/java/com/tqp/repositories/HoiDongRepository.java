/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.HoiDong;
import java.util.List;

public interface HoiDongRepository {
    List<HoiDong> getAll();
    HoiDong getById(int id);
    HoiDong save(HoiDong hd);
    void delete(int id);
    
    //api
    List<HoiDong> getHoiDongByKhoa(String khoa);
}
