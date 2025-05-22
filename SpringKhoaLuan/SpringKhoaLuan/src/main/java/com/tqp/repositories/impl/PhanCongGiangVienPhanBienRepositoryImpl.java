/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import com.tqp.repositories.PhanCongGiangVienPhanBienRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PhanCongGiangVienPhanBienRepositoryImpl implements PhanCongGiangVienPhanBienRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private org.hibernate.SessionFactory sessionFactory;

    @Override
    public List<PhanCongGiangVienPhanBien> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM PhanCongGiangVienPhanBien", PhanCongGiangVienPhanBien.class);
        return q.getResultList();
    }

    @Override
    public PhanCongGiangVienPhanBien getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(PhanCongGiangVienPhanBien.class, id);
    }

    @Override
    public PhanCongGiangVienPhanBien save(PhanCongGiangVienPhanBien p) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(p);
        return p; // dùng được cho add hoặc update
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        PhanCongGiangVienPhanBien p = s.get(PhanCongGiangVienPhanBien.class, id);
        if (p != null)
            s.delete(p);
    }
    
    @Override
    public void assignPhanBien(int giangVienId, int hoiDongId) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createNativeQuery("INSERT INTO phanconggiangvienphanbiens(giangVienPhanBien_id, hoiDong_id) VALUES (:giangVienId, :hoiDongId)");
        query.setParameter("giangVienId", giangVienId);
        query.setParameter("hoiDongId", hoiDongId);
        query.executeUpdate();
    }
    
    @Override
    public List<PhanCongGiangVienPhanBien> findByGiangVienPhanBienId(int giangVienPhanBienId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM PhanCongGiangVienPhanBien WHERE giangVienPhanBienId = :giangVienPhanBienId", PhanCongGiangVienPhanBien.class);
        q.setParameter("giangVienPhanBienId", giangVienPhanBienId);
        return q.getResultList();
    }

    @Override
    public List<PhanCongGiangVienPhanBien> findByHoiDongId(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM PhanCongGiangVienPhanBien WHERE hoiDongId = :hoiDongId", PhanCongGiangVienPhanBien.class);
        q.setParameter("hoiDongId", hoiDongId);
        return q.getResultList();
    }
}
