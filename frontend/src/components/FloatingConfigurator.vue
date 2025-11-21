<script setup>
import AppConfigurator from '@/layout/AppConfigurator.vue';
import { useThemeStore } from '@/stores/themeStore'
const themeStore = useThemeStore();
</script>

<template>
    <div class="fixed flex gap-4 top-8 right-8">
        <Button
            type="button"
            @click="themeStore.toggleDarkMode"
            rounded
            :icon="themeStore.isDarkTheme ? 'pi pi-moon' : 'pi pi-sun'"
            severity="secondary"
        />

        <div class="relative">
            <Button
                icon="pi pi-palette"
                v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
                type="button"
                rounded
            />
            <AppConfigurator />
        </div>
    </div>
</template>
