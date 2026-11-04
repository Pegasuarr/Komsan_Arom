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
        <div class="flex items-center gap-4 pt-2">
          <button
            @click="playFeaturedTrack"
            class="px-6 py-3 rounded-full bg-purple-600 hover:bg-purple-500 text-white font-semibold text-sm flex items-center gap-2.5 shadow-lg shadow-purple-600/30 transition hover:scale-105 cursor-pointer"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 fill-current" viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
            <span>Play Now</span>
          </button>
          <button
            class="px-6 py-3 rounded-full bg-melody-card/80 hover:bg-melody-card text-white font-semibold text-sm border border-melody-border/80 transition hover:border-purple-500/50 cursor-pointer"
          >
            Save to Library
          </button>
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

    <!-- Popular Artists -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <div>
          <h2 class="text-xl sm:text-2xl font-bold tracking-tight text-white">Popular Artists</h2>
          <p class="text-xs sm:text-sm text-melody-muted mt-0.5">Top performing artists in Cambodia and worldwide</p>
        </div>
      </div>

      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
        <div
          v-for="artist in popularArtists"
          :key="artist.id"
          class="p-4 rounded-2xl bg-melody-card/40 hover:bg-melody-card border border-melody-border/40 hover:border-purple-500/40 transition group cursor-pointer text-center"
        >
          <div class="w-24 h-24 mx-auto mb-3 rounded-full overflow-hidden shadow-lg border border-melody-border/50">
            <img
              :src="artist.image"
              :alt="artist.name"
              class="w-full h-full object-cover group-hover:scale-110 transition duration-500"
            />
          </div>
          <h3 class="font-semibold text-sm text-white truncate group-hover:text-purple-300 transition-colors">
            {{ artist.name }}
          </h3>
          <p class="text-xs text-melody-muted mt-0.5">
            {{ artist.monthlyListeners }}
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
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'

const playerStore = usePlayerStore()

// High quality, curated royalty-free music photography
const quickPicks = ref([
  {
    id: 101,
    title: 'Phnom Penh After Dark',
    artist: 'VannDa',
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
  },
  {
    id: 102,
    title: 'Mekong River Sunset',
    artist: 'Sinn Sisamouth (Lo-Fi Rework)',
    cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3'
  },
  {
    id: 103,
    title: 'Electric Dreams',
    artist: 'Kroma Sound',
    cover: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3'
  },
  {
    id: 104,
    title: 'Angkor Ambient',
    artist: 'Chhom Nimol',
    cover: 'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3'
  },
  {
    id: 105,
    title: 'Acoustic Rain',
    artist: 'Laura Jansen',
    cover: 'https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3'
  },
  {
    id: 106,
    title: 'Neon Skyline',
    artist: 'Solaris',
    cover: 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=300&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3'
  }
])

const trendingSongs = ref([
  {
    id: 201,
    title: 'Midnight Echoes',
    artist: 'Aroma Beats',
    cover: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3'
  },
  {
    id: 202,
    title: 'Time & Tide',
    artist: 'VannDa ft. Sophia Kao',
    cover: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=400&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3'
  },
  {
    id: 203,
    title: 'Sihanoukville Waves',
    artist: 'Ocean Groove',
    cover: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3'
  },
  {
    id: 204,
    title: 'Silver Moon',
    artist: 'G-Devith',
    cover: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3'
  },
  {
    id: 205,
    title: 'Starlight Rhythm',
    artist: 'KlapYaHandz',
    cover: 'https://images.unsplash.com/photo-1459749411175-04bf5292ceea?w=400&auto=format&fit=crop&q=80',
    audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
  }
])

const popularArtists = ref([
  {
    id: 1,
    name: 'VannDa',
    monthlyListeners: '1.4M listeners',
    image: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300&auto=format&fit=crop&q=80'
  },
  {
    id: 2,
    name: 'Sophia Kao',
    monthlyListeners: '890K listeners',
    image: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80'
  },
  {
    id: 3,
    name: 'G-Devith',
    monthlyListeners: '950K listeners',
    image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80'
  },
  {
    id: 4,
    name: 'KlapYaHandz',
    monthlyListeners: '720K listeners',
    image: 'https://images.unsplash.com/photo-1501196354995-cbb51c65aaea?w=300&auto=format&fit=crop&q=80'
  },
  {
    id: 5,
    name: 'Laura Jansen',
    monthlyListeners: '1.1M listeners',
    image: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&auto=format&fit=crop&q=80'
  },
  {
    id: 6,
    name: 'Aroma Beats',
    monthlyListeners: '640K listeners',
    image: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=300&auto=format&fit=crop&q=80'
  }
])

const playlists = ref([
  {
    id: 1,
    title: 'Khmer Hip-Hop Essentials',
    description: 'The defining anthems of modern Cambodian urban sound and rhyme.',
    cover: 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 2,
    title: 'Deep Focus Instrumental',
    description: 'Minimalist electronic and ambient textures designed for deep workflow.',
    cover: 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 3,
    title: 'Phnom Penh Sunset Chill',
    description: 'Warm grooves, lo-fi beats, and relaxing chords for the evening unwind.',
    cover: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop&q=80'
  },
  {
    id: 4,
    title: 'Cardio & Electronic Rush',
    description: 'High tempo energetic beats to power your morning routine and workouts.',
    cover: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400&auto=format&fit=crop&q=80'
  }
])

function playFeaturedTrack() {
  playerStore.playSong({
    id: 999,
    title: 'Late Night Sessions',
    artist: 'MelodyHub Originals',
    cover: 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80'
  })
}

function selectTrack(track) {
  playerStore.playSong(track)
}
</script>
