/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.TieuChi;
import com.tqp.repositories.TieuChiRepository;
import com.tqp.services.TieuChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TieuChiServiceImpl implements TieuChiService {

    @Autowired
    private TieuChiRepository tieuChiRepository;

    @Override
    public List<TieuChi> getAll() {
        return tieuChiRepository.getAll();
    }

    @Override
    public TieuChi addTieuChi(TieuChi tieuChi) {
        return tieuChiRepository.addTieuChi(tieuChi);
    }

    @Override
    public List<TieuChi> findByKhoa(String khoa) {
        return tieuChiRepository.findByKhoa(khoa);
    }

    @Override
    public TieuChi getById(int id) {
        return tieuChiRepository.getById(id);
    }

    @Override
    public TieuChi updateTieuChi(TieuChi tieuChi) {
        return tieuChiRepository.updateTieuChi(tieuChi);
    }
    
    @Override
    public List<TieuChi> getByKhoa(String khoa) {
        return tieuChiRepository.getByKhoa(khoa);
    }

}
