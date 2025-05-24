/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.BangDiem;
import java.util.List;

public interface BangDiemRepository {
    List<BangDiem> getAll();
    BangDiem getById(int id);
    BangDiem save(BangDiem diem);
    void delete(int id);
    
    BangDiem findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(int dtsvId, int giangVienId, String tieuChi);
    BangDiem update(BangDiem diem);
    
    List<BangDiem> findByDeTaiSinhVienId(int dtsvId);
    
    List<BangDiem> findByGiangVienAndDtsv(int giangVienId, int dtsvId);
}
