import React, { useState, useEffect } from "react";
import { Button, Form, Table, Alert } from "react-bootstrap";
import Apis, { authApis } from "../../config/Apis";

const AddTieuChi = () => {
  const [tieuChiList, setTieuChiList] = useState([]);
  const [newTieuChi, setNewTieuChi] = useState("");
  const [msg, setMsg] = useState("");
  const [editId, setEditId] = useState(null);
  const [editTenTieuChi, setEditTenTieuChi] = useState("");

  // Load danh sách tiêu chí
  const loadTieuChi = async () => {
    try {
      const res = await authApis().get("/tieuchi");
      setTieuChiList(res.data);
      setMsg("");
    } catch (error) {
      setMsg("Lỗi tải danh sách tiêu chí: " + error.message);
    }
  };

  useEffect(() => {
    loadTieuChi();
  }, []);

  // Tạo tiêu chí mới
  const handleCreateTieuChi = async () => {
    if (!newTieuChi.trim()) {
      setMsg("Vui lòng nhập tên tiêu chí");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("tenTieuChi", newTieuChi.trim());
      let res = await authApis().post("/tieuchi/", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setTieuChiList([...tieuChiList, res.data]);
      setMsg("Tạo tiêu chí thành công");
      setNewTieuChi("");
    } catch (error) {
      setMsg("Tạo tiêu chí thất bại: " + (error.response?.data || error.message));
    }
  };

  // Bắt đầu sửa
  const startEdit = (tc) => {
    setEditId(tc.id);
    setEditTenTieuChi(tc.tenTieuChi);
  };

  // Hủy sửa
  const cancelEdit = () => {
    setEditId(null);
    setEditTenTieuChi("");
  };

  // Gửi cập nhật tiêu chí (chỉ sửa tên)
  const handleUpdateTieuChi = async (id) => {
    try {
      let res = await authApis().put(`/tieuchi/${id}`, {
        tenTieuChi: editTenTieuChi
      });
      setMsg("Cập nhật tiêu chí thành công");
      setEditId(null);
      setEditTenTieuChi("");
      loadTieuChi();
    } catch (error) {
      setMsg("Cập nhật tiêu chí thất bại: " + (error.response?.data || error.message));
    }
  };

  return (
    <div className="container mt-3">
      <h3>Quản lý Tiêu chí</h3>
      {msg && <Alert variant="info">{msg}</Alert>}

      {/* Thêm mới tiêu chí */}
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateTieuChi();
        }}
        className="mb-3"
      >
        <Form.Group>
          <Form.Label>Tên tiêu chí</Form.Label>
          <Form.Control
            type="text"
            value={newTieuChi}
            onChange={(e) => setNewTieuChi(e.target.value)}
            placeholder="Nhập tên tiêu chí"
          />
        </Form.Group>
        <Button variant="primary" type="submit" className="mt-2">
          Tạo tiêu chí
        </Button>
      </Form>

      {/* Danh sách tiêu chí */}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên tiêu chí</th>
            <th>Trạng thái</th>
            <th>Khoa</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {tieuChiList.length > 0 ? (
            tieuChiList.map((tc) =>
              editId === tc.id ? (
                <tr key={tc.id}>
                  <td>{tc.id}</td>
                  <td>
                    <Form.Control
                      type="text"
                      value={editTenTieuChi}
                      onChange={(e) => setEditTenTieuChi(e.target.value)}
                    />
                  </td>
                  <td>{tc.status}</td>
                  <td>{tc.khoa}</td>
                  <td>
                    <Button size="sm" variant="success" onClick={() => handleUpdateTieuChi(tc.id)}>
                      Lưu
                    </Button>{" "}
                    <Button size="sm" variant="secondary" onClick={cancelEdit}>
                      Hủy
                    </Button>
                  </td>
                </tr>
              ) : (
                <tr key={tc.id}>
                  <td>{tc.id}</td>
                  <td>{tc.tenTieuChi}</td>
                  <td>{tc.status}</td>
                  <td>{tc.khoa}</td>
                  <td>
                    <Button size="sm" variant="warning" onClick={() => startEdit(tc)}>
                      Sửa
                    </Button>
                  </td>
                </tr>
              )
            )
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                Chưa có tiêu chí nào
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default AddTieuChi;
