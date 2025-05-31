/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.BangDiemTongHopDTO;
import com.tqp.pojo.BangDiem;
import java.util.List;

public interface BangDiemService {
    List<BangDiem> getAll();
    BangDiem getById(int id);
    BangDiem add(BangDiem diem);
    void delete(int id);
    
    BangDiem findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(int dtsvId, int giangVienId, String tieuChi);
    BangDiem update(BangDiem diem);
    
    List<BangDiem> findByDeTaiSinhVienId(int dtsvId); //apiAdmin
    Double tinhDiemTrungBinhByDeTaiSinhVienId(int dtsvId);
    
    List<BangDiemTongHopDTO> layBangDiemTongHopTheoHoiDong(int hoiDongId);
    
    List<BangDiem> findByGiangVienAndDtsv(int giangVienId, int dtsvId);
}
