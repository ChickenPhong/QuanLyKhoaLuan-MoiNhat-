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
    loadGiangViens();
  }, []);

  const togglePhanBien = (id) => {
    setPhanBiensIds(prev =>
      prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
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
      name: tenHoiDong,                 // backend dùng 'name'
      chuTichId: Number(chuTichId),
      thuKyId: Number(thuKyId),
      giangVienPhanBienIds: phanBiensIds.map(id => Number(id)),
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
            onChange={e => setChuTichId(e.target.value)}
            required
          >
            <option value="">-- Chọn Chủ Tịch --</option>
            {giangViens
              .filter(gv => String(gv.id) !== String(thuKyId) && !phanBiensIds.includes(gv.id))
              .map(gv => (
                <option key={gv.id} value={gv.id}>{gv.username}</option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Chọn Thư Ký</Form.Label>
          <Form.Select
            value={thuKyId}
            onChange={e => setThuKyId(e.target.value)}
            required
          >
            <option value="">-- Chọn Thư Ký --</option>
            {giangViens
              .filter(gv => String(gv.id) !== String(chuTichId) && !phanBiensIds.includes(gv.id))
              .map(gv => (
                <option key={gv.id} value={gv.id}>{gv.username}</option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Chọn Giảng Viên Phản Biện (1-3 người)</Form.Label>
          <div>
            {giangViens
              .filter(gv => String(gv.id) !== String(chuTichId) && String(gv.id) !== String(thuKyId))
              .map(gv => (
                <Form.Check
                  inline
                  key={gv.id}
                  type="checkbox"
                  id={`phanbien-${gv.id}`}
                  label={gv.username}
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
    </div>
  );
};

export default TaoHoiDong;
