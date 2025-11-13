<script setup>
import { useLayout } from '@/layout/composables/layout'
import AppConfigurator from './AppConfigurator.vue'
import { useThemeStore } from '@/stores/themeStore'

const { toggleMenu } = useLayout()
const themeStore = useThemeStore()

// Local computed helpers for template clarity
const isDarkTheme = () => themeStore.isDarkTheme
const toggleDarkMode = () => themeStore.toggleDarkMode()
</script>

<template>
    <div class="layout-topbar">
        <!-- Logo & Menu Button -->
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" @click="toggleMenu">
                <i class="pi pi-bars"></i>
            </button>
            <router-link to="/" class="layout-topbar-logo">
                <svg viewBox="0 0 54 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <!-- Simplified SVG path for brevity -->
                    <path fill="var(--primary-color)" d="M0 0h54v40H0z"/>
                </svg>
                <span>SAKAI</span>
            </router-link>
        </div>

        <!-- Topbar Actions -->
        <div class="layout-topbar-actions">
            <!-- Dark Mode Toggle -->
            <button type="button" class="layout-topbar-action" @click="toggleDarkMode">
                <i :class="['pi', { 'pi-moon': isDarkTheme(), 'pi-sun': !isDarkTheme() }]"></i>
            </button>

            <!-- Palette / App Configurator -->
            <div class="relative">
                <button
                    v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
                    type="button"
                    class="layout-topbar-action layout-topbar-action-highlight"
                >
                    <i class="pi pi-palette"></i>
                </button>
                <AppConfigurator />
            </div>

            <!-- Extra menu actions -->
            <button
                class="layout-topbar-menu-button layout-topbar-action"
                v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
            >
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <!-- Dropdown Menu -->
            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-calendar"></i>
                        <span>Calendar</span>
                    </button>
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-inbox"></i>
                        <span>Messages</span>
                    </button>
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-user"></i>
                        <span>Profile</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
