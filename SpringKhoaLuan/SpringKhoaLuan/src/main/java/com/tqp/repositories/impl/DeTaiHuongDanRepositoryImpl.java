/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import com.tqp.repositories.DeTaiHuongDanRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DeTaiHuongDanRepositoryImpl implements DeTaiHuongDanRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<DeTaiKhoaLuan_GiangVienHuongDan> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_GiangVienHuongDan", DeTaiKhoaLuan_GiangVienHuongDan.class);
        return q.getResultList();
    }

    @Override
    public DeTaiKhoaLuan_GiangVienHuongDan getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(DeTaiKhoaLuan_GiangVienHuongDan.class, id);
    }

    @Override
    public DeTaiKhoaLuan_GiangVienHuongDan save(DeTaiKhoaLuan_GiangVienHuongDan d) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(d);
        return d;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        DeTaiKhoaLuan_GiangVienHuongDan d = s.get(DeTaiKhoaLuan_GiangVienHuongDan.class, id);
        if (d != null)
            s.delete(d);
    }
    
    @Override
    public DeTaiKhoaLuan_GiangVienHuongDan findByDeTaiKhoaLuanSinhVienId(int id) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_GiangVienHuongDan WHERE deTaiKhoaLuanSinhVienId = :id", DeTaiKhoaLuan_GiangVienHuongDan.class);
        q.setParameter("id", id);
        List<DeTaiKhoaLuan_GiangVienHuongDan> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    @Override
    public List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByDeTaiKhoaLuanSinhVienId(int id) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_GiangVienHuongDan WHERE deTaiKhoaLuanSinhVienId = :id", DeTaiKhoaLuan_GiangVienHuongDan.class);
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    //api
    @Override
    public List<DeTaiKhoaLuan_GiangVienHuongDan> findAllByGiangVienHuongDanId(int giangVienId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery(
            "FROM DeTaiKhoaLuan_GiangVienHuongDan WHERE giangVienHuongDanId = :gid",
            DeTaiKhoaLuan_GiangVienHuongDan.class
        );
        q.setParameter("gid", giangVienId);
        return q.getResultList();
    }
}
