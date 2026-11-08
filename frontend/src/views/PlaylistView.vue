<template>
  <div class="space-y-8 pb-12">
    <!-- Playlist Header Banner -->
    <div class="flex flex-col sm:flex-row items-center sm:items-end gap-6 p-6 sm:p-8 rounded-3xl bg-gradient-to-b from-purple-900/60 to-melody-card border border-melody-border/60">
      <img
        :src="playlist.cover"
        :alt="playlist.title"
        class="w-44 h-44 sm:w-52 sm:h-52 rounded-2xl object-cover shadow-2xl shrink-0"
      />
      <div class="space-y-2 text-center sm:text-left">
        <span class="text-xs font-bold uppercase tracking-wider text-purple-400">
          Playlist
        </span>
        <h1 class="text-2xl sm:text-4xl lg:text-5xl font-black text-white tracking-tight">
          {{ playlist.title }}
        </h1>
        <p class="text-xs sm:text-sm text-melody-muted max-w-xl leading-relaxed">
          {{ playlist.description }}
        </p>
        <p class="text-xs text-neutral-300 font-medium pt-1">
          Curated by <span class="text-white font-semibold">{{ playlist.creator }}</span> • {{ playlist.tracks.length }} songs
        </p>
      </div>
    </div>

    <!-- Action Toolbar (Play All, Shuffle, Save) -->
    <div class="flex items-center gap-4 px-2">
      <button
        @click="playAll"
        class="w-14 h-14 rounded-full bg-purple-600 hover:bg-purple-500 text-white flex items-center justify-center shadow-xl shadow-purple-600/30 transition hover:scale-105 cursor-pointer"
        title="Play playlist"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 fill-current translate-x-0.5" viewBox="0 0 24 24">
          <path d="M8 5v14l11-7z" />
        </svg>
      </button>

      <button
        class="p-3 rounded-full text-melody-muted hover:text-white hover:bg-melody-card transition cursor-pointer"
        title="Save to Library"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
        </svg>
      </button>
    </div>

    <!-- Song Table List -->
    <div class="space-y-1">
      <div class="grid grid-cols-12 px-4 py-2 text-xs font-semibold text-melody-muted uppercase tracking-wider border-b border-melody-border/40">
        <span class="col-span-1 text-center">#</span>
        <span class="col-span-7 sm:col-span-6">Title</span>
        <span class="hidden sm:block sm:col-span-4">Album</span>
        <span class="col-span-4 sm:col-span-1 text-right">Time</span>
      </div>

      <div
        v-for="(track, index) in playlist.tracks"
        :key="track.id"
        @click="playerStore.playSong(track)"
        class="grid grid-cols-12 items-center px-4 py-2.5 rounded-xl hover:bg-melody-card/70 border border-transparent hover:border-melody-border/40 transition group cursor-pointer"
      >
        <!-- # / Play icon -->
        <div class="col-span-1 flex items-center justify-center">
          <span class="text-xs text-melody-muted group-hover:hidden font-medium">
            {{ index + 1 }}
          </span>
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-purple-400 hidden group-hover:block fill-current" viewBox="0 0 24 24">
            <path d="M8 5v14l11-7z" />
          </svg>
        </div>

        <!-- Title & Artist -->
        <div class="col-span-7 sm:col-span-6 flex items-center gap-3 min-w-0 pr-2">
          <img :src="track.cover" :alt="track.title" class="w-10 h-10 rounded-lg object-cover shadow shrink-0" />
          <div class="truncate">
            <p class="text-sm font-semibold text-white truncate group-hover:text-purple-300 transition-colors">
              {{ track.title }}
            </p>
            <p class="text-xs text-melody-muted truncate mt-0.5">
              {{ track.artist }}
            </p>
          </div>
        </div>

        <!-- Album / Category -->
        <div class="hidden sm:block sm:col-span-3 text-xs text-melody-muted truncate pr-2">
          {{ track.genre || playlist.title }}
        </div>

        <!-- Duration & Edit button -->
        <div class="col-span-4 sm:col-span-2 flex items-center justify-end gap-3 text-xs text-melody-muted font-mono">
          <span>{{ track.duration }}</span>
          <button
            @click.stop="openEditModal(track)"
            class="opacity-0 group-hover:opacity-100 p-1.5 rounded-lg text-melody-muted hover:text-white hover:bg-melody-card transition cursor-pointer"
            title="Edit song title & details"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Empty state when playlist has no songs -->
      <div v-if="playlist.tracks.length === 0" class="py-16 text-center text-melody-muted space-y-2">
        <div class="w-12 h-12 rounded-full bg-melody-card border border-melody-border/60 mx-auto flex items-center justify-center text-melody-muted">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-purple-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
          </svg>
        </div>
        <p class="text-sm font-semibold text-white">No tracks in {{ playlist.title }} yet</p>
        <p class="text-xs">Use the Add / Upload Track button to start adding songs to this category!</p>
      </div>
    </div>

    <!-- Edit Song Modal Component -->
    <EditSongModal
      :is-open="isEditModalOpen"
      :song="selectedSongForEdit"
      @close="isEditModalOpen = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore } from '@/stores/playerStore'
