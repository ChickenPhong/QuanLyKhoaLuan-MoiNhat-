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
    
    void assign(int deTaiKhoaLuanSinhVienId, int giangVienId); //apiGiaovu
    
    DeTaiKhoaLuan_GiangVienHuongDan findByDeTaiKhoaLuanSinhVienId(int id);
    List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByDeTaiKhoaLuanSinhVienId(int id); //apiGiaovu
    
    //api
    List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByGiangVienHuongDanId(int giangVienId);
}
