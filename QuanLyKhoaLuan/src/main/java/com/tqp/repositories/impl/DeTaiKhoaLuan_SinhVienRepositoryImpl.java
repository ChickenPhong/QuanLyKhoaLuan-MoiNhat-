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
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import java.util.*;
import org.hibernate.Session;

public class DeTaiKhoaLuan_SinhVienRepositoryImpl {
    public List<DeTaiKhoaLuan_SinhVien> getDanhSachDangKy(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<DeTaiKhoaLuan_SinhVien> cq = cb.createQuery(DeTaiKhoaLuan_SinhVien.class);
            Root<DeTaiKhoaLuan_SinhVien> root = cq.from(DeTaiKhoaLuan_SinhVien.class);
            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String sinhVienId = params.get("sinhVienId");
                if (sinhVienId != null && !sinhVienId.isEmpty())
                    predicates.add(cb.equal(root.get("sinhVien").get("id").as(Integer.class), Integer.parseInt(sinhVienId)));

                String deTaiId = params.get("deTaiKhoaLuanId");
                if (deTaiId != null && !deTaiId.isEmpty())
                    predicates.add(cb.equal(root.get("deTaiKhoaLuan").get("id").as(Integer.class), Integer.parseInt(deTaiId)));
            }

            cq.where(predicates.toArray(Predicate[]::new));

            Query q = s.createQuery(cq);
            return q.getResultList();
        }
    }

    public DeTaiKhoaLuan_SinhVien getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(DeTaiKhoaLuan_SinhVien.class, id);
        }
    }
}
