<template>
  <div class="space-y-10 pb-12">
    <!-- Featured Hero Banner -->
    <section class="relative overflow-hidden rounded-3xl bg-gradient-to-r from-purple-900/80 via-indigo-950 to-melody-card p-6 sm:p-10 border border-purple-500/20 shadow-2xl">
      <div class="relative z-10 max-w-xl space-y-4">
        <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-purple-500/20 text-purple-300 border border-purple-500/30 tracking-wide uppercase">
          Featured Release
        </span>
        <h1 class="text-3xl sm:text-5xl font-black tracking-tight text-white leading-tight">
          Late Night Sessions
        </h1>
        <p class="text-melody-muted text-sm sm:text-base leading-relaxed">
          Immerse yourself in deep ambient chords, smooth rhythm sections, and curated sounds crafted for pure focus.
        </p>
        <div class="flex flex-wrap items-center gap-4 pt-2">
          <button
            @click="playFeaturedTrack"
            class="px-6 py-3 rounded-full bg-purple-600 hover:bg-purple-500 text-white font-semibold text-sm flex items-center gap-2.5 shadow-lg shadow-purple-600/30 transition hover:scale-105 active:scale-95 cursor-pointer"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 fill-current" viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
            <span>Play Now</span>
          </button>
          <button
            @click="isUploadOpen = true"
            class="px-6 py-3 rounded-full bg-melody-card/80 hover:bg-melody-card text-white font-semibold text-sm border border-melody-border/80 transition hover:border-purple-500/50 cursor-pointer flex items-center gap-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>Add / Upload Track</span>
          </button>
          <a
            :href="TELEGRAM_BOT_URL"
            target="_blank"
            rel="noopener noreferrer"
            class="px-5 py-3 rounded-full bg-[#2AABEE]/15 hover:bg-[#2AABEE]/30 border border-[#2AABEE]/40 text-white font-semibold text-sm transition hover:scale-105 active:scale-95 cursor-pointer flex items-center gap-2 group"
          >
            <TelegramIcon custom-class="w-4 h-4 text-[#2AABEE] group-hover:scale-110 transition-transform" />
            <span class="text-sky-200 group-hover:text-white">Listen on Telegram</span>
          </a>
        </div>
      </div>

      <!-- Ambient glow decor -->
      <div class="absolute -right-16 -bottom-16 w-80 h-80 bg-purple-600/25 rounded-full blur-3xl pointer-events-none"></div>
    </section>

    <!-- Quick Play / Recently Played Grid -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-xl sm:text-2xl font-bold tracking-tight text-white">Good evening</h2>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <div
          v-for="item in quickPicks"
          :key="item.id"
          @click="selectTrack(item)"
          class="flex items-center gap-4 p-2 rounded-xl bg-melody-card/70 hover:bg-melody-card border border-melody-border/40 hover:border-purple-500/40 transition group cursor-pointer shadow-sm"
        >
          <img
            :src="item.cover"
            :alt="item.title"
            class="w-14 h-14 rounded-lg object-cover shadow shrink-0"
          />
          <div class="flex-1 min-w-0">
            <h3 class="text-sm font-semibold text-white truncate group-hover:text-purple-300 transition-colors">
              {{ item.title }}
            </h3>
            <p class="text-xs text-melody-muted truncate mt-0.5">
              {{ item.artist }}
            </p>
          </div>
          <!-- Edit button -->
          <button
            @click.stop="openEditModal(item)"
            class="w-8 h-8 rounded-full bg-melody-dark/80 hover:bg-purple-600 text-melody-muted hover:text-white flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all mr-1 shrink-0 cursor-pointer"
            title="Edit song title & details"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
          </button>
          <button
            class="w-10 h-10 rounded-full bg-purple-600 text-white flex items-center justify-center opacity-0 group-hover:opacity-100 group-hover:scale-105 transition-all shadow-md mr-2 shrink-0 cursor-pointer"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 fill-current translate-x-0.5" viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
          </button>
        </div>
      </div>
    </section>

    <!-- Trending Songs -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <div>
          <h2 class="text-xl sm:text-2xl font-bold tracking-tight text-white">Trending Now</h2>
          <p class="text-xs sm:text-sm text-melody-muted mt-0.5">The most played tracks across Komsan Arom this week</p>
        </div>
        <router-link to="/search" class="text-xs font-semibold text-purple-400 hover:text-purple-300 transition">
          See all
        </router-link>
      </div>

      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
        <div
          v-for="song in trendingSongs"
          :key="song.id"
          @click="selectTrack(song)"
          class="p-3.5 rounded-2xl bg-melody-card/60 hover:bg-melody-card border border-melody-border/40 hover:border-purple-500/40 transition-all duration-300 group cursor-pointer flex flex-col"
        >
          <div class="relative aspect-square rounded-xl overflow-hidden mb-3.5 shadow-md">
            <img
              :src="song.cover"
              :alt="song.title"
              class="w-full h-full object-cover group-hover:scale-105 transition duration-500"
            />
            <!-- Edit Button on Card -->
            <button
              @click.stop="openEditModal(song)"
              class="absolute top-2.5 right-2.5 w-8 h-8 rounded-full bg-black/60 hover:bg-purple-600 text-white flex items-center justify-center shadow-lg opacity-0 group-hover:opacity-100 transition-all hover:scale-110 cursor-pointer"
              title="Edit song title & details"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
              </svg>
            </button>
            <button
              class="absolute bottom-2.5 right-2.5 w-10 h-10 rounded-full bg-purple-600 text-white flex items-center justify-center shadow-xl opacity-0 translate-y-2 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-200 hover:scale-105 cursor-pointer"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 fill-current translate-x-0.5" viewBox="0 0 24 24">
                <path d="M8 5v14l11-7z" />
              </svg>
            </button>
          </div>
          <h3 class="font-semibold text-sm text-white truncate group-hover:text-purple-300 transition-colors">
            {{ song.title }}
          </h3>
          <p class="text-xs text-melody-muted truncate mt-1">
            {{ song.artist }}
          </p>
        </div>
      </div>
    </section>

    <!-- Recommended Playlists -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <div>
          <h2 class="text-xl sm:text-2xl font-bold tracking-tight text-white">Curated Playlists</h2>
          <p class="text-xs sm:text-sm text-melody-muted mt-0.5">Handpicked for studying, commuting, and relaxing</p>
        </div>
      </div>

      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <div
          v-for="playlist in playlists"
          :key="playlist.id"
          @click="$router.push(`/playlist/${playlist.id}`)"
          class="p-4 rounded-2xl bg-melody-card/60 hover:bg-melody-card border border-melody-border/40 hover:border-purple-500/40 transition group cursor-pointer flex flex-col"
        >
          <div class="relative aspect-square rounded-xl overflow-hidden mb-3.5 shadow-md">
            <img
              :src="playlist.cover"
              :alt="playlist.title"
              class="w-full h-full object-cover group-hover:scale-105 transition duration-500"
            />
          </div>
          <h3 class="font-semibold text-sm text-white truncate group-hover:text-purple-300 transition-colors">
            {{ playlist.title }}
          </h3>
          <p class="text-xs text-melody-muted line-clamp-2 mt-1 leading-relaxed">
            {{ playlist.description }}
          </p>
        </div>
      </div>
    </section>
    <!-- Upload Song Modal Component -->
    <UploadSongModal
      :is-open="isUploadOpen"
      @close="isUploadOpen = false"
      @song-added="handleSongAdded"
    />

    <!-- Edit Song Modal Component -->
    <EditSongModal
      :is-open="isEditModalOpen"
      :song="selectedSongForEdit"
      @close="isEditModalOpen = false"
      @updated="handleSongUpdated"
      @deleted="handleSongDeleted"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'
