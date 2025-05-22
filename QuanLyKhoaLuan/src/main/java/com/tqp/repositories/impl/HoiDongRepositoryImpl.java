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
import com.tqp.pojo.HoiDong;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class HoiDongRepositoryImpl {
    public List<HoiDong> getHoiDong(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<HoiDong> cq = cb.createQuery(HoiDong.class);
            Root<HoiDong> root = cq.from(HoiDong.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String name = params.get("name");
                if (name != null && !name.isEmpty())
                    predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));

                String status = params.get("status");
                if (status != null && !status.isEmpty())
                    predicates.add(cb.equal(root.get("status").as(String.class), status));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public HoiDong getHoiDongById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(HoiDong.class, id);
        }
    }
}
