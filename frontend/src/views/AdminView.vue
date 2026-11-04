<template>
  <div class="space-y-8 pb-12">
    <!-- Admin Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div>
        <div class="flex items-center gap-2">
          <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-amber-500/20 text-amber-400 border border-amber-500/30 tracking-wider uppercase">
            Admin
          </span>
          <h1 class="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">Music Management</h1>
        </div>
        <p class="text-xs sm:text-sm text-melody-muted mt-1">Catalog overview, track ingestion, and artist metrics</p>
      </div>

      <button
        class="px-5 py-2.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white font-semibold text-xs flex items-center gap-2 shadow-lg shadow-purple-600/30 transition hover:scale-105 cursor-pointer self-start sm:self-auto"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        <span>Upload Music</span>
      </button>
    </div>

    <!-- Metrics Cards Grid -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="p-5 rounded-2xl bg-melody-card border border-melody-border/60">
        <p class="text-xs text-melody-muted font-medium">Catalog Tracks</p>
        <p class="text-2xl font-black text-white mt-1">1,482</p>
        <span class="text-[11px] text-emerald-400 font-medium">+24 this week</span>
      </div>
      <div class="p-5 rounded-2xl bg-melody-card border border-melody-border/60">
        <p class="text-xs text-melody-muted font-medium">Active Artists</p>
        <p class="text-2xl font-black text-white mt-1">128</p>
        <span class="text-[11px] text-emerald-400 font-medium">+5 verified</span>
      </div>
      <div class="p-5 rounded-2xl bg-melody-card border border-melody-border/60">
        <p class="text-xs text-melody-muted font-medium">Monthly Streams</p>
        <p class="text-2xl font-black text-white mt-1">324.8K</p>
        <span class="text-[11px] text-emerald-400 font-medium">+18.2%</span>
      </div>
      <div class="p-5 rounded-2xl bg-melody-card border border-melody-border/60">
        <p class="text-xs text-melody-muted font-medium">Active Listeners</p>
        <p class="text-2xl font-black text-white mt-1">42.5K</p>
        <span class="text-[11px] text-purple-400 font-medium">MelodyHub users</span>
      </div>
    </div>

    <!-- Songs Management Table -->
    <div class="p-6 rounded-2xl bg-melody-card border border-melody-border/60 space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-base font-bold text-white">Recent Releases</h2>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead>
            <tr class="text-melody-muted border-b border-melody-border/40 uppercase tracking-wider text-[11px]">
              <th class="pb-3 font-semibold">Track</th>
              <th class="pb-3 font-semibold">Artist</th>
              <th class="pb-3 font-semibold">Duration</th>
              <th class="pb-3 font-semibold">Streams</th>
              <th class="pb-3 font-semibold">Status</th>
              <th class="pb-3 font-semibold text-right">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-melody-border/30">
            <tr v-for="track in adminTracks" :key="track.id" class="hover:bg-melody-dark/40 transition">
              <td class="py-3 font-semibold text-white">{{ track.title }}</td>
              <td class="py-3 text-melody-muted">{{ track.artist }}</td>
              <td class="py-3 text-melody-muted">{{ track.duration }}</td>
              <td class="py-3 text-white font-medium">{{ track.streams }}</td>
              <td class="py-3">
                <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  Live
                </span>
              </td>
              <td class="py-3 text-right space-x-2">
                <button class="px-2.5 py-1 rounded-lg bg-melody-border/50 hover:bg-melody-border text-white text-[11px] transition cursor-pointer">
                  Edit
                </button>
                <button class="px-2.5 py-1 rounded-lg bg-red-500/10 hover:bg-red-500/20 text-red-400 text-[11px] transition cursor-pointer">
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const adminTracks = ref([
  { id: 1, title: 'Phnom Penh After Dark', artist: 'VannDa', duration: '3:24', streams: '84,200' },
  { id: 2, title: 'Mekong River Sunset', artist: 'Sinn Sisamouth (Lo-Fi Rework)', duration: '4:12', streams: '62,150' },
  { id: 3, title: 'Electric Dreams', artist: 'Kroma Sound', duration: '2:56', streams: '41,900' },
  { id: 4, title: 'Angkor Ambient', artist: 'Chhom Nimol', duration: '5:02', streams: '38,400' },
  { id: 5, title: 'Midnight Echoes', artist: 'Aroma Beats', duration: '3:15', streams: '92,800' }
])
</script>
