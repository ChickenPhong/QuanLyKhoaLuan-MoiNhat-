/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.repositories.DeTaiRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional

public class DeTaiRepositoryImpl implements DeTaiRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<DeTaiKhoaLuan> getAll() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan", DeTaiKhoaLuan.class);
        return q.getResultList();
    }

    @Override
    public List<DeTaiKhoaLuan> getByKhoa(String khoa) {
        Session session = factory.getObject().getCurrentSession();
        Query q = session.createQuery("FROM DeTaiKhoaLuan WHERE khoa = :khoa", DeTaiKhoaLuan.class);
        q.setParameter("khoa", khoa);
        return q.getResultList();
    }

    @Override
    public DeTaiKhoaLuan getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(DeTaiKhoaLuan.class, id);
    }

    @Override
    public DeTaiKhoaLuan save(DeTaiKhoaLuan deTai) {
        Session s = this.factory.getObject().getCurrentSession();
        s.saveOrUpdate(deTai);
        return deTai;
    }

    @Override
    public boolean delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        DeTaiKhoaLuan d = s.get(DeTaiKhoaLuan.class, id);
        if (d != null) {
            s.delete(d);
        }
        return false;
    }
}
