/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.repositories.DeTaiSinhVienRepository;
import org.hibernate.query.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DeTaiSinhVienRepositoryImpl implements DeTaiSinhVienRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<DeTaiKhoaLuan_SinhVien> getAll() {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_SinhVien", DeTaiKhoaLuan_SinhVien.class);
        return q.getResultList();
    }

    @Override
    public DeTaiKhoaLuan_SinhVien getById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(DeTaiKhoaLuan_SinhVien.class, id);
    }

    @Override
    public DeTaiKhoaLuan_SinhVien save(DeTaiKhoaLuan_SinhVien dtsv) {
        Session s = factory.getObject().getCurrentSession();
        s.saveOrUpdate(dtsv);
        return dtsv;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        DeTaiKhoaLuan_SinhVien dtsv = s.get(DeTaiKhoaLuan_SinhVien.class, id);
        if (dtsv != null)
            s.delete(dtsv);
    }
    
    @Override
    public boolean isSinhVienDaXepDeTai(int sinhVienId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(*) FROM DeTaiKhoaLuan_SinhVien WHERE sinhVienId = :id");
        q.setParameter("id", sinhVienId);
        Long count = (Long) q.getSingleResult();
        return count > 0;
    }
    
    @Override
    public DeTaiKhoaLuan_SinhVien findBySinhVienId(int sinhVienId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeTaiKhoaLuan_SinhVien WHERE sinhVienId = :id", DeTaiKhoaLuan_SinhVien.class);
        q.setParameter("id", sinhVienId);
        List<DeTaiKhoaLuan_SinhVien> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    @Override
    public DeTaiKhoaLuan_SinhVien findByDeTaiId(int deTaiId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<DeTaiKhoaLuan_SinhVien> q = 
            session.createQuery("FROM DeTaiKhoaLuan_SinhVien WHERE deTaiKhoaLuanId = :id", DeTaiKhoaLuan_SinhVien.class);
        q.setParameter("id", deTaiId);
        List<DeTaiKhoaLuan_SinhVien> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    @Override
    public List<DeTaiKhoaLuan_SinhVien> getByKhoaVaKhoaHoc(String khoa, String khoaHoc) {
        Session session = factory.getObject().getCurrentSession();
        // Giả sử bảng NguoiDung là entity 'NguoiDung', join tới DeTaiKhoaLuan_SinhVien
        String hql = "SELECT dtsv FROM DeTaiKhoaLuan_SinhVien dtsv "
                   + "JOIN NguoiDung nd ON dtsv.sinhVienId = nd.id "
                   + "WHERE nd.khoa = :khoa AND nd.khoaHoc = :khoaHoc";
        return session.createQuery(hql, DeTaiKhoaLuan_SinhVien.class)
                      .setParameter("khoa", khoa)
                      .setParameter("khoaHoc", khoaHoc)
                      .getResultList();
    }
    
    //api
    @Override
    public List<DeTaiKhoaLuan_SinhVien> findBySinhVienIds(List<Integer> sinhVienIds) {
        if (sinhVienIds == null || sinhVienIds.isEmpty()) return List.of();
        Session session = factory.getObject().getCurrentSession();
        String hql = "FROM DeTaiKhoaLuan_SinhVien WHERE sinhVienId IN (:ids)";
        Query<DeTaiKhoaLuan_SinhVien> q = session.createQuery(hql, DeTaiKhoaLuan_SinhVien.class);
        q.setParameterList("ids", sinhVienIds);
        return q.getResultList();
    }
}
