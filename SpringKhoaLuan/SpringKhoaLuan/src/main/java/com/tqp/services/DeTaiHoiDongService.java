/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import java.util.List;

public interface DeTaiHoiDongService {
    List<DeTaiKhoaLuan_HoiDong> getAll();
    DeTaiKhoaLuan_HoiDong getById(int id);
    DeTaiKhoaLuan_HoiDong add(DeTaiKhoaLuan_HoiDong dthd);
    void delete(int id);
    
    void assignHoiDong(int detaikhoaluanSinhVienId, int hoiDongId); //apiGiaovu
    boolean isDeTaiAssigned(int deTaiId); //apiGiaovu
    DeTaiKhoaLuan_HoiDong findByDeTaiId(int deTaiId);
    long countDeTaiByHoiDongId(int hoiDongId);
    
    List<DeTaiKhoaLuan_HoiDong> findByHoiDongId(int hoiDongId);
    void lockAllByHoiDongId(int hoiDongId);
    boolean isHoiDongLocked(int hoiDongId);
    
    List<DeTaiKhoaLuan> findDeTaiByHoiDongId(int hoiDongId);
    DeTaiKhoaLuan_HoiDong findByDtsvId(int dtsvId); //apiGiaovu
    
    //api
    List<DeTaiKhoaLuan_HoiDong> findByDtsvIds(List<Integer> dtsvIds);  //apiAdmin, apiGiaovu
    int lockAllByHoiDongIdAndDtsvIds(int hoiDongId, List<Integer> dtsvIds); //apiGiaovu
}
