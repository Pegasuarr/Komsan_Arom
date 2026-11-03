<template>
  <div class="space-y-6">
    <!-- Welcome Header Banner -->
    <div class="relative overflow-hidden rounded-2xl bg-gradient-to-r from-purple-900/60 via-indigo-900/40 to-melody-dark p-8 border border-purple-500/20 shadow-2xl">
      <div class="relative z-10 max-w-2xl">
        <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-purple-500/20 text-purple-300 border border-purple-500/30 mb-3">
          🎵 Welcome to MelodyHub
        </span>
        <h1 class="text-3xl sm:text-4xl font-extrabold tracking-tight text-white mb-2">
          Komsan Arom
        </h1>
        <p class="text-melody-muted text-sm sm:text-base leading-relaxed">
          Your modern music streaming space. Stage 1 setup is active with Vue 3, Tailwind CSS, Pinia, Vue Router, and Axios.
        </p>
      </div>

      <!-- Background glow effect -->
      <div class="absolute -right-10 -bottom-10 w-64 h-64 bg-purple-600/20 rounded-full blur-3xl pointer-events-none"></div>
    </div>

    <!-- Tech Stack Verification Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="p-5 rounded-xl bg-melody-card border border-melody-border/60 hover:border-purple-500/40 transition">
        <div class="w-10 h-10 rounded-lg bg-emerald-500/10 text-emerald-400 flex items-center justify-center font-bold text-lg mb-3">
          V
        </div>
        <h3 class="font-semibold text-white">Vue.js 3</h3>
        <p class="text-xs text-melody-muted mt-1">Composition API & script setup reactivity engine.</p>
      </div>

      <div class="p-5 rounded-xl bg-melody-card border border-melody-border/60 hover:border-purple-500/40 transition">
        <div class="w-10 h-10 rounded-lg bg-cyan-500/10 text-cyan-400 flex items-center justify-center font-bold text-lg mb-3">
          T
        </div>
        <h3 class="font-semibold text-white">Tailwind CSS</h3>
        <p class="text-xs text-melody-muted mt-1">Dark theme palette with custom colors & responsive layout.</p>
      </div>

      <div class="p-5 rounded-xl bg-melody-card border border-melody-border/60 hover:border-purple-500/40 transition">
        <div class="w-10 h-10 rounded-lg bg-yellow-500/10 text-yellow-400 flex items-center justify-center font-bold text-lg mb-3">
          P
        </div>
        <h3 class="font-semibold text-white">Pinia</h3>
        <p class="text-xs text-melody-muted mt-1">Global state store for audio queue and user session.</p>
      </div>

      <div class="p-5 rounded-xl bg-melody-card border border-melody-border/60 hover:border-purple-500/40 transition">
        <div class="w-10 h-10 rounded-lg bg-purple-500/10 text-purple-400 flex items-center justify-center font-bold text-lg mb-3">
          R
        </div>
        <h3 class="font-semibold text-white">Vue Router & Axios</h3>
        <p class="text-xs text-melody-muted mt-1">SPA navigation and REST API client prepared for Spring Boot.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// Script setup for HomeView
</script>

