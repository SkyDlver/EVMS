// stores/themeStore.js
import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

export const useThemeStore = defineStore('theme', () => {
    const isDarkTheme = ref(localStorage.getItem('theme') === 'dark')
    const palette = ref(localStorage.getItem('palette') || 'default')

    // Apply body class on init
    if (isDarkTheme.value) document.body.classList.add('dark')
    else document.body.classList.remove('dark')

    const toggleDarkMode = () => {
        isDarkTheme.value = !isDarkTheme.value
    }

    const setPalette = (newPalette) => {
        palette.value = newPalette
    }

    watch(isDarkTheme, (val) => {
        localStorage.setItem('theme', val ? 'dark' : 'light')
        document.body.classList.toggle('dark', val)
    })

    watch(palette, (val) => {
        localStorage.setItem('palette', val)
        document.body.dataset.palette = val
    })

    return { isDarkTheme, palette, toggleDarkMode, setPalette }
})
