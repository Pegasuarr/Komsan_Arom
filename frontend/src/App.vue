<template>
  <div class="min-h-screen bg-melody-darkest text-white flex flex-col font-sans">
    <!-- Top Stage-1 Header Bar -->
    <header class="h-16 border-b border-melody-border/50 bg-melody-dark/80 backdrop-blur-md px-6 flex items-center justify-between sticky top-0 z-50">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-purple-600 to-indigo-500 flex items-center justify-center shadow-lg shadow-purple-500/20">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
          </svg>
        </div>
        <div>
          <span class="text-lg font-bold tracking-tight bg-gradient-to-r from-white via-neutral-200 to-purple-400 bg-clip-text text-transparent">
            Komsan Arom
          </span>
          <span class="text-[10px] block text-purple-400 font-semibold tracking-wider uppercase">MelodyHub</span>
        </div>
      </div>

      <!-- Quick Status Badge & Interactive Pinia Test -->
      <div class="flex items-center gap-4">
        <div class="hidden sm:flex items-center gap-2 px-3 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-medium">
          <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
          Stage 1 Ready
        </div>

        <!-- Testing Pinia Reactivity -->
        <button
          @click="testPlayerStoreToggle"
          class="px-3.5 py-1.5 rounded-lg bg-melody-card hover:bg-melody-cardHover border border-melody-border text-xs font-medium transition flex items-center gap-2 cursor-pointer"
        >
          <span class="text-melody-muted">Pinia State:</span>
          <span :class="playerStore.isPlaying ? 'text-emerald-400 font-semibold' : 'text-neutral-400'">
            {{ playerStore.isPlaying ? '▶ Playing (Simulated)' : '⏸ Idle' }}
          </span>
        </button>
      </div>
    </header>

    <!-- Main View Area (Where Vue Router renders components) -->
    <main class="flex-1 max-w-7xl w-full mx-auto p-6 sm:p-8">
      <router-view />
    </main>

    <!-- Footer info -->
    <footer class="py-4 border-t border-melody-border/30 text-center text-xs text-melody-muted">
      Komsan Arom • MelodyHub Frontend Architecture • Vue 3 + Tailwind CSS + Pinia
    </footer>
  </div>
</template>

<script setup>
import { usePlayerStore } from '@/stores/playerStore'

// Access the Pinia store instance
const playerStore = usePlayerStore()

// Handler demonstrating reactive Pinia store action
function testPlayerStoreToggle() {
  if (!playerStore.currentSong) {
    playerStore.playSong({
      id: 1,
      title: 'Setup Test Track',
      artist: 'MelodyHub System'
    })
  } else {
    playerStore.togglePlay()
  }
}
</script>

