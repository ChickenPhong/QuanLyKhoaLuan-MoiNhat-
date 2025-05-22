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
import com.tqp.pojo.NguoiDung;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

public class NguoiDungRepositoryImpl {
    public List<NguoiDung> getNguoiDung(Map<String, String> params) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<NguoiDung> cq = cb.createQuery(NguoiDung.class);
            Root<NguoiDung> root = cq.from(NguoiDung.class);
            cq.select(root);

            if (params != null && !params.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();

                String role = params.get("role");
                if (role != null && !role.isEmpty())
                    predicates.add(cb.equal(root.get("role").as(String.class), role));

                String username = params.get("username");
                if (username != null && !username.isEmpty())
                    predicates.add(cb.like(root.get("username").as(String.class), "%" + username + "%"));

                cq.where(predicates.toArray(Predicate[]::new));
            }

            Query query = s.createQuery(cq);
            return query.getResultList();
        }
    }

    public NguoiDung getNguoiDungById(int id) {
        try (Session s = HibernateUtils.getFACTORY().openSession()) {
            return s.get(NguoiDung.class, id);
        }
    }
}
