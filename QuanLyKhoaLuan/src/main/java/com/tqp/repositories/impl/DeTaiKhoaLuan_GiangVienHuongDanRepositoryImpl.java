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
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import java.util.*;
import org.hibernate.Session;

public class DeTaiKhoaLuan_GiangVienHuongDanRepositoryImpl {
    public List<DeTaiKhoaLuan_GiangVienHuongDan> getDanhSachHuongDan(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<DeTaiKhoaLuan_GiangVienHuongDan> cq = cb.createQuery(DeTaiKhoaLuan_GiangVienHuongDan.class);
            Root<DeTaiKhoaLuan_GiangVienHuongDan> root = cq.from(DeTaiKhoaLuan_GiangVienHuongDan.class);
            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String gvId = params.get("giangVienId");
                if (gvId != null && !gvId.isEmpty())
                    predicates.add(cb.equal(root.get("giangVien").get("id").as(Integer.class), Integer.parseInt(gvId)));

                String deTaiId = params.get("deTaiKhoaLuanId");
                if (deTaiId != null && !deTaiId.isEmpty())
                    predicates.add(cb.equal(root.get("deTaiKhoaLuan").get("id").as(Integer.class), Integer.parseInt(deTaiId)));
            }

            cq.where(predicates.toArray(Predicate[]::new));

            Query q = s.createQuery(cq);
            return q.getResultList();
        }
    }

    public DeTaiKhoaLuan_GiangVienHuongDan getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(DeTaiKhoaLuan_GiangVienHuongDan.class, id);
        }
    }
}
