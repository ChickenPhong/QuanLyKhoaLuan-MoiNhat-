/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.HoiDong;
import com.tqp.repositories.HoiDongRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HoiDongRepositoryImpl implements HoiDongRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<HoiDong> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM HoiDong", HoiDong.class);
        return q.getResultList();
    }

    @Override
    public HoiDong getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(HoiDong.class, id);
    }

    @Override
    public HoiDong save(HoiDong hd) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(hd);
        return hd;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        HoiDong hd = s.get(HoiDong.class, id);
        if (hd != null)
            s.delete(hd);
    }
    
    //api
    @Override
    public List<HoiDong> getHoiDongByKhoa(String khoa) {
        Session s = factory.getObject().getCurrentSession();
        // Nếu cần lọc trạng thái active nữa thì bổ sung điều kiện
        Query q = s.createQuery("FROM HoiDong WHERE khoa = :khoa AND status = 'active'", HoiDong.class);
        q.setParameter("khoa", khoa);
        return q.getResultList();
    }
}
