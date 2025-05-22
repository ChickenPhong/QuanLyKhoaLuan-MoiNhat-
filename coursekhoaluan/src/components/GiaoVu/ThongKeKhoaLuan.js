
import React, { useState } from "react";
import { Container, Row, Col, Form, Button, Table } from "react-bootstrap";

const ThongKeKhoaLuan = ({
  allKhoaHoc = [],
  allKhoa = [],
  initialKhoaHoc = "",
  initialKhoa = "",
  thongKeList = [],
  onFilter,
}) => {
  const [khoaHoc, setKhoaHoc] = useState(initialKhoaHoc);
  const [khoa, setKhoa] = useState(initialKhoa);

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    if (onFilter) onFilter({ khoaHoc, khoa });
  };

  return (
    <Container className="mt-4">
      <h2 className="text-center text-primary mb-4">Thống kê điểm khóa luận</h2>

      <Form className="row mb-4" onSubmit={handleFilterSubmit}>
        <Col md={3}>
          <Form.Select
            value={khoaHoc}
            onChange={(e) => setKhoaHoc(e.target.value)}
            aria-label="Chọn khóa học"
          >
            <option value="">-- Chọn khóa học --</option>
            {allKhoaHoc.map((n) => (
              <option key={n} value={n}>
                {n}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={3}>
          <Form.Select
            value={khoa}
            onChange={(e) => setKhoa(e.target.value)}
            aria-label="Chọn ngành/khoa"
          >
            <option value="">-- Chọn ngành/khoa --</option>
            {allKhoa.map((ng) => (
              <option key={ng} value={ng}>
                {ng}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={2}>
          <Button variant="primary" type="submit">
            Lọc
          </Button>
        </Col>
        <Col md={2}>
          <a
            className="btn btn-success"
            href="/khoaluan/xuat-bangdiem-tatca"
            target="_blank"
            rel="noopener noreferrer"
          >
            <i className="bi bi-file-earmark-pdf"></i> Xuất PDF toàn bộ
          </a>
        </Col>
      </Form>

      <Table bordered>
        <thead>
          <tr>
            <th>Khóa học</th>
            <th>Ngành</th>
            <th>Số sinh viên</th>
            <th>Số đề tài</th>
            <th>Điểm trung bình</th>
            <th>Xuất PDF</th>
          </tr>
        </thead>
        <tbody>
          {thongKeList.length > 0 ? (
            thongKeList.map((tk, index) => (
              <tr key={tk.hoiDongId || index}>
                <td>{tk.khoaHoc}</td>
                <td>{tk.khoa}</td>
                <td>{tk.soLuongSinhVien}</td>
                <td>{tk.soLuongDeTai}</td>
                <td>{tk.diemTrungBinh.toFixed(2)}</td>
                <td>
                  <a
                    className="btn btn-danger btn-sm"
                    href={`/khoaluan/xuat-bangdiem-hoidong/${tk.hoiDongId}`}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    <i className="bi bi-file-earmark-pdf"></i> Xuất PDF
                  </a>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={6} className="text-center">
                Không có dữ liệu thống kê
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default ThongKeKhoaLuan;