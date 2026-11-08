<template>
  <div>
    <!-- Mobile Backdrop: dark overlay when sidebar is opened on mobile -->
    <div
      v-if="isOpen"
      @click="$emit('close')"
      class="fixed inset-0 z-40 bg-black/70 backdrop-blur-sm lg:hidden transition-opacity"
    ></div>

    <!-- Main Sidebar Container -->
    <aside
      :class="[
        'fixed top-0 bottom-0 left-0 z-50 w-64 bg-melody-dark border-r border-melody-border/60 flex flex-col transition-transform duration-300 ease-in-out',
        isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
      ]"
    >
      <!-- Brand Logo Section -->
      <div class="h-16 px-6 flex items-center justify-between border-b border-melody-border/40">
        <router-link to="/" @click="$emit('close')" class="flex items-center gap-3 group">
          <div class="w-9 h-9 rounded-xl bg-gradient-to-tr from-purple-600 via-indigo-600 to-purple-400 flex items-center justify-center shadow-lg shadow-purple-500/20 group-hover:scale-105 transition-transform">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
            </svg>
          </div>
          <div>
            <h1 class="text-base font-bold tracking-tight text-white group-hover:text-purple-300 transition-colors">
              Komsan Arom
            </h1>
            <span class="text-[10px] block font-semibold text-purple-400 tracking-wider uppercase -mt-1">
              MelodyHub
            </span>
          </div>
        </router-link>

        <!-- Close button for mobile -->
        <button
          @click="$emit('close')"
          class="lg:hidden p-1.5 rounded-lg text-melody-muted hover:text-white hover:bg-melody-card transition"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Main Navigation Menu -->
      <nav class="p-4 space-y-1">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          @click="$emit('close')"
          v-slot="{ isActive }"
          class="flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all group"
          :class="[
            $route.path === item.path
              ? 'bg-gradient-to-r from-purple-600/20 to-indigo-600/10 text-white font-semibold border border-purple-500/30'
              : 'text-melody-muted hover:text-white hover:bg-melody-card/60'
          ]"
        >
          <!-- Icon -->
          <span
            :class="[
              $route.path === item.path ? 'text-purple-400' : 'text-melody-muted group-hover:text-purple-300'
            ]"
            v-html="item.icon"
          ></span>
          <span>{{ item.label }}</span>
        </router-link>
      </nav>

      <!-- Quick Actions Divider -->
      <div class="px-4 py-2">
        <div class="h-px bg-melody-border/50"></div>
      </div>

      <!-- Playlist Quick Actions -->
      <div class="px-4 py-2 space-y-1">
        <button
          @click="createNewPlaylist"
          class="w-full flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium text-melody-muted hover:text-white hover:bg-melody-card/60 transition group cursor-pointer"
        >
          <div class="w-6 h-6 rounded-lg bg-melody-border flex items-center justify-center text-white group-hover:bg-purple-600 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
          </div>
          <span>Create Playlist</span>
        </button>

        <router-link
          to="/library"
          @click="$emit('close')"
          class="flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium text-melody-muted hover:text-white hover:bg-melody-card/60 transition group"
        >
          <div class="w-6 h-6 rounded-lg bg-gradient-to-br from-purple-600 to-pink-500 flex items-center justify-center text-white">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 fill-current" viewBox="0 0 24 24">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </div>
          <span>Liked Songs</span>
        </router-link>
      </div>

      <!-- Scrollable User Playlists -->
      <div class="flex-1 overflow-y-auto px-4 py-2 space-y-1">
        <p class="px-3.5 py-1 text-[11px] font-semibold text-melody-muted uppercase tracking-wider">
          Playlists
        </p>
        <router-link
          v-for="pl in userPlaylists"
          :key="pl.id"
          :to="`/playlist/${pl.id}`"
          @click="$emit('close')"
          class="block px-3.5 py-1.5 rounded-lg text-xs text-melody-muted hover:text-white hover:bg-melody-card/40 transition truncate"
          :class="{ 'text-purple-400 font-medium': $route.path === `/playlist/${pl.id}` }"
        >
          {{ pl.title }}
        </router-link>
      </div>

      <!-- Telegram Music Bot Promo Card -->
      <div class="p-3.5 mx-4 my-2 rounded-2xl bg-gradient-to-br from-[#2AABEE]/15 to-[#229ED9]/5 border border-[#2AABEE]/30 shadow-md">
        <div class="flex items-start gap-3">
          <div class="w-9 h-9 rounded-xl bg-[#2AABEE] text-white flex items-center justify-center shrink-0 shadow-md shadow-[#2AABEE]/30">
            <TelegramIcon custom-class="w-5 h-5 text-white" />
          </div>
          <div class="min-w-0 flex-1">
            <h4 class="text-xs font-bold text-white tracking-tight">Telegram Music Bot</h4>
            <p class="text-[11px] text-sky-200/70 leading-snug mt-0.5">Stream your catalog tracks directly in Telegram</p>
          </div>
        </div>
        <a
          :href="TELEGRAM_BOT_URL"
          target="_blank"
          rel="noopener noreferrer"
          class="mt-3 w-full py-1.5 px-3 rounded-xl bg-[#2AABEE] hover:bg-[#229ED9] text-white text-xs font-semibold flex items-center justify-center gap-1.5 shadow-md shadow-[#2AABEE]/20 transition hover:scale-[1.02] active:scale-95 cursor-pointer"
        >
          <span>Launch Bot</span>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
          </svg>
        </a>
      </div>

      <!-- Bottom Admin Link -->
      <div class="p-4 border-t border-melody-border/40">
        <router-link
          to="/admin"
          @click="$emit('close')"
          class="flex items-center gap-2.5 px-3 py-2 rounded-xl text-xs font-medium text-melody-muted hover:text-amber-400 hover:bg-melody-card/60 transition group"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-amber-500 group-hover:scale-110 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
          </svg>
          <span>Admin Portal</span>
        </router-link>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { TELEGRAM_BOT_URL } from '@/config/telegram'
import TelegramIcon from '@/components/common/TelegramIcon.vue'

// Props: accepts 'isOpen' from the parent App.vue for mobile drawer control
defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

// Emits: tells parent when to close the drawer on mobile
defineEmits(['close'])

// Navigation Items Configuration
const navItems = [
  {
    label: 'Home',
    path: '/',
    icon: `<svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>`
  },
  {
    label: 'Search',
    path: '/search',
    icon: `<svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>`
  },
  {
    label: 'Your Library',
    path: '/library',
    icon: `<svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 19V5m4 14V3m4 16v-9"/></svg>`
  }
]

// User Curated Playlists
const userPlaylists = ref([
  { id: 1, title: 'Khmer' },
  { id: 2, title: 'Chinese' },
  { id: 3, title: 'France' },
  { id: 4, title: 'English' },
  { id: 5, title: 'Phonk' },
  { id: 6, title: 'Japan' },
  { id: 7, title: 'Korea' }
])

function createNewPlaylist() {
  const newId = userPlaylists.value.length + 1
  userPlaylists.value.push({
    id: newId,
    title: `My Playlist #${newId}`
  })
}
</script>

