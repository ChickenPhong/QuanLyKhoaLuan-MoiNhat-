import React, { useState, useEffect } from "react";
import { authApis } from "../../config/Apis";
import { Button, Table, Form, Alert } from "react-bootstrap";

function ThongKeKhoaLuan() {
    const [khoaHocList, setKhoaHocList] = useState([]);
    const [khoaHoc, setKhoaHoc] = useState("");
    const [nganhList, setNganhList] = useState([]);
    const [nganh, setNganh] = useState("");
    const [dsSinhVien, setDsSinhVien] = useState([]);
    const [tongSinhVien, setTongSinhVien] = useState(0);
    const [soSinhVienThamGia, setSoSinhVienThamGia] = useState(0);
    const [msg, setMsg] = useState("");

    // Lấy danh sách khóa học khi load
    useEffect(() => {
        authApis().get("/giaovu/khoahoc")
            .then(res => setKhoaHocList(res.data || []))
            .catch(() => setMsg("Không lấy được danh sách khóa học"));
    }, []);

    // Lấy danh sách ngành khi chọn khóa học
    useEffect(() => {
        if (!khoaHoc) {
            setNganhList([]);
            setNganh("");
            return;
        }
        authApis().get(`/giaovu/nganh?khoaHoc=${khoaHoc}`)
            .then(res => setNganhList(res.data || []))
            .catch(() => setNganhList([]));
        setNganh("");
    }, [khoaHoc]);

    // Lọc thống kê sinh viên theo khóa học (và ngành nếu có)
    const handleFilter = async () => {
        if (!khoaHoc) {
            setMsg("Vui lòng chọn khóa học!");
            return;
        }
        setMsg("");
        try {
            let url = `/giaovu/thongke_sinhvien?khoaHoc=${khoaHoc}`;
            if (nganh) url += `&nganh=${encodeURIComponent(nganh)}`;
            let res = await authApis().get(url);
            setDsSinhVien(res.data.dsSinhVien || []); // Đúng theo object trả về
            setTongSinhVien(res.data.tongSinhVien || 0);
            setSoSinhVienThamGia(res.data.soSinhVienThamGia || 0);
        } catch (err) {
            setMsg("Không lấy được dữ liệu thống kê!");
            setDsSinhVien([]);
            setTongSinhVien(0);
            setSoSinhVienThamGia(0);
        }
    };

    // Xuất PDF điểm toàn bộ
    const handleExportPDF = async () => {
        if (!khoaHoc) {
            setMsg("Vui lòng chọn khóa học để xuất PDF!");
            return;
        }
        setMsg("");
        try {
            const res = await authApis().post(`/giaovu/xuat_pdf_diem`, { khoaHoc }, { responseType: "blob" });
            const url = window.URL.createObjectURL(new Blob([res.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `bang_diem_khoahoc_${khoaHoc}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            setMsg("Xuất PDF thất bại!");
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="text-primary mb-4">Thống kê điểm khóa luận</h2>
            {msg && <Alert variant="danger">{msg}</Alert>}

            <Form className="mb-3 d-flex align-items-center gap-3">
                <Form.Select
                    value={khoaHoc}
                    onChange={e => setKhoaHoc(e.target.value)}
                    style={{ width: 250, marginRight: 16 }}
                >
                    <option value="">-- Chọn khóa học --</option>
                    {khoaHocList.map(k => (
                        <option key={k} value={k}>{k}</option>
                    ))}
                </Form.Select>
                <Form.Select
                    value={nganh}
                    onChange={e => setNganh(e.target.value)}
                    style={{ width: 220, marginRight: 16 }}
                    disabled={!khoaHoc || nganhList.length === 0}
                >
                    <option value="">-- Chọn ngành (tùy chọn) --</option>
                    {nganhList.map(n => (
                        <option key={n} value={n}>{n}</option>
                    ))}
                </Form.Select>
                <Button variant="primary" onClick={handleFilter}>Lọc</Button>
                <Button variant="success" className="ms-3" onClick={handleExportPDF}>Xuất PDF toàn bộ</Button>
            </Form>

            <div>
                <b>Tổng số sinh viên:</b> {tongSinhVien}
                &nbsp; | &nbsp;
                <b>Sinh viên tham gia khóa luận:</b> {soSinhVienThamGia}
                &nbsp; | &nbsp;
                <b>Tỷ lệ tham gia:</b>
                {tongSinhVien > 0
                    ? ((soSinhVienThamGia / tongSinhVien) * 100).toFixed(1) + '%'
                    : '0%'}
            </div>

            <Table striped bordered>
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sinh viên</th>
                        <th>Tên đề tài</th>
                        <th>Tên hội đồng</th>
                        <th>Điểm trung bình</th>
                    </tr>
                </thead>
                <tbody>
                    {(!Array.isArray(dsSinhVien) || dsSinhVien.length === 0) ? (
                        <tr><td colSpan={5}>Không có dữ liệu thống kê</td></tr>
                    ) : dsSinhVien.map((sv, idx) => (
                        <tr key={idx}>
                            <td>{idx + 1}</td>
                            <td>{sv.tenSinhVien}</td>
                            <td>{sv.tenDeTai}</td>
                            <td>{sv.tenHoiDong}</td>
                            <td>{sv.diemTrungBinh !== null && sv.diemTrungBinh !== undefined ? sv.diemTrungBinh.toFixed(2) : ""}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    );
}

export default ThongKeKhoaLuan;
