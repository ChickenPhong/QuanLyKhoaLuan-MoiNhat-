import React, { useEffect, useState, useContext } from "react";
import { MyUserContext } from "../../config/Contexts";
import { Button, Table, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";


// Hàm group danh sách theo khóa học
function groupByKhoaHoc(danhSach) {
  const map = {};
  for (const item of danhSach) {
    // Tùy thuộc backend trả, bạn lấy từ selected hoặc từ sinhVien hoặc từ đề tài
    // Ưu tiên lấy từ sinhVien.khoaHoc, nếu không có thì lấy item.khoaHoc, nếu không thì null
    const khoaHoc = item.khoaHoc || "";
    if (!khoaHoc) continue; // Bỏ qua nếu không có khóa
    if (!map[khoaHoc]) map[khoaHoc] = [];
    map[khoaHoc].push(item);
  }
  return map;
}

function ChamDiem() {
  const user = useContext(MyUserContext);

  const [danhSach, setDanhSach] = useState([]);
  const [tieuChis, setTieuChis] = useState([]);
  const [diem, setDiem] = useState({});
  const [selected, setSelected] = useState(null);
  const [msg, setMsg] = useState("");
  const [msgType, setMsgType] = useState("danger");
  const [isLocked, setIsLocked] = useState(false);

  // Load danh sách cần chấm
  useEffect(() => {
    if (user && user.id) {
      authApis()
        .get(`/giangvien/phanbien/danhsach`)
        .then(res => {
          setDanhSach(res.data);
          console.log("DanhSachAPI: ", res.data); // <--- Thêm dòng này!
        })
        .catch(err => {
          setMsg("Lỗi lấy danh sách: " + (err.response?.data?.error || err.message));
          setMsgType("danger");
        });
    }
  }, [user]);

  // Group và sort khóa giảm dần (mới nhất lên đầu)
  const group = groupByKhoaHoc(danhSach);
  const sortedKhoaHoc = Object.keys(group).sort((a, b) => b.localeCompare(a)); // giảm dần

  // Khi chọn sinh viên, mới load tiêu chí & điểm
  useEffect(() => {
    if (selected && user && user.id) {
      // Truyền thêm khoa của đề tài khi gọi API
      const khoa = selected.khoa || selected.khoaDeTai; // hoặc key phù hợp với cấu trúc dữ liệu
      console.log("selected:", selected, "khoa gửi lên:", khoa);
      authApis().get(`/giangvien/tieuchi${khoa ? `?khoa=${encodeURIComponent(khoa)}` : ""}`)
        .then(res => {
          setTieuChis(res.data);
          // Lấy điểm (nếu đã có)
          authApis().get(`/giangvien/phanbien/diem?dtsvId=${selected.dtsvId}`)
            .then(res2 => {
              let diemObj = {};
              // Lấy trạng thái khóa từ response
              if (res2.data && res2.data.isLocked !== undefined) {
                setIsLocked(res2.data.isLocked);
              } else {
                setIsLocked(false);
              }
              // Lấy list điểm
              (res2.data.list || res2.data).forEach(d => diemObj[d.tieuChi] = d.diem ?? "");
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
      setIsLocked(false);
    }
  }, [selected, user]);

  // Nhập điểm: ép float, nếu rỗng thì trả rỗng
  const handleDiemChange = (tc, value) => {
    setDiem({ ...diem, [tc]: value === "" ? "" : parseFloat(value) });
  };

  // Kiểm tra nhập đủ điểm, hợp lệ 0-10
  const isFull = tieuChis.length > 0 &&
    tieuChis.every(tc =>
      diem[tc.tenTieuChi || tc.tieuChi] !== "" &&
      diem[tc.tenTieuChi || tc.tieuChi] !== undefined &&
      !isNaN(diem[tc.tenTieuChi || tc.tieuChi]) &&
      diem[tc.tenTieuChi || tc.tieuChi] >= 0 &&
      diem[tc.tenTieuChi || tc.tieuChi] <= 10
    );

  // Lưu điểm
  const handleSave = () => {
    if (!selected || !user || !user.id) return;
    authApis().post(`/giangvien/phanbien/luudiem`, {
      dtsvId: selected.dtsvId,
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
          {sortedKhoaHoc.length === 0 && <p>Không có sinh viên nào cần chấm!</p>}
          {sortedKhoaHoc.map(khoaHoc => (
            <div key={khoaHoc} className="mb-4">
              <h5 style={{ fontWeight: "bold", color: "#0984e3" }}>
                Khóa {khoaHoc}
              </h5>
              <Table bordered striped hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Đề tài</th>
                    <th>Sinh viên</th>
                    <th>Hội đồng</th>
                    <th>Trạng thái</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {group[khoaHoc].map((dt, idx) => (
                    <tr key={dt.dtsvId}>
                      <td>{idx + 1}</td>
                      <td>{dt.deTaiTitle}</td>
                      <td>{dt.sinhVienTen}</td>
                      <td>{dt.hoiDongName}</td>
                      <td>
                        {dt.isLocked
                          ? <span style={{ color: "red", fontWeight: "bold" }}>Đã khóa</span>
                          : <span style={{ color: "green" }}>Chưa khóa</span>
                        }
                      </td>
                      <td>
                        <Button
                          size="sm"
                          variant="outline-primary"
                          onClick={() => setSelected(dt)}
                        >
                          Chấm điểm
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          ))}
        </>
      ) : (
        <div>
          <h5>Đề tài: <b>{selected.deTaiTitle}</b></h5>
          <h5>Sinh viên: <b>{selected.sinhVienTen}</b></h5>
          {isLocked &&
            <Alert variant="danger">
              Đề tài này đã bị khóa. Bạn chỉ có thể xem điểm, không thể chỉnh sửa hoặc lưu điểm!
            </Alert>
          }
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
                  <tr key={tc.tenTieuChi || tc.tieuChi}>
                    <td>{tc.tenTieuChi || tc.tieuChi}</td>
                    <td>
                      <Form.Control
                        type="number"
                        min={0}
                        max={10}
                        step={0.1}
                        value={diem[tc.tenTieuChi || tc.tieuChi] === 0 ? 0 : (diem[tc.tenTieuChi || tc.tieuChi] || "")}
                        onChange={e => handleDiemChange(tc.tenTieuChi || tc.tieuChi, e.target.value)}
                        required
                        disabled={isLocked}
                      />
                    </td>
                  </tr>
                )}
              </tbody>
            </Table>
            <Button variant="secondary" className="me-2" onClick={() => setSelected(null)}>
              Quay lại
            </Button>
            <Button variant="success" onClick={handleSave} disabled={!isFull || isLocked}>
              Lưu điểm
            </Button>
          </Form>
        </div>
      )}
    </div>
  );
}

export default ChamDiem;
