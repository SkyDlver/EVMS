import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const api = axios.create({
    baseURL: 'http://172.16.100.146:8080', // your backend URL
    headers: {
        'Content-Type': 'application/json',
    },
})

// Automatically add token from Pinia store
api.interceptors.request.use((config) => {
    const authStore = useAuthStore()
    if (authStore.token) {
        config.headers.Authorization = `Bearer ${authStore.token}`
    }
    return config
})

// Optional: handle 401 globally
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            const authStore = useAuthStore()
            authStore.clearAuth()
            window.location.href = '/login' // redirect to login
        }
        return Promise.reject(error)
    }
)

export default api
