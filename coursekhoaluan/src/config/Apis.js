import axios from "axios";
import cookie from "react-cookies";

// Nhớ cập nhật lại URL phù hợp với backend của khóa luận
const BASE_URL = 'http://localhost:8080/SpringKhoaLuan/api/';

export const endpoints = {
    // 🔐 Authentication
    login: 'login',
    'current-user': 'secure/profile',

    //  Quản trị viên
    'get-users': 'users/',             // GET - lấy danh sách
    'add-user': 'users/',              // POST - thêm user (multipart/form-data)

    //giaovu
    detai: "detai",

    //sinhvien
    detaiSinhVien: "sinhvien/detai",
};

// Gọi API có kèm token (auth required)
export const authApis = () => {
    const token = cookie.load('token');
    if (!token) {
        console.warn("Token không tồn tại khi gọi API");
    }
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            //'Authorization': `Bearer ${cookie.load('token')}`
            Authorization: token ? `Bearer ${token}` : '',  // Gửi header chỉ khi có token
        }
    });
};

// Gọi API không cần token
export default axios.create({
    baseURL: BASE_URL,
});
