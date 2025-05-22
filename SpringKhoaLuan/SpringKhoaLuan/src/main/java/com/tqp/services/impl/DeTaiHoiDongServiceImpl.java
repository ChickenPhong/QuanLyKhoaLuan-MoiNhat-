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
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.repositories.DeTaiHoiDongRepository;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeTaiHoiDongServiceImpl implements DeTaiHoiDongService{
    @Autowired
    private DeTaiHoiDongRepository repo;
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private DeTaiService deTaiService;

    @Override
    public List<DeTaiKhoaLuan_HoiDong> getAll() {
        return repo.getAll();
    }

    @Override
    public DeTaiKhoaLuan_HoiDong getById(int id) {
        return repo.getById(id);
    }

    @Override
    public DeTaiKhoaLuan_HoiDong add(DeTaiKhoaLuan_HoiDong dthd) {
        return repo.save(dthd);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }
    
    @Override
    public void assignHoiDong(int deTaiId, int hoiDongId) {
        repo.assignHoiDong(deTaiId, hoiDongId);
    }

    @Override
    public boolean isDeTaiAssigned(int deTaiId) {
        return repo.isDeTaiAssigned(deTaiId);
    }
    
    @Override
    public DeTaiKhoaLuan_HoiDong findByDeTaiId(int deTaiId) {
        return repo.findByDeTaiId(deTaiId);
    }
    
    @Override
    public long countDeTaiByHoiDongId(int hoiDongId) {
        return repo.countDeTaiByHoiDongId(hoiDongId);
    }
    
     @Override
    public List<DeTaiKhoaLuan_HoiDong> findByHoiDongId(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "FROM DeTaiKhoaLuan_HoiDong WHERE hoiDongId = :hoiDongId";
        Query<DeTaiKhoaLuan_HoiDong> q = s.createQuery(hql, DeTaiKhoaLuan_HoiDong.class);
        q.setParameter("hoiDongId", hoiDongId);
        return q.getResultList();
    }
    
    @Override
    public void lockAllByHoiDongId(int hoiDongId) {
        repo.lockAllByHoiDongId(hoiDongId);
    }
    
    @Override
    public boolean isHoiDongLocked(int hoiDongId) {
        return repo.isHoiDongLocked(hoiDongId);
    }
    
    @Override
    public List<DeTaiKhoaLuan> findDeTaiByHoiDongId(int hoiDongId) {
        List<DeTaiKhoaLuan_HoiDong> list = this.findByHoiDongId(hoiDongId); // đã có sẵn hàm này
        List<DeTaiKhoaLuan> result = new ArrayList<>();
        for (DeTaiKhoaLuan_HoiDong dthd : list) {
            DeTaiKhoaLuan dt = deTaiService.getDeTaiById(dthd.getDeTaiKhoaLuanId());
            if (dt != null)
                result.add(dt);
        }
        return result;
    }
}
