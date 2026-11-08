<template>
  <div class="min-h-screen bg-melody-darkest text-white flex flex-col font-sans selection:bg-purple-600 selection:text-white">
    <!-- 1. Left Sidebar Navigation -->
    <AppSidebar
      :is-open="isSidebarOpen"
      @close="isSidebarOpen = false"
    />

    <!-- 2. Main Content Wrapper (Shifted right by 16rem/w-64 on desktop) -->
    <div class="lg:pl-64 flex flex-col flex-1 min-h-screen">
      <!-- Sticky Top Navbar -->
      <AppNavbar
        @toggle-sidebar="isSidebarOpen = !isSidebarOpen"
      />

      <!-- Main Scrollable Content Container -->
      <main class="flex-1 p-4 sm:p-8 pb-28 max-w-7xl w-full mx-auto">
        <router-view />
      </main>
    </div>

    <!-- 3. Real HTML5 Audio Music Player Dock -->
    <MusicPlayer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppNavbar from '@/components/layout/AppNavbar.vue'
import MusicPlayer from '@/components/music/MusicPlayer.vue'

// Mobile sidebar drawer state (reactive boolean)
const isSidebarOpen = ref(false)
</script>
