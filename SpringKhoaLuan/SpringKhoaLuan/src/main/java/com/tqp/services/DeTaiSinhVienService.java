/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import java.util.List;

public interface DeTaiSinhVienService {
    List<DeTaiKhoaLuan_SinhVien> getAll();
    DeTaiKhoaLuan_SinhVien getById(int id); //apiGiaovu
    DeTaiKhoaLuan_SinhVien add(DeTaiKhoaLuan_SinhVien dtsv);
    void delete(int id);
    
    DeTaiKhoaLuan_SinhVien assign(int sinhVienId, int deTaiId); //apiGiaovu
    boolean isSinhVienDaXepDeTai(int sinhVienId); //apiGiaovu
    DeTaiKhoaLuan_SinhVien findBySinhVienId(int sinhVienId); //apiAdmin, apiGiaoVu
    DeTaiKhoaLuan_SinhVien findByDeTaiId(int deTaiId);
    
    List<DeTaiKhoaLuan_SinhVien> getByKhoaVaKhoaHoc(String khoa, String khoaHoc); //apiGiaovu
    //api
    List<DeTaiKhoaLuan_SinhVien> findBySinhVienIds(List<Integer> sinhVienIds); //apiGiaovu
}


