/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.HoiDongWithMembersDTO;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.pojo.ThanhVienHoiDong;
import com.tqp.repositories.HoiDongRepository;
import com.tqp.repositories.PhanCongGiangVienPhanBienRepository;
import com.tqp.repositories.ThanhVienHoiDongRepository;
import com.tqp.services.HoiDongService;
import com.tqp.services.ThanhVienHoiDongService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoiDongServiceImpl implements HoiDongService{
    @Autowired
    private HoiDongRepository hoiDongRepo;
    
    @Autowired
    private ThanhVienHoiDongRepository tvRepo;
    
    @Autowired
    private ThanhVienHoiDongService thanhVienHoiDongService;

    @Override
    public List<HoiDong> getAllHoiDong() {
        return hoiDongRepo.getAll();
    }

    @Override
    public HoiDong getById(int id) {
        return hoiDongRepo.getById(id);
    }

    @Override
    public HoiDong addHoiDong(HoiDong hoiDong) {
        return hoiDongRepo.save(hoiDong);
    }

    @Override
    public void deleteHoiDong(int id) {
        hoiDongRepo.delete(id);
    }
    
    @Override
    public List<NguoiDung> getThanhVienHoiDong(int hoiDongId) {
        return tvRepo.getGiangVienByHoiDongId(hoiDongId);
    }
    
    //api
    @Override
    public List<HoiDong> getHoiDongByKhoa(String khoa) {
        return hoiDongRepo.getHoiDongByKhoa(khoa);
    }
    
    @Override
    public List<HoiDongWithMembersDTO> getHoiDongWithMembersByKhoa(String khoa) {
        List<HoiDong> hoiDongs = hoiDongRepo.getHoiDongByKhoa(khoa);
        List<HoiDongWithMembersDTO> result = new ArrayList<>();

        for (HoiDong hd : hoiDongs) {
            HoiDongWithMembersDTO dto = new HoiDongWithMembersDTO();
            dto.setId(hd.getId());
            dto.setName(hd.getName());

            List<ThanhVienHoiDong> members = thanhVienHoiDongService.getByHoiDongId(hd.getId());

            Integer chuTich = null;
            Integer thuKy = null;
            List<Integer> phanBiens = new ArrayList<>();

            for (ThanhVienHoiDong tv : members) {
                switch (tv.getRole()) {
                    case "chu_tich":
                        chuTich = tv.getUserId();
                        break;
                    case "thu_ky":
                        thuKy = tv.getUserId();
                        break;
                    case "phan_bien":
                        phanBiens.add(tv.getUserId());
                        break;
                }
            }

            dto.setChuTichUserId(chuTich);
            dto.setThuKyUserId(thuKy);
            dto.setGiangVienPhanBienIds(phanBiens);

            result.add(dto);
        }

        return result;
    }
}
