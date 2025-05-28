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
    public void exportBangDiemTongHop(List<BangDiemTongHopDTO> bangDiemList, OutputStream out,
            String tenKhoa, String tenTruong, String khoaHoc) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Đọc font đúng cách
        InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/arial.ttf");
        if (fontStream == null) throw new RuntimeException("Không tìm thấy font Arial tại fonts/arial.ttf!");

        byte[] fontBytes = fontStream.readAllBytes();
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
        
         Font headerFont = new Font(bf, 14, Font.BOLD);
        Font titleFont = new Font(bf, 16, Font.BOLD);
        Font normalFont = new Font(bf, 12);
        Font cellFont = new Font(bf, 11);
        Font smallFont = new Font(bf, 7);

        // Đầu trang: trường - khoa - khóa học
        Paragraph pTruong = new Paragraph("TRƯỜNG ĐẠI HỌC MỞ THÀNH PHỐ HỒ CHÍ MINH ", headerFont);
        pTruong.setAlignment(Element.ALIGN_CENTER);
        document.add(pTruong);

        Paragraph pKhoa = new Paragraph("Khoa: " + tenKhoa, normalFont);
        document.add(pKhoa);

        Paragraph pKhoaHoc = new Paragraph("Khóa học: " + khoaHoc, normalFont);
        document.add(pKhoaHoc);

        document.add(Chunk.NEWLINE);

        // Tiêu đề chính
        Paragraph title = new Paragraph("BẢNG ĐIỂM TỔNG HỢP HỘI ĐỒNG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // line break

        //bảng
        PdfPTable table = new PdfPTable(5); //5 cột
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 5, 4, 4, 2});

        // Header
        table.addCell(new Phrase("Tên hội đồng", cellFont));
        table.addCell(new Phrase("Giảng viên phản biện", cellFont));
        table.addCell(new Phrase("Tên đề tài", cellFont));
        table.addCell(new Phrase("Tên sinh viên", cellFont));
        table.addCell(new Phrase("Điểm trung bình", cellFont));

        for (BangDiemTongHopDTO b : bangDiemList) {
            table.addCell(new Phrase(b.getTenHoiDong(), cellFont));
            table.addCell(new Phrase(b.getTenGiangVienPhanBien(), cellFont));
            table.addCell(new Phrase(b.getTenDeTai(), cellFont));
            table.addCell(new Phrase(b.getTenSinhVien(), cellFont));
            table.addCell(new Phrase(b.getDiemTrungBinh() == null ? "" : String.format("%.2f", b.getDiemTrungBinh()), cellFont));
        }

        document.add(table);
        
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // Dòng ký lãnh đạo
        Paragraph pLeader = new Paragraph("Lãnh đạo", normalFont);
        pLeader.setAlignment(Element.ALIGN_RIGHT);
        document.add(pLeader);

        Paragraph pSignSpace = new Paragraph("\n\n\n\n( Ký và ghi rõ họ tên )", smallFont);
        pSignSpace.setAlignment(Element.ALIGN_RIGHT);
        document.add(pSignSpace);
        
        document.close();
        
    }
}
