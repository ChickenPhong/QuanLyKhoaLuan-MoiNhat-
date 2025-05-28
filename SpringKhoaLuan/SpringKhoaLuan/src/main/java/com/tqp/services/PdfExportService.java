/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

import com.tqp.dto.BangDiemTongHopDTO;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author Tran Quoc Phong
 */
public interface PdfExportService {
    void exportBangDiemTongHop(List<BangDiemTongHopDTO> bangDiemList, OutputStream out,
            String tenKhoa, String tenTruong, String khoaHoc) throws Exception;
}
