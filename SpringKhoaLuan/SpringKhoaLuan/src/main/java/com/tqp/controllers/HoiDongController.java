/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.EmailService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import com.tqp.services.ThanhVienHoiDongService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.web.server.ServerWebExchangeExtensionsKt.principal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Tran Quoc Phong
 */
@Controller
public class HoiDongController {
    @Autowired
    private HoiDongService hoiDongService;

    @Autowired
    private NguoiDungService nguoiDungService;
    
    @Autowired
    private ThanhVienHoiDongService thanhVienHoiDongService;

    @Autowired
    private PhanCongGiangVienPhanBienService phanBienService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/hoidong")
    public String viewHoiDong(Model model, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        
        List<HoiDong> hoiDongs = hoiDongService.getAllHoiDong();
        model.addAttribute("hoiDongs", hoiDongs);

        // Lấy danh sách giảng viên theo khoa của user đang đăng nhập
        List<NguoiDung> giangViens = nguoiDungService.getGiangVienByKhoa(user.getKhoa());
        model.addAttribute("giangViens", giangViens);

        return "hoidong"; // giao diện hội đồng
    }

    @PostMapping("/hoidong/create")
    public String createHoiDong(@RequestParam("tenHoiDong") String tenHoiDong,
                                @RequestParam("chutich") int chuTichId,
                                @RequestParam("thuky") int thuKyId,
                                @RequestParam("giangvienphanbien") List<Integer> gvPhanBienIds,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        // Lấy thông tin người dùng từ Principal
        var user = nguoiDungService.getByUsername(principal.getName());
        HoiDong hd = new HoiDong();
        hd.setName(tenHoiDong);
        String khoa = user.getKhoa();
        hd.setKhoa(khoa); // Gán thông tin khoa vào đối tượng hội đồng
        hd.setStatus("active"); // Cập nhật trạng thái hội đồng nếu cần
        hoiDongService.addHoiDong(hd);


        // Gán các thành viên hội đồng
        thanhVienHoiDongService.addThanhVien(hd.getId(), chuTichId, "chu_tich");
        thanhVienHoiDongService.addThanhVien(hd.getId(), thuKyId, "thu_ky");

        // Lọc các giảng viên đã chọn
        List<Integer> allGiangVienIds = new ArrayList<>();
        allGiangVienIds.add(chuTichId);
        allGiangVienIds.add(thuKyId);

        for (int gvId : gvPhanBienIds) {
            if (!allGiangVienIds.contains(gvId)) { // Kiểm tra giảng viên đã được chọn chưa
                thanhVienHoiDongService.addThanhVien(hd.getId(), gvId, "phan_bien");
                //phanBienService.addPhanBien(hd.getId(), gvId); // lưu thêm bảng `phanconggiangvienphanbiens`
            }
            
            // Lấy thông tin giảng viên phản biện để gửi email
            NguoiDung gv = nguoiDungService.getById(gvId);
            
            emailService.sendEmail(
            gv.getEmail(),
            "Thông báo phân công phản biện",
            "Chào thầy/cô " + gv.getUsername() + ",\nThầy/cô đã được phân công làm giảng viên phản biện cho hội đồng \"" + tenHoiDong + "\"."
        );
        }

        redirectAttributes.addFlashAttribute("message", "Tạo hội đồng thành công!");
        return "redirect:/hoidong";
    }
}
