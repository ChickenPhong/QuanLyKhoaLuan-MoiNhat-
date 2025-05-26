/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import com.tqp.repositories.PhanCongGiangVienPhanBienRepository;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhanCongGiangVienPhanBienServiceImpl implements PhanCongGiangVienPhanBienService{
    @Autowired
    private PhanCongGiangVienPhanBienRepository repo;
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<PhanCongGiangVienPhanBien> getAll() {
        return repo.getAll();
    }

    @Override
    public PhanCongGiangVienPhanBien getById(int id) {
        return repo.getById(id);
    }

    @Override
    public PhanCongGiangVienPhanBien add(PhanCongGiangVienPhanBien p) {
        return repo.save(p);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }
    
    @Override
    public void addPhanBien(int hoiDongId, int giangVienId) {
        PhanCongGiangVienPhanBien pc = new PhanCongGiangVienPhanBien();
        pc.setHoiDongId(hoiDongId);
        pc.setGiangVienPhanBienId(giangVienId);
        repo.save(pc);
    }
    
    @Override
    public void assignPhanBien(int giangVienId, int hoiDongId) {
        PhanCongGiangVienPhanBien pc = new PhanCongGiangVienPhanBien();
        pc.setHoiDongId(hoiDongId);
        pc.setGiangVienPhanBienId(giangVienId);
        repo.save(pc); // Dùng Hibernate để tránh lỗi insert thiếu dữ liệu
    }
    
    @Override
    public List<PhanCongGiangVienPhanBien> findByGiangVienPhanBienId(int giangVienPhanBienId) {
        return repo.findByGiangVienPhanBienId(giangVienPhanBienId);
    }

    @Override
    public List<PhanCongGiangVienPhanBien> findByHoiDongId(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "FROM PhanCongGiangVienPhanBien WHERE hoiDongId = :hoiDongId";
        Query q = s.createQuery(hql, PhanCongGiangVienPhanBien.class);
        q.setParameter("hoiDongId", hoiDongId);
        return q.getResultList();
    }
}
