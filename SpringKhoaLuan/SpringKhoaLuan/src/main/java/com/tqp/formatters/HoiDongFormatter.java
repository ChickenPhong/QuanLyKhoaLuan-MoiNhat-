/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.formatters;

/**
 *
 * @author Tran Quoc Phong
 */

import com.tqp.pojo.HoiDong;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class HoiDongFormatter implements Formatter<HoiDong>{
    @Override
    public String print(HoiDong hd, Locale locale) {
        return String.valueOf(hd.getId());
    }

    @Override
    public HoiDong parse(String id, Locale locale) throws ParseException {
        HoiDong hd = new HoiDong();
        hd.setId(Integer.valueOf(id));
        return hd;
    }
}
