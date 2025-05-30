/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.ThongKeDTO;
import com.tqp.services.ThongKeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThongKeServiceImpl implements ThongKeService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getAllKhoaHoc() {
        // DISTINCT các khóa học có sinh viên làm khóa luận
        return entityManager.createQuery(
            "SELECT DISTINCT sv.khoaHoc FROM NguoiDung sv WHERE sv.role = 'ROLE_SINHVIEN' AND sv.khoaHoc IS NOT NULL ORDER BY sv.khoaHoc DESC",
            String.class
        ).getResultList();
    }

    @Override
    public List<String> getAllKhoa() {
        // DISTINCT các khoa có đề tài
        return entityManager.createQuery(
            "SELECT DISTINCT d.khoa FROM DeTaiKhoaLuan d WHERE d.khoa IS NOT NULL", String.class
        ).getResultList();
    }

    @Override
    public List<ThongKeDTO> thongKeLoc(String khoaHoc, String khoa) {
        String jpql =
            "SELECT new com.tqp.dto.ThongKeDTO(" +
            "   d.khoa, sv.khoaHoc, COUNT(DISTINCT d.id), AVG(bd.diem), COUNT(DISTINCT sv.id) " +
            ") " +
            "FROM DeTaiKhoaLuan d " +
            "JOIN DeTaiKhoaLuan_SinhVien ds ON ds.deTaiKhoaLuanId = d.id " +
            "JOIN NguoiDung sv ON sv.id = ds.sinhVienId " +
            "LEFT JOIN BangDiem bd ON bd.deTaiKhoaLuanSinhVienId = ds.id " +
            "WHERE (:khoaHoc IS NULL OR sv.khoaHoc = :khoaHoc) " +
            "AND (:khoa IS NULL OR d.khoa = :khoa) " +
            "GROUP BY d.khoa, sv.khoaHoc " +
            "ORDER BY sv.khoaHoc DESC, d.khoa";


        return entityManager.createQuery(jpql, ThongKeDTO.class)
            .setParameter("khoaHoc", (khoaHoc == null || khoaHoc.isEmpty()) ? null : khoaHoc)
            .setParameter("khoa", (khoa == null || khoa.isEmpty()) ? null : khoa)
            .getResultList();
    }
}
