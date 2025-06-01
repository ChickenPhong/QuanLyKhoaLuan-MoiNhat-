/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.dto;

import java.util.List;

/**
 *
 * @author Tran Quoc Phong
 */
public class HoiDongWithMembersDTO {
    private Integer id;
    private String name;
    private Integer chuTichUserId;
    private Integer thuKyUserId;
    private List<Integer> giangVienPhanBienIds;

    public HoiDongWithMembersDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChuTichUserId() {
        return chuTichUserId;
    }

    public void setChuTichUserId(Integer chuTichUserId) {
        this.chuTichUserId = chuTichUserId;
    }

    public Integer getThuKyUserId() {
        return thuKyUserId;
    }

    public void setThuKyUserId(Integer thuKyUserId) {
        this.thuKyUserId = thuKyUserId;
    }

    public List<Integer> getGiangVienPhanBienIds() {
        return giangVienPhanBienIds;
    }

    public void setGiangVienPhanBienIds(List<Integer> giangVienPhanBienIds) {
        this.giangVienPhanBienIds = giangVienPhanBienIds;
    }
}
