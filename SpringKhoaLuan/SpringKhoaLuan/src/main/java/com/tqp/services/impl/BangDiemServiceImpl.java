/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.BangDiem;
import com.tqp.repositories.BangDiemRepository;
import com.tqp.services.BangDiemService;
import java.util.List;
import com.tqp.dto.BangDiemTongHopDTO;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.NguoiDung;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BangDiemServiceImpl implements BangDiemService{
    @Autowired
    private BangDiemRepository bangDiemRepo;
    
    @Autowired
    private com.tqp.services.DeTaiHoiDongService deTaiHoiDongService;

    @Autowired
    private com.tqp.services.DeTaiSinhVienService deTaiSinhVienService;

    @Autowired
    private com.tqp.services.NguoiDungService nguoiDungService;

    @Autowired
    private com.tqp.services.DeTaiService deTaiService;
    
    @Autowired
    private com.tqp.services.HoiDongService hoiDongService;

    @Override
    public List<BangDiem> getAll() {
        return bangDiemRepo.getAll();
    }

    @Override
    public BangDiem getById(int id) {
        return bangDiemRepo.getById(id);
    }

    @Override
    public BangDiem add(BangDiem diem) {
        return bangDiemRepo.save(diem);
    }

    @Override
    public void delete(int id) {
        bangDiemRepo.delete(id);
    }
    
    @Override
    public BangDiem findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(int dtsvId, int giangVienId, String tieuChi) {
        return bangDiemRepo.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(dtsvId, giangVienId, tieuChi);
    }
    
    @Override
    public BangDiem update(BangDiem diem) {
        return bangDiemRepo.update(diem);
    }
    
    @Override
    public List<BangDiem> findByDeTaiSinhVienId(int dtsvId) {
        return bangDiemRepo.findByDeTaiSinhVienId(dtsvId);
    }

    @Override
    public Double tinhDiemTrungBinhByDeTaiSinhVienId(int dtsvId) {
        List<BangDiem> bangDiems = bangDiemRepo.findByDeTaiSinhVienId(dtsvId);
        if (bangDiems == null || bangDiems.isEmpty()) return null;
        double tong = 0.0;
        for (BangDiem bd : bangDiems) {
            tong += bd.getDiem();
        }
        return tong / bangDiems.size();
    }
    
    @Override
    public List<BangDiemTongHopDTO> layBangDiemTongHopTheoHoiDong(int hoiDongId) {
        // Lấy các đề tài thuộc hội đồng này
        List<DeTaiKhoaLuan> deTais = deTaiHoiDongService.findDeTaiByHoiDongId(hoiDongId);
        System.out.println("===> Đề tài của hội đồng " + hoiDongId + ": " + deTais.size());

        List<BangDiemTongHopDTO> result = new ArrayList<>();
        for (DeTaiKhoaLuan dt : deTais) {
            System.out.println("Đề tài id: " + dt.getId() + ", title: " + dt.getTitle());
            // Lấy sinh viên thực hiện đề tài này
            DeTaiKhoaLuan_SinhVien dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
            if (dtsv == null) continue;
            NguoiDung sv = nguoiDungService.getById(dtsv.getSinhVienId());

            // Tính điểm trung bình cho đề tài này
            Double diemTrungBinh = this.tinhDiemTrungBinhByDeTaiSinhVienId(dt.getId());

            BangDiemTongHopDTO dto = new BangDiemTongHopDTO();
            dto.setTenHoiDong(hoiDongService.getById(hoiDongId).getName());
            dto.setTenDeTai(dt.getTitle());
            dto.setTenSinhVien(sv.getUsername());
            dto.setDiemTrungBinh(diemTrungBinh);
            result.add(dto);
        }
        return result;
    }
    
    @Override
    public List<BangDiem> findByGiangVienAndDtsv(int giangVienId, int dtsvId) {
        return bangDiemRepo.findByGiangVienAndDtsv(giangVienId, dtsvId);
    }


}
