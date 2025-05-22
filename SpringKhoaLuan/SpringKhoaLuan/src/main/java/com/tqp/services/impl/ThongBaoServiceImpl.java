/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.ThongBao;
import com.tqp.repositories.ThongBaoRepository;
import com.tqp.services.ThongBaoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThongBaoServiceImpl implements ThongBaoService{
    @Autowired
    private ThongBaoRepository repo;

    @Override
    public List<ThongBao> getAll() {
        return repo.getAll();
    }

    @Override
    public ThongBao getById(int id) {
        return repo.getById(id);
    }

    @Override
    public ThongBao add(ThongBao t) {
        return repo.save(t);
    }

    @Override
    public void delete(int id) {
        repo.delete(id);
    }
}
