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
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class PhanCongGiangVienPhanBienRepositoryImpl {
    public List<PhanCongGiangVienPhanBien> getPhanCong(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<PhanCongGiangVienPhanBien> cq = cb.createQuery(PhanCongGiangVienPhanBien.class);
            Root<PhanCongGiangVienPhanBien> root = cq.from(PhanCongGiangVienPhanBien.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String gvId = params.get("giangVienPhanBienId");
                if (gvId != null && !gvId.isEmpty())
                    predicates.add(cb.equal(root.get("giangVienPhanBien").get("id").as(Integer.class), Integer.parseInt(gvId)));

                String deTaiId = params.get("deTaiKhoaLuanId");
                if (deTaiId != null && !deTaiId.isEmpty())
                    predicates.add(cb.equal(root.get("deTaiKhoaLuan").get("id").as(Integer.class), Integer.parseInt(deTaiId)));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public PhanCongGiangVienPhanBien getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(PhanCongGiangVienPhanBien.class, id);
        }
    }
}
