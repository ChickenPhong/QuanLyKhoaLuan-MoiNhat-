import React, { useEffect, useState, useContext } from "react";
import { MyUserContext } from "../../config/Contexts";
import { Button, Table, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";

function ChamDiem() {
  const user = useContext(MyUserContext);

  const [danhSach, setDanhSach] = useState([]);
  const [tieuChis, setTieuChis] = useState([]);
  const [diem, setDiem] = useState({});
  const [selected, setSelected] = useState(null);
  const [msg, setMsg] = useState("");
  const [msgType, setMsgType] = useState("danger");

  // Load danh sách cần chấm
  useEffect(() => {
    if (user && user.id) {
      authApis()
        .get(`/giangvien/phanbien/danhsach?giangVienPhanBienId=${user.id}`)
        .then(res => setDanhSach(res.data))
        .catch(err => {
          setMsg("Lỗi lấy danh sách: " + (err.response?.data?.error || err.message));
          setMsgType("danger");
        });
    }
  }, [user]);

  // Khi chọn sinh viên, mới load tiêu chí & điểm
  useEffect(() => {
    if (selected && user && user.id) {
      authApis().get(`/giangvien/tieuchi`)
        .then(res => {
          setTieuChis(res.data);
          // Lấy điểm (nếu đã có)
          authApis().get(`/giangvien/phanbien/diem?dtsvId=${selected.dtsvId}&giangVienPhanBienId=${user.id}`)
            .then(res2 => {
              let diemObj = {};
              res2.data.forEach(d => diemObj[d.tieuChi] = d.diem ?? "");
              setDiem(diemObj);
            })
            .catch(err => {
              setMsg("Lỗi lấy điểm: " + (err.response?.data?.error || err.message));
              setMsgType("danger");
            });
        })
        .catch(err => {
          setMsg("Lỗi lấy tiêu chí: " + (err.response?.data?.error || err.message));
          setMsgType("danger");
        });
    } else {
      setTieuChis([]);
      setDiem({});
    }
  }, [selected, user]);

  // Nhập điểm: ép float, nếu rỗng thì trả rỗng
  const handleDiemChange = (tc, value) => {
    setDiem({ ...diem, [tc]: value === "" ? "" : parseFloat(value) });
  };

  // Kiểm tra nhập đủ điểm, hợp lệ 0-10
  const isFull = tieuChis.length > 0 &&
    tieuChis.every(tc =>
      diem[tc.tenTieuChi] !== "" &&
      diem[tc.tenTieuChi] !== undefined &&
      !isNaN(diem[tc.tenTieuChi]) &&
      diem[tc.tenTieuChi] >= 0 &&
      diem[tc.tenTieuChi] <= 10
    );

  // Lưu điểm
  const handleSave = () => {
    if (!selected || !user || !user.id) return;
    authApis().post(`/giangvien/phanbien/luudiem`, {
      dtsvId: selected.dtsvId,
      giangVienPhanBienId: user.id,
      diemMap: diem
    })
      .then(res => {
        setMsg(res.data || "Lưu điểm thành công!");
        setMsgType("success");
        setSelected(null);
        setDiem({});
      })
      .catch(err => {
        setMsg("Lưu điểm thất bại: " + (err.response?.data?.error || err.message));
        setMsgType("danger");
      });
  };

  if (!user || !user.id)
    return <p>Đang tải thông tin người dùng...</p>;

  return (
    <div className="container mt-4">
      <h2 className="text-info">Chấm điểm đề tài phản biện</h2>
      {msg && <Alert variant={msgType}>{msg}</Alert>}

      {!selected ? (
        <>
          <h4>Chọn sinh viên để chấm điểm:</h4>
          <Table bordered striped hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Đề tài</th>
                <th>Sinh viên</th>
                <th>Hội đồng</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {danhSach.length === 0 && (
                <tr><td colSpan={5}>Không có sinh viên nào cần chấm!</td></tr>
              )}
              {danhSach.map((dt, idx) =>
                <tr key={dt.dtsvId}>
                  <td>{idx + 1}</td>
                  <td>{dt.deTaiTitle}</td>
                  <td>{dt.sinhVienTen}</td>
                  <td>{dt.hoiDongName}</td>
                  <td>
                    <Button size="sm" variant="outline-primary" onClick={() => setSelected(dt)}>
                      Chấm điểm
                    </Button>
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </>
      ) : (
        <div>
          <h5>Đề tài: <b>{selected.deTaiTitle}</b></h5>
          <h5>Sinh viên: <b>{selected.sinhVienTen}</b></h5>
          <Form>
            <Table bordered>
              <thead>
                <tr>
                  <th>Tiêu chí</th>
                  <th>Điểm</th>
                </tr>
              </thead>
              <tbody>
                {tieuChis.map(tc =>
                  <tr key={tc.tenTieuChi}>
                    <td>{tc.tenTieuChi}</td>
                    <td>
                      <Form.Control
                        type="number"
                        min={0}
                        max={10}
                        step={0.1}
                        value={diem[tc.tenTieuChi] === 0 ? 0 : (diem[tc.tenTieuChi] || "")}
                        onChange={e => handleDiemChange(tc.tenTieuChi, e.target.value)}
                        required
                      />
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>
            <Button variant="secondary" className="me-2" onClick={() => setSelected(null)}>
              Quay lại
            </Button>
            <Button variant="success" onClick={handleSave} disabled={!isFull}>
              Lưu điểm
            </Button>
          </Form>
        </div>
      )}
    </div>
  );
}

export default ChamDiem;