import songService from '@/services/songService'
import UploadSongModal from '@/components/music/UploadSongModal.vue'
import EditSongModal from '@/components/music/EditSongModal.vue'
import TelegramIcon from '@/components/common/TelegramIcon.vue'
import { TELEGRAM_BOT_URL } from '@/config/telegram'

const playerStore = usePlayerStore()
const isUploadOpen = ref(false)
const isEditModalOpen = ref(false)
const selectedSongForEdit = ref(null)

function openEditModal(song) {
  selectedSongForEdit.value = song
  isEditModalOpen.value = true
}

function handleSongUpdated(updated) {
  // Update in quickPicks
  const qIdx = quickPicks.value.findIndex(s => s.id === updated.id)
  if (qIdx !== -1) {
    quickPicks.value[qIdx] = { ...quickPicks.value[qIdx], ...updated }
  }
  // Update in trendingSongs
  const tIdx = trendingSongs.value.findIndex(s => s.id === updated.id)
  if (tIdx !== -1) {
    trendingSongs.value[tIdx] = { ...trendingSongs.value[tIdx], ...updated }
  }
}

function handleSongDeleted(songId) {
  quickPicks.value = quickPicks.value.filter(s => s.id !== songId)
  trendingSongs.value = trendingSongs.value.filter(s => s.id !== songId)
}

