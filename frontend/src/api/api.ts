import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: '/api',
});

const api = {
  get: async <R>(url: string) => {
    const response = await axiosInstance.get<R>(url, {
      headers: {
        Accept: 'application/json'
      }
    });
    return response.data;
  },
  post: async <R, B>(url: string, body: B) => {
    const response = await axiosInstance.post<R>(url, body, {
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      }
    });
    return response.data;
  }
};

export default api;