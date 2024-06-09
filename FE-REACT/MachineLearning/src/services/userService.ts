import api from './api';

export  const getUsers = () => {
  return api.get('/users');
};

export async function getCurrentUser() {
  // return request('/api/users/bf349934-c6a0-4ad8-b573-fcef34e16c9c'); // Đường dẫn tới API lấy thông tin người dùng hiện tại
  return api.get('/users/bf349934-c6a0-4ad8-b573-fcef34e16c9c');

}
