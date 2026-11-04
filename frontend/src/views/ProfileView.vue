<template>
  <div class="space-y-8 max-w-4xl pb-12">
    <!-- User Profile Header -->
    <div class="flex flex-col sm:flex-row items-center sm:items-end gap-6 p-6 sm:p-8 rounded-3xl bg-gradient-to-r from-purple-900/40 via-melody-card to-melody-card border border-melody-border/60">
      <div class="w-32 h-32 rounded-full bg-gradient-to-tr from-purple-600 via-indigo-600 to-pink-500 flex items-center justify-center text-4xl font-extrabold text-white shadow-2xl shrink-0">
        {{ authStore.user?.username?.charAt(0).toUpperCase() || 'U' }}
      </div>
      <div class="space-y-1.5 text-center sm:text-left flex-1">
        <span class="text-xs font-semibold text-purple-400 tracking-wider uppercase">
          Profile
        </span>
        <h1 class="text-3xl sm:text-4xl font-black text-white">
          {{ authStore.user?.username || 'Komsan Listener' }}
        </h1>
        <p class="text-xs text-melody-muted">
          {{ authStore.user?.email || 'listener@melodyhub.io' }}
        </p>
        <div class="flex items-center justify-center sm:justify-start gap-4 pt-2 text-xs text-neutral-300">
          <span><strong class="text-white">5</strong> Public Playlists</span>
          <span>•</span>
          <span><strong class="text-white">48</strong> Followers</span>
          <span>•</span>
          <span><strong class="text-white">12</strong> Following</span>
        </div>
      </div>
    </div>

    <!-- Account Details Card -->
    <div class="p-6 rounded-2xl bg-melody-card border border-melody-border/60 space-y-6">
      <h2 class="text-lg font-bold text-white border-b border-melody-border/40 pb-3">
        Account Settings
      </h2>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm">
        <div class="p-4 rounded-xl bg-melody-dark/60 border border-melody-border/40">
          <p class="text-xs text-melody-muted">Subscription Plan</p>
          <p class="font-semibold text-white mt-1">MelodyHub Premium</p>
        </div>
        <div class="p-4 rounded-xl bg-melody-dark/60 border border-melody-border/40">
          <p class="text-xs text-melody-muted">Audio Quality</p>
          <p class="font-semibold text-white mt-1">High (320 kbps)</p>
        </div>
      </div>

      <div class="flex justify-end pt-2">
        <button
          @click="authStore.logout"
          class="px-5 py-2.5 rounded-xl bg-red-500/10 hover:bg-red-500/20 text-red-400 border border-red-500/30 text-xs font-semibold transition cursor-pointer"
        >
          Sign Out of MelodyHub
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()
</script>
