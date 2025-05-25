/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.repositories.DeTaiHoiDongRepository;
import org.hibernate.query.Query;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DeTaiHoiDongRepositoryImpl implements DeTaiHoiDongRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<DeTaiKhoaLuan_HoiDong> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_HoiDong", DeTaiKhoaLuan_HoiDong.class);
        return q.getResultList();
    }

    @Override
    public DeTaiKhoaLuan_HoiDong getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(DeTaiKhoaLuan_HoiDong.class, id);
    }

    @Override
    public DeTaiKhoaLuan_HoiDong save(DeTaiKhoaLuan_HoiDong dthd) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(dthd);
        return dthd;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        DeTaiKhoaLuan_HoiDong dthd = s.get(DeTaiKhoaLuan_HoiDong.class, id);
        if (dthd != null) {
            s.delete(dthd);
        }
    }

    @Override
    public void assignHoiDong(int detaikhoaluanSinhVienId, int hoiDongId) {
        Session session = sessionFactory.getCurrentSession();
        DeTaiKhoaLuan_HoiDong dtHd = new DeTaiKhoaLuan_HoiDong();
        dtHd.setDeTaiKhoaLuanSinhVienId(detaikhoaluanSinhVienId);
        dtHd.setHoiDongId(hoiDongId);
        session.save(dtHd);
    }

    @Override
    public boolean isDeTaiAssigned(int detaikhoaluanSinhVienId) {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("SELECT COUNT(*) FROM DeTaiKhoaLuan_HoiDong WHERE deTaiKhoaLuanSinhVienId=:id");
        q.setParameter("id", detaikhoaluanSinhVienId);
        long count = (Long) q.getSingleResult();
        return count > 0;
    }

    @Override
    public DeTaiKhoaLuan_HoiDong findByDeTaiId(int detaikhoaluanSinhVienId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<DeTaiKhoaLuan_HoiDong> q
                = session.createQuery("FROM DeTaiKhoaLuan_HoiDong WHERE deTaiKhoaLuanSinhVienId = :id", DeTaiKhoaLuan_HoiDong.class);
        q.setParameter("id", detaikhoaluanSinhVienId);
        List<DeTaiKhoaLuan_HoiDong> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public long countDeTaiByHoiDongId(int hoiDongId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery("SELECT COUNT(*) FROM DeTaiKhoaLuan_HoiDong WHERE hoiDongId = :id", Long.class);
        q.setParameter("id", hoiDongId);
        return q.getSingleResult();
    }

    @Override
    public List<DeTaiKhoaLuan_HoiDong> findByHoiDongId(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        Query<DeTaiKhoaLuan_HoiDong> q = s.createQuery(
                "FROM DeTaiKhoaLuan_HoiDong WHERE hoiDongId = :hoiDongId", DeTaiKhoaLuan_HoiDong.class);
        q.setParameter("hoiDongId", hoiDongId);
        return q.getResultList();
    }

    @Override
    public void lockAllByHoiDongId(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "UPDATE DeTaiKhoaLuan_HoiDong SET locked = true WHERE hoiDongId = :hoiDongId";
        Query q = s.createQuery(hql);
        q.setParameter("hoiDongId", hoiDongId);
        q.executeUpdate();
    }

    @Override
    public boolean isHoiDongLocked(int hoiDongId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM DeTaiKhoaLuan_HoiDong WHERE hoiDongId = :hoiDongId AND locked = false";
        Query q = s.createQuery(hql);
        q.setParameter("hoiDongId", hoiDongId);
        Long count = (Long) q.getSingleResult();
        return count == 0; // Nếu tất cả đều locked thì return true
    }

    @Override
    public DeTaiKhoaLuan_HoiDong findByDtsvId(int dtsvId) {
        Session s = factory.getObject().getCurrentSession();
        Query<DeTaiKhoaLuan_HoiDong> q = s.createQuery(
                "FROM DeTaiKhoaLuan_HoiDong WHERE deTaiKhoaLuanSinhVienId = :dtsvId", DeTaiKhoaLuan_HoiDong.class);
        q.setParameter("dtsvId", dtsvId);
        List<DeTaiKhoaLuan_HoiDong> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    //api
    @Override
    public List<DeTaiKhoaLuan_HoiDong> findByDtsvIds(List<Integer> dtsvIds) {
        if (dtsvIds == null || dtsvIds.isEmpty()) {
            return List.of();
        }
        Session session = factory.getObject().getCurrentSession();
        String hql = "FROM DeTaiKhoaLuan_HoiDong WHERE deTaiKhoaLuanSinhVienId IN (:ids)";
        Query<DeTaiKhoaLuan_HoiDong> q = session.createQuery(hql, DeTaiKhoaLuan_HoiDong.class);
        q.setParameterList("ids", dtsvIds);
        return q.getResultList();
    }

    @Override
    public int lockAllByHoiDongIdAndDtsvIds(int hoiDongId, List<Integer> dtsvIds) {
        if (dtsvIds == null || dtsvIds.isEmpty()) {
            return 0;
        }
        Session session = factory.getObject().getCurrentSession();
        String hql = "UPDATE DeTaiKhoaLuan_HoiDong SET locked = true WHERE hoiDongId = :hoiDongId AND deTaiKhoaLuanSinhVienId IN (:dtsvIds)";
        Query q = session.createQuery(hql);
        q.setParameter("hoiDongId", hoiDongId);
        q.setParameterList("dtsvIds", dtsvIds);
        return q.executeUpdate();
    }

}
