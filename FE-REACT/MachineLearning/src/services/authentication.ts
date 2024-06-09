import api from './api';


interface RegisterData {
  username: string;
  password: string;
}

interface LoginData {
  username: string;
  password: string;
}

interface User {
  id: string;
  username: string;
  fullname: string;
  email: string;
  dob: string;
  roles: string[];
}

export const register = async (data: RegisterData) => {
  try {
    const response = await api.post('/auth/register', data);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};


export const login = async (data: LoginData) => {
  try {
    const response = await api.post('/auth/login', data);
    const { token } = response.data;
    localStorage.setItem('token', token);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};


// export const login = (username: any, password: any) => {
//   return api.post('/auth/token', { username, password });
// };

// export const register = (username: any, password: any) => {
//   return api.post('/users', { username, password });
// };

export const authHeader = () => {
  const token = localStorage.getItem('token');
  if (token) {
    return { Authorization: 'Bearer ' + token };
  } else {
    return {};
  }
};
