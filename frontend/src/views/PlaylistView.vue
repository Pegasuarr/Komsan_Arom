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

        <!-- Album -->
        <div class="hidden sm:block sm:col-span-4 text-xs text-melody-muted truncate pr-2">
          {{ track.album || 'Single' }}
        </div>

        <!-- Duration -->
        <div class="col-span-4 sm:col-span-1 text-right text-xs text-melody-muted">
          {{ track.duration }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'

const playerStore = usePlayerStore()

const playlist = ref({
  id: 1,
  title: 'Late Night Lo-Fi',
  description: 'Smooth atmospheric beats, vinyl crackles, and warm electric piano for deep concentration.',
  creator: 'MelodyHub Editorial',
  cover: 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=500&auto=format&fit=crop&q=80',
  tracks: [
    {
      id: 1,
      title: 'Midnight Reverie',
      artist: 'Aroma Beats',
      album: 'Midnight Echoes',
      duration: '3:15',
      cover: 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=300&auto=format&fit=crop&q=80',
      audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
    {
      id: 2,
      title: 'Phnom Penh After Dark',
      artist: 'VannDa',
      album: 'Skull 2',
      duration: '3:24',
      cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80',
      audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3'
    },
    {
      id: 3,
      title: 'Mekong River Sunset',
      artist: 'Sinn Sisamouth (Lo-Fi Rework)',
      album: 'Golden Era Revisited',
      duration: '4:12',
      cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80',
      audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3'
    },
    {
      id: 4,
      title: 'Electric Dreams',
      artist: 'Kroma Sound',
      album: 'Synthesized Soul',
      duration: '2:56',
      cover: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&auto=format&fit=crop&q=80',
      audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3'
    }
  ]
})

function playAll() {
  if (playlist.value.tracks.length > 0) {
    playerStore.playSong(playlist.value.tracks[0])
  }
}
</script>
