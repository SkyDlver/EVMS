// stores/themeStore.js
import { defineStore } from 'pinia';

export const useThemeStore = defineStore('theme', {
    state: () => ({
        preset: 'Aura',
        primary: 'emerald',
        surface: 'slate',
        isDarkTheme: false
    }),
    actions: {
        toggleDarkMode() {
            this.isDarkTheme = !this.isDarkTheme;
            document.documentElement.classList.toggle('app-dark', this.isDarkTheme);
        }
    },
    persist: true
});
