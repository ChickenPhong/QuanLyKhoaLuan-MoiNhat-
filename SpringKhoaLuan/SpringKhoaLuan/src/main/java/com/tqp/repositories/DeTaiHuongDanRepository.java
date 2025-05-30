/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import java.util.List;

public  interface DeTaiHuongDanRepository {
    List<DeTaiKhoaLuan_GiangVienHuongDan> getAll();
    DeTaiKhoaLuan_GiangVienHuongDan getById(int id);
    DeTaiKhoaLuan_GiangVienHuongDan save(DeTaiKhoaLuan_GiangVienHuongDan d);
    void delete(int id);
    DeTaiKhoaLuan_GiangVienHuongDan findByDeTaiKhoaLuanSinhVienId(int id);
    List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByDeTaiKhoaLuanSinhVienId(int id);
    
    //api
    List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByGiangVienHuongDanId(int giangVienId);
}
