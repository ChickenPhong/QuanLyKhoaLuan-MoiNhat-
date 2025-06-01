/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.ThanhVienHoiDong;
import com.tqp.repositories.ThanhVienHoiDongRepository;
import com.tqp.services.ThanhVienHoiDongService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThanhVienHoiDongServiceImpl implements ThanhVienHoiDongService{
    @Autowired
    private ThanhVienHoiDongRepository repo;

    @Override
    public List<ThanhVienHoiDong> getAll() {
        return repo.getAll();
    }

    @Override
    public ThanhVienHoiDong getById(int id) {
        return repo.getById(id);
    }

    @Override
    public ThanhVienHoiDong add(ThanhVienHoiDong tv) {
        return repo.save(tv);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }
    
    @Override
    public void addThanhVien(int hoiDongId, int userId, String role) {
        ThanhVienHoiDong tv = new ThanhVienHoiDong();
        tv.setHoiDongId(hoiDongId);
        tv.setUserId(userId);
        tv.setRole(role);
        repo.save(tv);
    }
    
    @Override
    public List<ThanhVienHoiDong> getByHoiDongId(Integer hoiDongId) {
        return repo.findByHoiDongId(hoiDongId);
    }
}
