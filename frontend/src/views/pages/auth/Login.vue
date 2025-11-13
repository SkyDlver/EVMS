<script setup>
import FloatingConfigurator from '@/components/FloatingConfigurator.vue'
import { ref, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { useAuthStore } from '@/stores/auth'

const username = ref('')  // renamed from email to username
const password = ref('')
const checked = ref(false)
const loading = ref(false)
const authStore = useAuthStore()
const { proxy } = getCurrentInstance()
const router = useRouter()
const toast = useToast()

const handleLogin = async () => {
    if (!username.value || !password.value) {
        toast.add({
            severity: 'warn',
            summary: 'Missing Fields',
            detail: 'Please enter your username and password.',
            life: 3000,
        })
        return
    }

    try {
        loading.value = true
        const response = await proxy.$api.post('/api/login', {
            username: username.value,
            password: password.value,
        })

        const { token, user } = response.data

        authStore.setAuth(token, user)  // <-- store token and user in Pinia

        toast.add({
            severity: 'success',
            summary: 'Login Successful',
            detail: `Welcome, ${user?.name || 'User'}!`,
            life: 3000,
        })

        router.push('/pages/crud')
    } catch (err) {
        console.error(err)
        toast.add({
            severity: 'error',
            summary: 'Login Failed',
            detail: err.response?.data?.message || 'Invalid credentials.',
            life: 3000,
        })
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                    <div class="text-center mb-8">
                        <!-- Logo SVG omitted for brevity -->
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">
                            Welcome to PrimeLand!
                        </div>
                        <span class="text-muted-color font-medium">Sign in to continue</span>
                    </div>

                    <div>
                        <label for="username" class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2">
                            Username
                        </label>
                        <InputText v-model="username" placeholder="Username" class="w-full md:w-[30rem] mb-8"
                                   :pt="{ input: { id: 'username' } }" />

                        <label for="password" class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2">
                            Password
                        </label>
                        <Password v-model="password" placeholder="Password" :toggleMask="true" class="mb-4" fluid :feedback="false"
                                  :pt="{ input: { id: 'password' } }" />

                        <div class="flex items-center justify-between mt-2 mb-8 gap-8">
                            <div class="flex items-center">
                                <Checkbox v-model="checked" binary class="mr-2"
                                          :pt="{ input: { id: 'rememberme' } }" />
                                <label for="rememberme">Remember me</label>
                            </div>
                            <span class="font-medium no-underline ml-2 text-right cursor-pointer text-primary">
                                Forgot password?
                            </span>
                        </div>

                        <Button :loading="loading" label="Sign In" class="w-full" @click="handleLogin" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.pi-eye,
.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}
</style>
