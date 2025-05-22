function deleteKhoaLuan(endpoint, id) {
    if (confirm("Bạn có chắc chắn muốn xóa đề tài khóa luận này không?")) {
        fetch(`${endpoint}/${id}`, {
            method: 'DELETE'
        }).then(res => {
            if (res.status === 204) {
                alert("Đã xóa đề tài khóa luận thành công!");
                location.reload();
            } else {
                alert("Lỗi hệ thống! Không thể xóa đề tài.");
            }
        }).catch(error => {
            console.error("Lỗi khi xóa:", error);
            alert("Đã xảy ra lỗi trong quá trình xóa!");
        });
    }
}

