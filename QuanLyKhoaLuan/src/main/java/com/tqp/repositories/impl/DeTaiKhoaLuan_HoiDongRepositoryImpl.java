/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.hibernate.HibernateUtils;
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import java.util.*;
import org.hibernate.Session;

public class DeTaiKhoaLuan_HoiDongRepositoryImpl {
    public List<DeTaiKhoaLuan_HoiDong> getDanhSachHoiDongTheoDeTai(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<DeTaiKhoaLuan_HoiDong> cq = cb.createQuery(DeTaiKhoaLuan_HoiDong.class);
            Root<DeTaiKhoaLuan_HoiDong> root = cq.from(DeTaiKhoaLuan_HoiDong.class);
            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String hoiDongId = params.get("hoiDongId");
                if (hoiDongId != null && !hoiDongId.isEmpty())
                    predicates.add(cb.equal(root.get("hoiDong").get("id").as(Integer.class), Integer.parseInt(hoiDongId)));

                String deTaiId = params.get("deTaiKhoaLuanId");
                if (deTaiId != null && !deTaiId.isEmpty())
                    predicates.add(cb.equal(root.get("deTaiKhoaLuan").get("id").as(Integer.class), Integer.parseInt(deTaiId)));
            }

            cq.where(predicates.toArray(Predicate[]::new));

            Query q = s.createQuery(cq);
            return q.getResultList();
        }
    }

    public DeTaiKhoaLuan_HoiDong getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(DeTaiKhoaLuan_HoiDong.class, id);
        }
    }
}
