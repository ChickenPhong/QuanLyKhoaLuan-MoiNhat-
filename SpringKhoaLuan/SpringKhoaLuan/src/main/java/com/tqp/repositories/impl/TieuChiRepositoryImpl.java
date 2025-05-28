/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.TieuChi;
import com.tqp.repositories.TieuChiRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TieuChiRepositoryImpl implements TieuChiRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<TieuChi> getAll() {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM TieuChi", TieuChi.class);
        return query.getResultList();
    }

    @Override
    public TieuChi addTieuChi(TieuChi tieuChi) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(tieuChi);
        return tieuChi; // trả về entity vừa lưu
    }

    @Override
    public List<TieuChi> findByKhoa(String khoa) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM TieuChi WHERE khoa = :khoa", TieuChi.class);
        query.setParameter("khoa", khoa);
        return query.getResultList();
    }

    @Override
    public TieuChi getById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(TieuChi.class, id);
    }

    @Override
    public TieuChi updateTieuChi(TieuChi tieuChi) {
        Session session = this.factory.getObject().getCurrentSession();
        session.update(tieuChi);
        return tieuChi;
    }
    
    @Override
    public List<TieuChi> getByKhoa(String khoa) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM TieuChi WHERE khoa = :khoa", TieuChi.class);
        query.setParameter("khoa", khoa);
        return query.getResultList();
    }
}
