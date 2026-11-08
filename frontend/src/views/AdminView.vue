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
        @click="isUploadModalOpen = true"
        class="px-5 py-2.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white font-semibold text-xs flex items-center gap-2 shadow-lg shadow-purple-600/30 transition hover:scale-105 active:scale-95 cursor-pointer self-start sm:self-auto"
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
        <p class="text-2xl font-black text-white mt-1">{{ totalTracksCount }}</p>
        <span class="text-[11px] text-emerald-400 font-medium">+{{ newTracksCount }} newly added</span>
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
        <div>
          <h2 class="text-base font-bold text-white">Catalog Songs & Releases</h2>
          <p class="text-xs text-melody-muted">Click Play on any track to listen immediately</p>
        </div>
        <button
          @click="isUploadModalOpen = true"
          class="text-xs font-semibold text-purple-400 hover:text-purple-300 transition"
        >
          + Add another track
        </button>
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
            <tr
              v-for="track in adminTracks"
              :key="track.id"
              class="hover:bg-melody-dark/40 transition group"
            >
              <td class="py-3 font-semibold text-white flex items-center gap-3">
                <button
                  @click="playTrack(track)"
                  class="w-7 h-7 rounded-full bg-purple-600 hover:bg-purple-500 text-white flex items-center justify-center transition shadow cursor-pointer shrink-0"
                  :title="'Play ' + track.title"
                >
                  <svg
                    v-if="playerStore.currentSong?.id === track.id && playerStore.isPlaying"
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-3.5 h-3.5 fill-current"
                    viewBox="0 0 24 24"
                  >
                    <path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z"/>
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    class="w-3.5 h-3.5 fill-current translate-x-0.5"
                    viewBox="0 0 24 24"
                  >
                    <path d="M8 5v14l11-7z"/>
                  </svg>
                </button>
                <span class="truncate">{{ track.title }}</span>
              </td>
              <td class="py-3 text-melody-muted">{{ track.artist }}</td>
              <td class="py-3 text-melody-muted font-mono">{{ track.duration }}</td>
              <td class="py-3 text-white font-medium">{{ track.streams }}</td>
              <td class="py-3">
                <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  Live
                </span>
              </td>
              <td class="py-3 text-right space-x-2">
                <button
                  @click="playTrack(track)"
                  class="px-2.5 py-1 rounded-lg bg-purple-600/20 hover:bg-purple-600/30 text-purple-300 text-[11px] font-medium transition cursor-pointer"
                >
                  Listen
                </button>
                <button
                  @click="deleteTrack(track.id)"
                  class="px-2.5 py-1 rounded-lg bg-red-500/10 hover:bg-red-500/20 text-red-400 text-[11px] transition cursor-pointer"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Upload/Add Music Modal Component -->
    <UploadSongModal
      :is-open="isUploadModalOpen"
      @close="isUploadModalOpen = false"
      @song-added="handleSongAdded"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'
import UploadSongModal from '@/components/music/UploadSongModal.vue'
import songService from '@/services/songService'

const playerStore = usePlayerStore()
const isUploadModalOpen = ref(false)
const adminTracks = ref([])

const totalTracksCount = computed(() => {
  return adminTracks.value.length
})

function mapTrack(t) {
  return {
    id: t.id,
    title: t.title,
    artist: t.artist,
    duration: t.duration || '3:30',
    streams: t.playCount ? t.playCount.toLocaleString() : '0',
    audioUrl: t.audioUrl ? (t.audioUrl.startsWith('http') ? t.audioUrl : `http://localhost:8085${t.audioUrl}`) : '',
    cover: t.cover ? (t.cover.startsWith('http') ? t.cover : `http://localhost:8085${t.cover}`) : 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80'
  }
}

async function loadTracks() {
  try {
    const res = await songService.getAllSongs()
    if (res.data) {
      const list = Array.isArray(res.data) ? res.data : (res.data.content || [])
      adminTracks.value = list.map(mapTrack)
    }
  } catch (err) {
    console.warn('Could not fetch catalog tracks:', err.message)
  }
}

function playTrack(track) {
  playerStore.playSong(track)
}

async function deleteTrack(id) {
  try {
    await songService.deleteSong(id)
  } catch (e) {}
  adminTracks.value = adminTracks.value.filter(t => t.id !== id)
  playerStore.catalog = playerStore.catalog.filter(t => t.id !== id)
}

function handleSongAdded(newSong) {
  adminTracks.value.unshift({
    id: newSong.id,
    title: newSong.title,
    artist: newSong.artist,
    duration: newSong.duration || '3:30',
    streams: '1',
    audioUrl: newSong.audioUrl,
    cover: newSong.cover
  })
}

onMounted(() => {
  loadTracks()
})
</script>
