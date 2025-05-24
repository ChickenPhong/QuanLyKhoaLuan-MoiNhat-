import React, { useEffect, useState } from "react";
import axios from "axios";

function ChamDiem({ user }) {
  const [danhSach, setDanhSach] = useState([]);
  const [tieuChis, setTieuChis] = useState([]);
  const [diem, setDiem] = useState({});
  const [selected, setSelected] = useState(null);

  // Lấy danh sách đề tài + sinh viên của giảng viên phản biện
  useEffect(() => {
    if (user && user.id) {
      axios.get(`/api/giangvien/phanbien/danhsach?giangVienPhanBienId=${user.id}`)
        .then(res => setDanhSach(res.data))
        .catch(err => console.error("Lỗi lấy danh sách đề tài:", err));

      axios.get(`/api/giangvien/tieuchi`)
        .then(res => setTieuChis(res.data))
        .catch(err => console.error("Lỗi lấy tiêu chí:", err));
    }
  }, [user]);

  // Khi chọn 1 đề tài_sinhvien, load điểm đã chấm
  useEffect(() => {
    if (selected && user && user.id) {
      axios.get(`/api/giangvien/phanbien/diem?dtsvId=${selected.dtsvId}&giangVienPhanBienId=${user.id}`)
        .then(res => {
          let diemObj = {};
          res.data.forEach(d => diemObj[d.tieuChi] = d.diem || "");
          setDiem(diemObj);
        })
        .catch(err => console.error("Lỗi lấy điểm:", err));
    }
  }, [selected, user]);

  const handleDiemChange = (tc, value) => {
    setDiem({ ...diem, [tc]: value });
  };

  const handleSave = () => {
    if (!selected || !user || !user.id) return;

    axios.post(`/api/giangvien/phanbien/luudiem`, {
      dtsvId: selected.dtsvId,
      giangVienPhanBienId: user.id,
      diemMap: diem
    })
      .then(res => alert(res.data))
      .catch(err => alert("Lưu điểm thất bại"));
  };

  if (!user || !user.id)
    return <p>Đang tải thông tin người dùng...</p>;

  return (
    <div>
      <h2>Chấm điểm đề tài phản biện</h2>
      <div>
        <h4>Chọn đề tài để chấm điểm:</h4>
        <ul>
          {danhSach.map(dt =>
            <li key={dt.dtsvId}>
              <button onClick={() => setSelected(dt)}>
                {dt.deTaiTitle} - {dt.sinhVienTen} ({dt.hoiDongName})
              </button>
            </li>
          )}
        </ul>
      </div>

      {selected &&
        <div>
          <h5>Đề tài: {selected.deTaiTitle}</h5>
          <h5>Sinh viên: {selected.sinhVienTen}</h5>
          <table>
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
                    <input
                      type="number"
                      value={diem[tc.tenTieuChi] || ""}
                      min={0} max={10} step={0.1}
                      onChange={e => handleDiemChange(tc.tenTieuChi, e.target.value)}
                    />
                  </td>
                </tr>
              )}
            </tbody>
          </table>
          <button onClick={handleSave}>Lưu điểm</button>
        </div>
      }
    </div>
  );
}

export default ChamDiem;
