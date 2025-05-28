/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.repositories.DeTaiRepository;
import com.tqp.services.DeTaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class DeTaiServiceImpl implements DeTaiService{
    @Autowired
    private DeTaiRepository deTaiRepo;

    @Override
    public List<DeTaiKhoaLuan> getAllDeTai() {
        return this.deTaiRepo.getAll();
    }

    @Override
    public List<DeTaiKhoaLuan> getByKhoaAndStatus(String khoa, String status) {
        return deTaiRepo.findByKhoaAndStatus(khoa, status);
    }

    @Override
    public DeTaiKhoaLuan getDeTaiById(int id) {
        return deTaiRepo.getById(id);
    }

    @Override
    public DeTaiKhoaLuan addDeTai(DeTaiKhoaLuan deTai) {
        return deTaiRepo.save(deTai);
    }

    @Override
    public boolean deleteDeTai(int id) {
        try {
            deTaiRepo.delete(id);  // Xóa theo id
            return true;  // Nếu thành công, trả về true
        } catch (Exception e) {
            return false;  // Nếu có lỗi, trả về false
        }
    }
    
    @Override
    public DeTaiKhoaLuan updateDeTai(int id, DeTaiKhoaLuan deTai) {
        System.out.println("UPDATE ID URL = " + id);
        System.out.println("UPDATE ID BODY = " + deTai.getId());
        DeTaiKhoaLuan existing = deTaiRepo.getById(id);
        if (existing != null) {
            existing.setTitle(deTai.getTitle());
            existing.setKhoa(deTai.getKhoa());
            existing.setStatus(deTai.getStatus());
            // Cập nhật các trường khác nếu có
            return deTaiRepo.update(existing);
        }
        return null;  // Hoặc ném exception nếu cần
    }
}
