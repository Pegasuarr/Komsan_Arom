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

    <!-- 3. Bottom Global Music Player Dock (Stage 2 Layout Placeholder for Stage 4) -->
    <div class="fixed bottom-0 left-0 right-0 z-40 h-20 sm:h-22 bg-melody-dark/95 backdrop-blur-lg border-t border-melody-border/60 px-4 sm:px-6 flex items-center justify-between">
      <!-- Left: Song details preview -->
      <div class="flex items-center gap-3 w-1/4 min-w-[140px]">
        <div class="w-12 h-12 rounded-lg bg-melody-card border border-melody-border/40 overflow-hidden flex items-center justify-center shrink-0 text-melody-muted">
          <img
            v-if="playerStore.currentSong?.cover"
            :src="playerStore.currentSong.cover"
            :alt="playerStore.currentSong.title"
            class="w-full h-full object-cover"
          />
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-purple-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
          </svg>
        </div>
        <div class="truncate">
          <p class="text-xs sm:text-sm font-semibold text-white truncate">
            {{ playerStore.currentSong?.title || 'No track selected' }}
          </p>
          <p class="text-[11px] text-melody-muted truncate">
            {{ playerStore.currentSong?.artist || 'MelodyHub' }}
          </p>
        </div>
      </div>

      <!-- Center: Playback Controls & Progress Bar -->
      <div class="flex flex-col items-center gap-1.5 w-2/4 max-w-xl">
        <div class="flex items-center gap-4">
          <!-- Shuffle Button -->
          <button class="text-melody-muted hover:text-white transition cursor-pointer" title="Shuffle">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
            </svg>
          </button>

          <!-- Previous Button -->
          <button class="text-melody-muted hover:text-white transition cursor-pointer" title="Previous">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
              <path d="M6 6h2v12H6zm3.5 6l8.5 6V6z"/>
            </svg>
          </button>

          <!-- Play/Pause Button -->
          <button
            @click="testTogglePlayback"
            class="w-9 h-9 rounded-full bg-white hover:bg-neutral-200 text-black flex items-center justify-center shadow-lg transition hover:scale-105 cursor-pointer"
            title="Play/Pause"
          >
            <svg v-if="!playerStore.isPlaying" xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current translate-x-0.5" viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z"/>
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
              <path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z"/>
            </svg>
          </button>

          <!-- Next Button -->
          <button class="text-melody-muted hover:text-white transition cursor-pointer" title="Next">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
              <path d="M6 18l8.5-6L6 6v12zM16 6v12h2V6h-2z"/>
            </svg>
          </button>

          <!-- Repeat Button -->
          <button class="text-melody-muted hover:text-white transition cursor-pointer" title="Repeat">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
          </button>
        </div>

        <!-- Progress scrubber simulator -->
        <div class="w-full flex items-center gap-2 text-[10px] text-melody-muted">
          <span>0:00</span>
          <div class="flex-1 h-1 bg-melody-border/60 rounded-full overflow-hidden">
            <div class="h-full bg-purple-500 w-1/3 rounded-full"></div>
          </div>
          <span>3:45</span>
        </div>
      </div>

      <!-- Right: Volume control preview -->
      <div class="hidden sm:flex items-center justify-end gap-2 w-1/4">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-melody-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.536 8.464a5 5 0 010 7.072m2.828-9.9a9 9 0 010 12.728M5.586 15H4a1 1 0 01-1-1v-4a1 1 0 011-1h1.586l4.707-4.707C10.923 3.663 12 4.109 12 5v14c0 .891-1.077 1.337-1.707.707L5.586 15z" />
        </svg>
        <div class="w-20 h-1 bg-melody-border/60 rounded-full overflow-hidden">
          <div class="h-full bg-white w-3/4 rounded-full"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppNavbar from '@/components/layout/AppNavbar.vue'
import { usePlayerStore } from '@/stores/playerStore'

// Mobile sidebar drawer state (reactive boolean)
const isSidebarOpen = ref(false)

// Global player state from Pinia
const playerStore = usePlayerStore()

function testTogglePlayback() {
  if (!playerStore.currentSong) {
    playerStore.playSong({
      id: 1,
      title: 'Midnight Reverie',
      artist: 'Aroma Beats'
    })
  } else {
    playerStore.togglePlay()
  }
}
</script>