import EditSongModal from '@/components/music/EditSongModal.vue'

const route = useRoute()
const playerStore = usePlayerStore()

const isEditModalOpen = ref(false)
const selectedSongForEdit = ref(null)

function openEditModal(track) {
  selectedSongForEdit.value = track
  isEditModalOpen.value = true
}

const playlistsData = {
  1: {
    id: 1,
    title: 'Khmer',
    description: 'Traditional & contemporary Cambodian music, golden era classics, and modern rhythms.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=500&auto=format&fit=crop&q=80'
  },
  2: {
    id: 2,
    title: 'Chinese',
    description: 'Mandopop, classical Chinese instruments, and soothing acoustic melodies.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=500&auto=format&fit=crop&q=80'
  },
  3: {
    id: 3,
    title: 'France',
    description: 'French chanson, modern Parisian indie pop, and cafe vibes.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=500&auto=format&fit=crop&q=80'
  },
  4: {
    id: 4,
    title: 'English',
    description: 'Global pop hits, indie rock, R&B grooves, and acoustic favorites.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500&auto=format&fit=crop&q=80'
  },
  5: {
    id: 5,
    title: 'Phonk',
    description: 'Drift phonk, aggressive 808 sub-bass, cowbell rhythms, and night drives.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1508974239320-0a029497e820?w=500&auto=format&fit=crop&q=80'
  },
  6: {
    id: 6,
    title: 'Japan',
    description: 'City Pop, anime OSTs, J-Pop sensations, and Tokyo ambient soundscapes.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=500&auto=format&fit=crop&q=80'
  },
  7: {
    id: 7,
    title: 'Korea',
    description: 'K-Pop anthems, soulful Korean ballads, and vibrant Seoul nights.',
    creator: 'MelodyHub',
    cover: 'https://images.unsplash.com/photo-1538485399081-7191377e8241?w=500&auto=format&fit=crop&q=80'
  }
}

const playlist = computed(() => {
  const paramId = parseInt(route.params.id) || 1
  const base = playlistsData[paramId] || playlistsData[1]
  const catName = base.title.toLowerCase()

  // Match tracks belonging to this playlist
  const matchingTracks = playerStore.catalog.filter(s => {
    if (!s) return false
    const genre = (s.genre || '').toLowerCase()
    const title = (s.title || '').toLowerCase()
    const artist = (s.artist || '').toLowerCase()

    if (catName === 'korea') {
      return genre === 'korea' || genre.includes('k-pop') || genre.includes('kpop') ||
             title.includes('korea') || artist.includes('korea')
    }
    return genre === catName || genre.includes(catName) ||
           title.includes(catName) || artist.includes(catName)
  })

  return {
    ...base,
    tracks: matchingTracks
  }
})

function playAll() {
  if (playlist.value.tracks.length > 0) {
    playerStore.playSong(playlist.value.tracks[0], playlist.value.tracks)
  }
}

onMounted(() => {
  if (playerStore.catalog.length === 0) {
    playerStore.loadCatalogFromBackend()
  }
})
</script>
