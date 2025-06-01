
import React, { useState, useEffect } from "react";
import { Button, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";

const TaoHoiDong = () => {
  const [tenHoiDong, setTenHoiDong] = useState("");
  const [giangViens, setGiangViens] = useState([]);
  const [chuTichId, setChuTichId] = useState("");
  const [thuKyId, setThuKyId] = useState("");
  const [phanBiensIds, setPhanBiensIds] = useState([]);
  const [msg, setMsg] = useState(null);
  const [msgVariant, setMsgVariant] = useState("info");
  const [submitting, setSubmitting] = useState(false);

  const [danhSachHoiDong, setDanhSachHoiDong] = useState([]);

  // Hàm lấy tên giảng viên theo id từ danh sách giảng viên
  const getTenGiangVien = (id) => {
    const gv = giangViens.find((g) => g.id === id);
    return gv ? gv.fullname : "N/A";
  };

  useEffect(() => {
    const loadGiangViens = async () => {
      try {
        const res = await authApis().get("/hoidong/giangvien");
        setGiangViens(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách giảng viên: " + error.message);
        setMsgVariant("danger");
      }
    };

    const loadDanhSachHoiDong = async () => {
      try {
        const res = await authApis().get("/hoidong/with-members");
        setDanhSachHoiDong(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách hội đồng: " + error.message);
        setMsgVariant("danger");
      }
    };

    loadGiangViens();
    loadDanhSachHoiDong();
  }, []);

  const togglePhanBien = (id) => {
    setPhanBiensIds((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!tenHoiDong || !chuTichId || !thuKyId) {
      setMsg("Vui lòng điền đầy đủ thông tin.");
      setMsgVariant("warning");
      return;
    }
    if (phanBiensIds.length < 1 || phanBiensIds.length > 3) {
      setMsg("Vui lòng chọn từ 1 đến 3 giảng viên phản biện.");
      setMsgVariant("warning");
      return;
    }
    if (chuTichId === thuKyId) {
      setMsg("Chủ Tịch và Thư Ký không được trùng nhau.");
      setMsgVariant("warning");
      return;
    }
    if (phanBiensIds.includes(chuTichId) || phanBiensIds.includes(thuKyId)) {
      setMsg("Phản Biện không được trùng với Chủ Tịch hoặc Thư Ký.");
      setMsgVariant("warning");
      return;
    }

    const payload = {
      name: tenHoiDong,
      chuTichUserId: Number(chuTichId),
      thuKyUserId: Number(thuKyId),
      giangVienPhanBienIds: phanBiensIds.map((id) => Number(id)),
    };

    try {
      setSubmitting(true);
      await authApis().post("/hoidong/", payload);
      setMsg("Tạo Hội Đồng thành công!");
      setMsgVariant("success");

      setTenHoiDong("");
      setChuTichId("");
      setThuKyId("");
      setPhanBiensIds([]);

      // Load lại danh sách hội đồng mới nhất
      const res = await authApis().get("/hoidong/with-members");
      setDanhSachHoiDong(res.data || []);
    } catch (error) {
      setMsg("Tạo Hội Đồng thất bại: " + error.message);
      setMsgVariant("danger");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Tạo Hội Đồng</h2>
      {msg && <Alert variant={msgVariant}>{msg}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Tên Hội Đồng</Form.Label>
          <Form.Control
            type="text"
            value={tenHoiDong}
            onChange={(e) => setTenHoiDong(e.target.value)}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Chọn Chủ Tịch</Form.Label>
          <Form.Select
            value={chuTichId}
            onChange={(e) => setChuTichId(e.target.value)}
            required
          >
            <option value="">-- Chọn Chủ Tịch --</option>
            {giangViens
              .filter(
                (gv) =>
                  String(gv.id) !== String(thuKyId) &&
                  !phanBiensIds.includes(gv.id)
              )
              .map((gv) => (
                <option key={gv.id} value={gv.id}>
                  {gv.fullname}
                </option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Chọn Thư Ký</Form.Label>
          <Form.Select
            value={thuKyId}
            onChange={(e) => setThuKyId(e.target.value)}
            required
          >
            <option value="">-- Chọn Thư Ký --</option>
            {giangViens
              .filter(
                (gv) =>
                  String(gv.id) !== String(chuTichId) &&
                  !phanBiensIds.includes(gv.id)
              )
              .map((gv) => (
                <option key={gv.id} value={gv.id}>
                  {gv.fullname}
                </option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Chọn Giảng Viên Phản Biện (1-3 người)</Form.Label>
          <div>
            {giangViens
              .filter(
                (gv) =>
                  String(gv.id) !== String(chuTichId) &&
                  String(gv.id) !== String(thuKyId)
              )
              .map((gv) => (
                <Form.Check
                  inline
                  key={gv.id}
                  type="checkbox"
                  id={`phanbien-${gv.id}`}
                  label={gv.fullname}
                  checked={phanBiensIds.includes(gv.id)}
                  onChange={() => togglePhanBien(gv.id)}
                />
              ))}
          </div>
        </Form.Group>

        <Button type="submit" disabled={submitting}>
          {submitting ? "Đang tạo..." : "Lập Hội Đồng"}
        </Button>
      </Form>

      {/* Hiển thị danh sách Hội Đồng */}
      {danhSachHoiDong.length > 0 && (
        <>
          <h3 className="mt-4">Danh sách Hội Đồng</h3>
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên Hội Đồng</th>
                <th>Chủ Tịch</th>
                <th>Thư Ký</th>
                <th>Phản Biện</th>
              </tr>
            </thead>
            <tbody>
              {danhSachHoiDong.map((hd) => (
                <tr key={hd.id}>
                  <td>{hd.id}</td>
                  <td>{hd.name}</td>
                  <td>{getTenGiangVien(hd.chuTichUserId)}</td>
                  <td>{getTenGiangVien(hd.thuKyUserId)}</td>
                  <td>
                    {hd.giangVienPhanBienIds
                      ? hd.giangVienPhanBienIds
                          .map((id) => getTenGiangVien(id))
                          .join(", ")
                      : ""}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
};

export default TaoHoiDong;