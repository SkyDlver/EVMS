import axios from 'axios'

// Create an Axios instance
const api = axios.create({
    baseURL: 'http://172.16.100.146:8080', // your backend base URL
    headers: {
        'Content-Type': 'application/json'
    },
})

// Optional: Add token automatically if available
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

export default api
