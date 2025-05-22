/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import com.tqp.pojo.NguoiDung;
import java.util.List;

public interface DeTaiHuongDanService {
    List<DeTaiKhoaLuan_GiangVienHuongDan> getAll();
    DeTaiKhoaLuan_GiangVienHuongDan getById(int id);
    DeTaiKhoaLuan_GiangVienHuongDan add(DeTaiKhoaLuan_GiangVienHuongDan d);
    void delete(int id);
    
    void assign(int deTaiId, int giangVienId);
    
    NguoiDung findByDeTaiId(int deTaiId);
    List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByDeTaiId(int deTaiId);
}
