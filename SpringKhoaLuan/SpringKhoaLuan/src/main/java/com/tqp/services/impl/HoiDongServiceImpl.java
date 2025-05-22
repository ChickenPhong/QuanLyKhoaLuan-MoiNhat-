/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.repositories.HoiDongRepository;
import com.tqp.repositories.PhanCongGiangVienPhanBienRepository;
import com.tqp.repositories.ThanhVienHoiDongRepository;
import com.tqp.services.HoiDongService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoiDongServiceImpl implements HoiDongService{
    @Autowired
    private HoiDongRepository hoiDongRepo;
    
    @Autowired
    private ThanhVienHoiDongRepository tvRepo;

    @Override
    public List<HoiDong> getAllHoiDong() {
        return hoiDongRepo.getAll();
    }

    @Override
    public HoiDong getById(int id) {
        return hoiDongRepo.getById(id);
    }

    @Override
    public HoiDong addHoiDong(HoiDong hoiDong) {
        return hoiDongRepo.save(hoiDong);
    }

    @Override
    public void deleteHoiDong(int id) {
        hoiDongRepo.delete(id);
    }
    
    @Override
    public List<NguoiDung> getThanhVienHoiDong(int hoiDongId) {
        return tvRepo.getGiangVienByHoiDongId(hoiDongId);
    }
}
