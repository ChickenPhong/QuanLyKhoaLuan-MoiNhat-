/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.ThongBao;
import com.tqp.repositories.ThongBaoRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ThongBaoRepositoryImpl implements ThongBaoRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ThongBao> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM ThongBao", ThongBao.class);
        return q.getResultList();
    }

    @Override
    public ThongBao getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(ThongBao.class, id);
    }

    @Override
    public ThongBao save(ThongBao t) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(t);
        return t;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        ThongBao t = s.get(ThongBao.class, id);
        if (t != null)
            s.delete(t);
    }
}
