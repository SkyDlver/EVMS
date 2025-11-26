import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => {
        const savedToken = localStorage.getItem('token') || sessionStorage.getItem('token')
        const savedUser = localStorage.getItem('user') || sessionStorage.getItem('user')
        let user = null

        try {
            if (savedUser && savedUser !== 'undefined') {
                user = JSON.parse(savedUser)
            }
        } catch (e) {
            console.warn('Failed to parse saved user:', e)
            user = null
        }

        return {
            token: savedToken || null,
            user,
        }
    },
    actions: {
        setAuth(token, user, remember = false) {
            this.token = token
            this.user = user

            if (remember) {
                localStorage.setItem('token', token)
                localStorage.setItem('user', JSON.stringify(user))
                sessionStorage.removeItem('token')
                sessionStorage.removeItem('user')
            } else {
                sessionStorage.setItem('token', token)
                sessionStorage.setItem('user', JSON.stringify(user))
                localStorage.removeItem('token')
                localStorage.removeItem('user')
            }
        },
        clearAuth() {
            this.token = null
            this.user = null
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('user')
        },
    },
})
