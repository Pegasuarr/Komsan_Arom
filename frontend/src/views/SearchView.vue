<template>
  <div class="space-y-8 pb-12">
    <!-- Search Bar Input Section -->
    <div class="relative max-w-2xl">
      <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-melody-muted">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Search for songs, artists, albums, or genres..."
        class="w-full pl-12 pr-4 py-3.5 rounded-2xl bg-melody-card border border-melody-border/80 text-white placeholder-melody-muted focus:outline-none focus:border-purple-500 focus:ring-1 focus:ring-purple-500/50 transition text-sm sm:text-base shadow-lg"
      />
      <button
        v-if="searchQuery"
        @click="searchQuery = ''"
        class="absolute inset-y-0 right-0 pr-4 flex items-center text-melody-muted hover:text-white transition cursor-pointer"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>

    <!-- Live Search Results (When typing) -->
    <section v-if="searchQuery.trim()" class="space-y-4">
      <h2 class="text-xl font-bold text-white tracking-tight">
        Results for "<span class="text-purple-400">{{ searchQuery }}</span>"
      </h2>

      <!-- Songs Results List -->
      <div v-if="filteredTracks.length > 0" class="space-y-1">
        <div
          v-for="(track, index) in filteredTracks"
          :key="track.id"
          @click="playerStore.playSong(track)"
          class="flex items-center gap-4 p-3 rounded-xl hover:bg-melody-card/70 border border-transparent hover:border-melody-border/40 transition group cursor-pointer"
        >
          <span class="w-6 text-center text-xs font-medium text-melody-muted group-hover:hidden">
            {{ index + 1 }}
          </span>
          <button class="w-6 text-center hidden group-hover:flex items-center justify-center text-purple-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 fill-current" viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
          </button>

          <img :src="track.cover" :alt="track.title" class="w-11 h-11 rounded-lg object-cover shadow shrink-0" />

          <div class="flex-1 min-w-0">
            <h4 class="text-sm font-semibold text-white truncate group-hover:text-purple-300 transition-colors">
              {{ track.title }}
            </h4>
            <p class="text-xs text-melody-muted truncate mt-0.5">
              {{ track.artist }}
            </p>
          </div>

          <span class="text-xs text-melody-muted shrink-0">{{ track.duration }}</span>
        </div>
      </div>

      <!-- Empty State when no results found -->
      <div v-else class="text-center py-16 space-y-3">
        <div class="w-12 h-12 mx-auto rounded-full bg-melody-card border border-melody-border/60 flex items-center justify-center text-melody-muted">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <h3 class="text-base font-semibold text-white">No results found</h3>
        <p class="text-xs text-melody-muted max-w-sm mx-auto">
          Please check your spelling or search for another artist, song, or genre.
        </p>
      </div>
    </section>

    <!-- Browse Genres & Categories (When not searching) -->
    <section v-else class="space-y-4">
      <h2 class="text-xl sm:text-2xl font-bold tracking-tight text-white">Browse All</h2>

      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
        <div
          v-for="cat in categories"
          :key="cat.id"
          :style="{ backgroundColor: cat.color }"
          class="relative h-32 rounded-2xl p-4 overflow-hidden shadow-lg transition-transform hover:scale-[1.02] cursor-pointer"
        >
          <h3 class="font-bold text-lg text-white leading-tight">
            {{ cat.name }}
          </h3>
          <div class="absolute -right-3 -bottom-3 w-18 h-18 rotate-[25deg] shadow-xl overflow-hidden rounded-lg">
            <img :src="cat.image" :alt="cat.name" class="w-full h-full object-cover" />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore } from '@/stores/playerStore'

const route = useRoute()
const playerStore = usePlayerStore()

const searchQuery = ref(route.query.q || '')

// Authentic categories matching music genres
const categories = ref([
  { id: 1, name: 'Khmer Pop', color: '#7c3aed', image: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=200&auto=format&fit=crop&q=80' },
  { id: 2, name: 'Hip-Hop & Rap', color: '#db2777', image: 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=200&auto=format&fit=crop&q=80' },
  { id: 3, name: 'Deep Focus & Lo-Fi', color: '#2563eb', image: 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=200&auto=format&fit=crop&q=80' },
  { id: 4, name: 'Acoustic & Folk', color: '#059669', image: 'https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=200&auto=format&fit=crop&q=80' },
  { id: 5, name: 'Electronic / Dance', color: '#ea580c', image: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&auto=format&fit=crop&q=80' },
  { id: 6, name: 'Rock & Alternative', color: '#dc2626', image: 'https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=200&auto=format&fit=crop&q=80' },
  { id: 7, name: 'Chill & Ambient', color: '#0891b2', image: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&auto=format&fit=crop&q=80' },
  { id: 8, name: 'Indie Discoveries', color: '#9333ea', image: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=200&auto=format&fit=crop&q=80' }
])

const allTracks = ref([
  { id: 1, title: 'Phnom Penh After Dark', artist: 'VannDa', duration: '3:24', cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80' },
  { id: 2, title: 'Mekong River Sunset', artist: 'Sinn Sisamouth (Lo-Fi Rework)', duration: '4:12', cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80' },
  { id: 3, title: 'Electric Dreams', artist: 'Kroma Sound', duration: '2:56', cover: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&auto=format&fit=crop&q=80' },
  { id: 4, title: 'Angkor Ambient', artist: 'Chhom Nimol', duration: '5:02', cover: 'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=300&auto=format&fit=crop&q=80' },
  { id: 5, title: 'Time & Tide', artist: 'Sophia Kao', duration: '3:45', cover: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=400&auto=format&fit=crop&q=80' },
  { id: 6, title: 'Silver Moon', artist: 'G-Devith', duration: '3:18', cover: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&auto=format&fit=crop&q=80' }
])

const filteredTracks = computed(() => {
  if (!searchQuery.value.trim()) return []
  const q = searchQuery.value.toLowerCase()
  return allTracks.value.filter(t =>
    t.title.toLowerCase().includes(q) || t.artist.toLowerCase().includes(q)
  )
})
</script>
