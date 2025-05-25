/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import java.util.List;

public interface DeTaiSinhVienRepository {
    List<DeTaiKhoaLuan_SinhVien> getAll();
    DeTaiKhoaLuan_SinhVien getById(int id);
    DeTaiKhoaLuan_SinhVien save(DeTaiKhoaLuan_SinhVien dtsv);
    void delete(int id);
    boolean isSinhVienDaXepDeTai(int sinhVienId);
    DeTaiKhoaLuan_SinhVien findBySinhVienId(int sinhVienId);
    DeTaiKhoaLuan_SinhVien findByDeTaiId(int deTaiId);
    
    List<DeTaiKhoaLuan_SinhVien> getByKhoaVaKhoaHoc(String khoa, String khoaHoc);
    //api
    List<DeTaiKhoaLuan_SinhVien> findBySinhVienIds(List<Integer> sinhVienIds);
}
