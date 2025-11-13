import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => {
        const savedToken = localStorage.getItem('token')
        const savedUser = localStorage.getItem('user')
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
        setAuth(token, user) {
            this.token = token
            this.user = user
            localStorage.setItem('token', token)
            localStorage.setItem('user', JSON.stringify(user))
        },
        clearAuth() {
            this.token = null
            this.user = null
            localStorage.removeItem('token')
            localStorage.removeItem('user')
        },
    },
})
