/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.BangDiemTongHopDTO;
import com.tqp.services.PdfExportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.InputStream;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class PdfExportServiceImpl implements PdfExportService{
    @Override
    public void exportBangDiemTongHop(List<BangDiemTongHopDTO> bangDiemList, OutputStream out) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Đọc font đúng cách
        InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/arial.ttf");
        if (fontStream == null) throw new RuntimeException("Không tìm thấy font Arial tại fonts/arial.ttf!");

        byte[] fontBytes = fontStream.readAllBytes();
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
        Font titleFont = new Font(bf, 16, Font.BOLD);
        Font cellFont = new Font(bf, 12);

        Paragraph title = new Paragraph("BẢNG ĐIỂM TỔNG HỢP HỘI ĐỒNG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // line break

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 5, 4, 2});

        // Header
        table.addCell(new Phrase("Tên hội đồng", cellFont));
        table.addCell(new Phrase("Tên đề tài", cellFont));
        table.addCell(new Phrase("Tên sinh viên", cellFont));
        table.addCell(new Phrase("Điểm trung bình", cellFont));

        for (BangDiemTongHopDTO b : bangDiemList) {
            table.addCell(new Phrase(b.getTenHoiDong(), cellFont));
            table.addCell(new Phrase(b.getTenDeTai(), cellFont));
            table.addCell(new Phrase(b.getTenSinhVien(), cellFont));
            table.addCell(new Phrase(b.getDiemTrungBinh() == null ? "" : String.format("%.2f", b.getDiemTrungBinh()), cellFont));
        }

        document.add(table);
        document.close();
    }
}
