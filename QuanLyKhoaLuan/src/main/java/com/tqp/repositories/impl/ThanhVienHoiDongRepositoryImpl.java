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
import com.tqp.pojo.ThanhVienHoiDong;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class ThanhVienHoiDongRepositoryImpl {
    public List<ThanhVienHoiDong> getThanhVienHoiDong(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<ThanhVienHoiDong> cq = cb.createQuery(ThanhVienHoiDong.class);
            Root<ThanhVienHoiDong> root = cq.from(ThanhVienHoiDong.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String hoiDongId = params.get("hoiDongId");
                if (hoiDongId != null && !hoiDongId.isEmpty())
                    predicates.add(cb.equal(root.get("hoiDong").get("id").as(Integer.class), Integer.parseInt(hoiDongId)));

                String userId = params.get("userId");
                if (userId != null && !userId.isEmpty())
                    predicates.add(cb.equal(root.get("user").get("id").as(Integer.class), Integer.parseInt(userId)));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public ThanhVienHoiDong getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(ThanhVienHoiDong.class, id);
        }
    }
}
