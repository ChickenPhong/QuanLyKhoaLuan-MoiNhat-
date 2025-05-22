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
import com.tqp.pojo.DeTaiKhoaLuan;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class DeTaiKhoaLuanRepositoryImpl {
    public List<DeTaiKhoaLuan> getDeTai(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<DeTaiKhoaLuan> cq = cb.createQuery(DeTaiKhoaLuan.class);
            Root<DeTaiKhoaLuan> root = cq.from(DeTaiKhoaLuan.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String title = params.get("title");
                if (title != null && !title.isEmpty())
                    predicates.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public DeTaiKhoaLuan getById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(DeTaiKhoaLuan.class, id);
        }
    }
}
