import React, { useState, useEffect } from "react";
import { authApis } from "../../config/Apis";
import { Button, Table, Form, Alert } from "react-bootstrap";

function AdminThongKe() {
    const [khoaList, setKhoaList] = useState([]);
    const [khoa, setKhoa] = useState("");
    const [khoaHocList, setKhoaHocList] = useState([]);
    const [khoaHoc, setKhoaHoc] = useState("");
    const [dsSinhVien, setDsSinhVien] = useState([]);
    const [msg, setMsg] = useState("");

    // Lấy danh sách khoa
    useEffect(() => {
        authApis().get("/users/khoa")
            .then(res => setKhoaList(res.data || []))
            .catch(() => setMsg("Không lấy được danh sách khoa"));
    }, []);

    // Khi chọn khoa thì load lại danh sách khóa học
    useEffect(() => {
        if (!khoa) {
            setKhoaHocList([]);
            setKhoaHoc("");
            return;
        }
        authApis().get(`/users/khoahoc?khoa=${khoa}`)
            .then(res => setKhoaHocList(res.data || []))
            .catch(() => setMsg("Không lấy được danh sách khóa học"));
    }, [khoa]);

    // Lọc thống kê
    const handleFilter = async () => {
        if (!khoa || !khoaHoc) {
            setMsg("Vui lòng chọn khoa và khóa học!");
            return;
        }
        setMsg("");
        try {
            let res = await authApis().get(`/users/thongke_khoaluan?khoa=${khoa}&khoaHoc=${khoaHoc}`);
            setDsSinhVien(res.data || []);
        } catch (err) {
            setMsg("Không lấy được dữ liệu thống kê!");
            setDsSinhVien([]);
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="text-primary mb-4">Thống kê điểm khóa luận (Quyền Admin)</h2>
            {msg && <Alert variant="danger">{msg}</Alert>}

            <Form className="mb-3 d-flex align-items-center gap-3">
                <Form.Select
                    value={khoa}
                    onChange={e => {
                        setKhoa(e.target.value);
                        setKhoaHoc("");
                        setDsSinhVien([]);
                    }}
                    style={{ width: 200 }}
                >
                    <option value="">-- Chọn khoa --</option>
                    {khoaList.map(k => (
                        <option key={k} value={k}>{k}</option>
                    ))}
                </Form.Select>

                <Form.Select
                    value={khoaHoc}
                    onChange={e => setKhoaHoc(e.target.value)}
                    style={{ width: 200 }}
                    disabled={!khoa}
                >
                    <option value="">-- Chọn khóa học --</option>
                    {khoaHocList.map(kh => (
                        <option key={kh} value={kh}>{kh}</option>
                    ))}
                </Form.Select>

                <Button variant="primary" onClick={handleFilter}>Lọc</Button>
            </Form>

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
                    {dsSinhVien.length === 0 ? (
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

export default AdminThongKe;
