/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import java.util.List;

public interface DeTaiHoiDongRepository {
    List<DeTaiKhoaLuan_HoiDong> getAll();
    DeTaiKhoaLuan_HoiDong getById(int id);
    DeTaiKhoaLuan_HoiDong save(DeTaiKhoaLuan_HoiDong dthd);
    void delete(int id);
    
    void assignHoiDong(int deTaiId, int hoiDongId);
    boolean isDeTaiAssigned(int deTaiId);
    DeTaiKhoaLuan_HoiDong findByDeTaiId(int deTaiId);
    long countDeTaiByHoiDongId(int hoiDongId);
    
    List<DeTaiKhoaLuan_HoiDong> findByHoiDongId(int hoiDongId);
    void lockAllByHoiDongId(int hoiDongId);
    boolean isHoiDongLocked(int hoiDongId);
}
