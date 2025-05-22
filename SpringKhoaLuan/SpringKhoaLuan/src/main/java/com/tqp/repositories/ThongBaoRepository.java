/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.repositories;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.ThongBao;
import java.util.List;

public interface ThongBaoRepository {
    List<ThongBao> getAll();
    ThongBao getById(int id);
    ThongBao save(ThongBao t);
    void delete(int id);
}
