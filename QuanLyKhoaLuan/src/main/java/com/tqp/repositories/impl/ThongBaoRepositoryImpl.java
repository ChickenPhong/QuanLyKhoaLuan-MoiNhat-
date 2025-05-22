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
import com.tqp.pojo.ThongBao;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class ThongBaoRepositoryImpl {
    public List<ThongBao> getThongBao(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<ThongBao> cq = cb.createQuery(ThongBao.class);
            Root<ThongBao> root = cq.from(ThongBao.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String nguoiDungId = params.get("nguoiDungId");
                if (nguoiDungId != null && !nguoiDungId.isEmpty())
                    predicates.add(cb.equal(root.get("nguoiDung").get("id").as(Integer.class), Integer.valueOf(nguoiDungId)));

                String keyword = params.get("keyword");
                if (keyword != null && !keyword.isEmpty())
                    predicates.add(cb.like(root.get("tinNhan").as(String.class), "%" + keyword + "%"));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public ThongBao getThongBaoById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(ThongBao.class, id);
        }
    }
}
