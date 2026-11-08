<template>
  <div class="space-y-6 pb-12">
    <!-- Header with Library Filters -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">Your Library</h1>
        <p class="text-xs sm:text-sm text-melody-muted mt-0.5">Your playlists, saved tracks, and followed artists</p>
      </div>

      <!-- Filter Pills -->
      <div class="flex items-center gap-2 overflow-x-auto pb-1">
        <button
          v-for="tab in tabs"
          :key="tab"
          @click="activeTab = tab"
          class="px-4 py-1.5 rounded-full text-xs font-semibold transition shrink-0 cursor-pointer"
          :class="[
            activeTab === tab
              ? 'bg-white text-black'
              : 'bg-melody-card hover:bg-melody-cardHover text-white border border-melody-border/60'
          ]"
        >
          {{ tab }}
        </button>
      </div>
    </div>

    <!-- Playlists View -->
    <div v-if="activeTab === 'Playlists' || activeTab === 'All'" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
      <!-- Liked Songs Special Card -->
      <div
        @click="$router.push('/playlist/liked')"
        class="p-4 rounded-2xl bg-gradient-to-br from-purple-700 via-indigo-800 to-melody-card border border-purple-500/30 hover:border-purple-500/60 transition group cursor-pointer flex flex-col justify-between col-span-2 sm:col-span-1 shadow-lg"
      >
        <div class="space-y-2">
          <div class="w-10 h-10 rounded-xl bg-white/10 backdrop-blur flex items-center justify-center text-white">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current text-white" viewBox="0 0 24 24">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </div>
          <h3 class="text-lg font-bold text-white mt-8 group-hover:text-purple-200 transition-colors">
            Liked Songs
          </h3>
          <p class="text-xs text-purple-200/80">
            {{ likedCount }} songs saved
          </p>
        </div>
      </div>

      <!-- User Playlists -->
      <div
        v-for="pl in libraryPlaylists"
        :key="pl.id"
        @click="$router.push(`/playlist/${pl.id}`)"
        class="p-3.5 rounded-2xl bg-melody-card/60 hover:bg-melody-card border border-melody-border/40 hover:border-purple-500/40 transition group cursor-pointer flex flex-col"
      >
        <div class="relative aspect-square rounded-xl overflow-hidden mb-3 shadow-md">
          <img :src="pl.cover" :alt="pl.title" class="w-full h-full object-cover group-hover:scale-105 transition duration-500" />
        </div>
        <h4 class="font-semibold text-sm text-white truncate group-hover:text-purple-300 transition-colors">
          {{ pl.title }}
        </h4>
        <p class="text-xs text-melody-muted truncate mt-1">
          By {{ pl.creator }} • {{ pl.trackCount }} songs
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'

const playerStore = usePlayerStore()

const tabs = ['All', 'Playlists', 'Artists', 'Albums']
const activeTab = ref('All')
const likedCount = ref(0)

function getTrackCount(category) {
  const cat = category.toLowerCase()
  return playerStore.catalog.filter(s => {
    if (!s) return false
    const genre = (s.genre || '').toLowerCase()
    const title = (s.title || '').toLowerCase()
    const artist = (s.artist || '').toLowerCase()
    if (cat === 'korea') {
      return genre === 'korea' || genre.includes('k-pop') || genre.includes('kpop') ||
             title.includes('korea') || artist.includes('korea')
    }
    return genre === cat || genre.includes(cat) || title.includes(cat) || artist.includes(cat)
  }).length
}

const libraryPlaylists = computed(() => [
  {
    id: 1,
    title: 'Khmer',
    creator: 'You',
    trackCount: getTrackCount('Khmer'),
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 2,
    title: 'Chinese',
    creator: 'You',
    trackCount: getTrackCount('Chinese'),
    cover: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 3,
    title: 'France',
    creator: 'You',
    trackCount: getTrackCount('France'),
    cover: 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 4,
    title: 'English',
    creator: 'You',
    trackCount: getTrackCount('English'),
    cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 5,
    title: 'Phonk',
    creator: 'You',
    trackCount: getTrackCount('Phonk'),
    cover: 'https://images.unsplash.com/photo-1508974239320-0a029497e820?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 6,
    title: 'Japan',
    creator: 'You',
    trackCount: getTrackCount('Japan'),
    cover: 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 7,
    title: 'Korea',
    creator: 'You',
    trackCount: getTrackCount('Korea'),
    cover: 'https://images.unsplash.com/photo-1538485399081-7191377e8241?w=400&auto=format&fit=crop&q=80'
  }
])

onMounted(() => {
  if (playerStore.catalog.length === 0) {
    playerStore.loadCatalogFromBackend()
  }
})
</script>
