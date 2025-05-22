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
import com.tqp.pojo.BangDiem;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;


public class BangDiemRepositoryImpl {
    public List<BangDiem> getBangDiem(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<BangDiem> cq = cb.createQuery(BangDiem.class);
            Root<BangDiem> root = cq.from(BangDiem.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String giangVienId = params.get("giangVienPhanBienId");
                if (giangVienId != null && !giangVienId.isEmpty())
                    predicates.add(cb.equal(root.get("giangVienPhanBien").get("id").as(Integer.class), Integer.parseInt(giangVienId)));

                String khoaLuanId = params.get("deTaiKhoaLuanId");
                if (khoaLuanId != null && !khoaLuanId.isEmpty())
                    predicates.add(cb.equal(root.get("deTaiKhoaLuan").get("id").as(Integer.class), Integer.parseInt(khoaLuanId)));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public BangDiem getBangDiemById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(BangDiem.class, id);
        }
    }
}