// High quality, curated royalty-free music photography
const quickPicks = ref([])
const trendingSongs = ref([])

const playlists = ref([
  {
    id: 1,
    title: 'Khmer',
    description: 'Traditional & contemporary Cambodian music, golden era classics, and modern rhythms.',
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 2,
    title: 'Chinese',
    description: 'Mandopop, classical Chinese instruments, and soothing acoustic melodies.',
    cover: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 3,
    title: 'France',
    description: 'French chanson, modern Parisian indie pop, and cafe vibes.',
    cover: 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 4,
    title: 'English',
    description: 'Global pop hits, indie rock, R&B grooves, and acoustic favorites.',
    cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 5,
    title: 'Phonk',
    description: 'Drift phonk, aggressive 808 sub-bass, cowbell rhythms, and night drives.',
    cover: 'https://images.unsplash.com/photo-1508974239320-0a029497e820?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 6,
    title: 'Japan',
    description: 'City Pop, anime OSTs, J-Pop sensations, and Tokyo ambient soundscapes.',
    cover: 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 7,
    title: 'Korea',
    description: 'K-Pop anthems, soulful Korean ballads, and vibrant Seoul nights.',
    cover: 'https://images.unsplash.com/photo-1538485399081-7191377e8241?w=400&auto=format&fit=crop&q=80'
  }
])

function playFeaturedTrack() {
  playerStore.playSong({
    id: 999,
    title: 'Late Night Sessions',
    artist: 'MelodyHub Originals',
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3',
    duration: '6:12'
  })
}

function handleSongAdded(newSong) {
  trendingSongs.value.unshift({
    id: newSong.id,
    title: newSong.title,
    artist: newSong.artist,
    cover: newSong.cover,
    audioUrl: newSong.audioUrl,
    duration: newSong.duration
  })
  quickPicks.value.unshift({
    id: newSong.id,
    title: newSong.title,
    artist: newSong.artist,
    cover: newSong.cover,
    audioUrl: newSong.audioUrl
  })
}

function mapSong(s) {
  return {
    id: s.id,
    title: s.title,
    artist: s.artist,
    duration: s.duration || '3:30',
    audioUrl: s.audioUrl ? (s.audioUrl.startsWith('http') ? s.audioUrl : `http://localhost:8085${s.audioUrl}`) : '',
    cover: s.cover ? (s.cover.startsWith('http') ? s.cover : `http://localhost:8085${s.cover}`) : 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80'
  }
}

function selectTrack(track) {
  playerStore.playSong(track)
  if (track.id) {
    songService.recordStream(track.id).catch(() => {})
  }
}

onMounted(async () => {
  playerStore.loadCatalogFromBackend()
  try {
    const [quickRes, trendRes] = await Promise.all([
      songService.getQuickPicks(),
      songService.getTrending()
    ])
    if (quickRes.data && quickRes.data.length > 0) quickPicks.value = quickRes.data.map(mapSong)
    if (trendRes.data && trendRes.data.length > 0) trendingSongs.value = trendRes.data.map(mapSong)
  } catch (err) {
    // If backend is still booting, fallback gracefully
  }
})
</script>
