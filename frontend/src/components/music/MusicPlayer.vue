<template>
  <div class="fixed bottom-0 left-0 right-0 z-40 h-20 sm:h-22 bg-melody-dark/95 backdrop-blur-xl border-t border-melody-border/60 px-4 sm:px-6 flex items-center justify-between transition-all">
    <!-- Hidden HTML5 Audio Element that executes real audio streaming & playback -->
    <audio
      ref="audioRef"
      :src="playerStore.currentSong?.audioUrl"
      preload="metadata"
      @timeupdate="onTimeUpdate"
      @loadedmetadata="onLoadedMetadata"
      @ended="onEnded"
      @play="playerStore.isPlaying = true"
      @pause="playerStore.isPlaying = false"
      @error="onAudioError"
    ></audio>

    <!-- 1. Left: Track Info & Thumbnail -->
    <div class="flex items-center gap-3 w-1/4 min-w-[140px]">
      <div class="relative w-12 h-12 rounded-lg bg-melody-card border border-melody-border/40 overflow-hidden flex items-center justify-center shrink-0 shadow-md">
        <img
          v-if="playerStore.currentSong?.cover"
          :src="playerStore.currentSong.cover"
          :alt="playerStore.currentSong?.title"
          class="w-full h-full object-cover"
        />
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-purple-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
        </svg>

        <!-- Pulsing play indicator over cover when playing -->
        <span
          v-if="playerStore.isPlaying"
          class="absolute inset-0 bg-purple-900/20 flex items-center justify-center pointer-events-none"
        >
          <span class="w-2 h-2 rounded-full bg-purple-400 animate-ping"></span>
        </span>
      </div>

      <div class="truncate flex-1">
        <p class="text-xs sm:text-sm font-semibold text-white truncate hover:underline cursor-pointer">
          {{ playerStore.currentSong?.title || 'No track selected' }}
        </p>
        <p class="text-[11px] text-melody-muted truncate mt-0.5">
          {{ playerStore.currentSong?.artist || 'MelodyHub' }}
        </p>
      </div>

      <!-- Quick Like Heart Button -->
      <button
        @click="isFavorite = !isFavorite"
        class="hidden sm:block text-melody-muted hover:text-red-500 transition cursor-pointer p-1"
        :class="{ 'text-red-500 fill-current': isFavorite }"
        title="Save to Favorites"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" :fill="isFavorite ? 'currentColor' : 'none'" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
        </svg>
      </button>

      <!-- Quick Edit Song Title Button -->
      <button
        v-if="playerStore.currentSong?.id"
        @click="isEditModalOpen = true"
        class="text-melody-muted hover:text-purple-400 transition cursor-pointer p-1"
        title="Edit Song Title & Details"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
        </svg>
      </button>
    </div>

    <!-- 2. Center: Controls & Interactive Timeline Scrubber -->
    <div class="flex flex-col items-center gap-1.5 w-2/4 max-w-xl px-2">
      <div class="flex items-center gap-4 sm:gap-6">
        <!-- Shuffle Button -->
        <button
          @click="playerStore.toggleShuffle"
          class="transition cursor-pointer p-1"
          :class="playerStore.isShuffle ? 'text-purple-400' : 'text-melody-muted hover:text-white'"
          title="Shuffle"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
          </svg>
        </button>

        <!-- Previous Button -->
        <button
          @click="playerStore.prevSong"
          class="text-melody-muted hover:text-white transition cursor-pointer p-1"
          title="Previous Track"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
            <path d="M6 6h2v12H6zm3.5 6l8.5 6V6z"/>
          </svg>
        </button>

        <!-- Main Play/Pause Button -->
        <button
          @click="playerStore.togglePlay"
          class="w-10 h-10 rounded-full bg-white hover:bg-neutral-200 text-black flex items-center justify-center shadow-lg shadow-white/10 transition-transform active:scale-95 hover:scale-105 cursor-pointer"
          :title="playerStore.isPlaying ? 'Pause' : 'Play'"
        >
          <svg v-if="!playerStore.isPlaying" xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current translate-x-0.5" viewBox="0 0 24 24">
            <path d="M8 5v14l11-7z"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
            <path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z"/>
          </svg>
        </button>

        <!-- Next Button -->
        <button
          @click="playerStore.nextSong"
          class="text-melody-muted hover:text-white transition cursor-pointer p-1"
          title="Next Track"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 fill-current" viewBox="0 0 24 24">
            <path d="M6 18l8.5-6L6 6v12zM16 6v12h2V6h-2z"/>
          </svg>
        </button>

        <!-- Repeat Button -->
        <button
          @click="playerStore.toggleRepeat"
          class="transition cursor-pointer p-1"
          :class="playerStore.isRepeat ? 'text-purple-400' : 'text-melody-muted hover:text-white'"
          title="Repeat Track"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
        </button>
      </div>

      <!-- Live Timeline Progress Scrubber with Range Slider -->
      <div class="w-full flex items-center gap-2.5 text-[11px] text-melody-muted select-none">
        <span class="w-9 text-right font-mono">{{ playerStore.formatTime(playerStore.currentTime) }}</span>

        <div class="relative flex-1 flex items-center group">
          <!-- Visible progress track styling -->
          <div class="w-full h-1 bg-melody-border/60 rounded-full overflow-hidden">
            <div
              class="h-full bg-purple-500 group-hover:bg-purple-400 rounded-full transition-all"
              :style="{ width: `${playerStore.progressPercentage}%` }"
            ></div>
          </div>

          <!-- HTML5 interactive range input for seeking -->
          <input
            type="range"
            min="0"
            :max="playerStore.duration || 100"
            step="0.1"
            :value="playerStore.currentTime"
            @input="onScrubTimeline"
            class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
            title="Seek timeline"
          />
        </div>

        <span class="w-9 text-left font-mono">{{ playerStore.formatTime(playerStore.duration) }}</span>
      </div>
    </div>

    <!-- 3. Right: Volume Controller & Mute Toggle -->
    <div class="hidden sm:flex items-center justify-end gap-3 w-1/4">
      <button
        @click="playerStore.toggleMute"
        class="text-melody-muted hover:text-white transition cursor-pointer p-1"
        :title="playerStore.isMuted ? 'Unmute' : 'Mute'"
      >
        <!-- Muted Icon -->
        <svg v-if="playerStore.isMuted || playerStore.volume === 0" xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.586 15H4a1 1 0 01-1-1v-4a1 1 0 011-1h1.586l4.707-4.707C10.923 3.663 12 4.109 12 5v14c0 .891-1.077 1.337-1.707.707L5.586 15z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2" />
        </svg>
        <!-- High Volume Icon -->
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.536 8.464a5 5 0 010 7.072m2.828-9.9a9 9 0 010 12.728M5.586 15H4a1 1 0 01-1-1v-4a1 1 0 011-1h1.586l4.707-4.707C10.923 3.663 12 4.109 12 5v14c0 .891-1.077 1.337-1.707.707L5.586 15z" />
        </svg>
      </button>

      <!-- Volume Slider -->
      <div class="relative w-24 flex items-center group">
        <div class="w-full h-1 bg-melody-border/60 rounded-full overflow-hidden">
          <div
            class="h-full bg-white group-hover:bg-purple-400 rounded-full transition-all"
            :style="{ width: `${playerStore.isMuted ? 0 : playerStore.volume * 100}%` }"
          ></div>
        </div>
        <input
          type="range"
          min="0"
          max="1"
          step="0.01"
          :value="playerStore.isMuted ? 0 : playerStore.volume"
          @input="onVolumeChange"
          class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
          title="Adjust volume"
        />
      </div>

      <span class="text-[10px] text-melody-muted font-mono w-7 text-right">
        {{ Math.round((playerStore.isMuted ? 0 : playerStore.volume) * 100) }}%
      </span>

      <!-- Open Track in Telegram Bot -->
      <a
        :href="telegramTrackUrl"
        target="_blank"
        rel="noopener noreferrer"
        class="p-1.5 rounded-lg text-melody-muted hover:text-[#2AABEE] hover:bg-melody-card transition cursor-pointer"
        title="Listen to this track in Telegram Bot"
      >
        <TelegramIcon custom-class="w-4 h-4 text-[#2AABEE] hover:scale-110 transition-transform" />
      </a>
    </div>

    <!-- Edit Song Details Modal -->
    <EditSongModal
      :is-open="isEditModalOpen"
      :song="playerStore.currentSong"
      @close="isEditModalOpen = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'
import { getTelegramSongLink } from '@/config/telegram'
import TelegramIcon from '@/components/common/TelegramIcon.vue'
import EditSongModal from '@/components/music/EditSongModal.vue'

const playerStore = usePlayerStore()
const audioRef = ref(null)
const isFavorite = ref(false)
const isEditModalOpen = ref(false)

const telegramTrackUrl = computed(() => {
  return getTelegramSongLink(playerStore.currentSong?.id)
})

// --- Audio Element Event Handlers ---
function onTimeUpdate() {
  if (audioRef.value) {
    playerStore.currentTime = audioRef.value.currentTime
  }
}

function onLoadedMetadata() {
  if (audioRef.value) {
    playerStore.duration = audioRef.value.duration
    // Sync initial volume and mute
    audioRef.value.volume = playerStore.volume
    audioRef.value.muted = playerStore.isMuted

    if (playerStore.isPlaying) {
      audioRef.value.play().catch((err) => {
        console.warn('Playback interrupted or autoplay blocked by browser:', err.message)
      })
    }
  }
}

function onEnded() {
  if (playerStore.isRepeat) {
    if (audioRef.value) {
      audioRef.value.currentTime = 0
      audioRef.value.play()
    }
  } else {
    playerStore.nextSong()
  }
}

function onAudioError(e) {
  console.warn('Audio playback error:', e)
}

function onScrubTimeline(e) {
  const targetSeconds = parseFloat(e.target.value)
  playerStore.seekTo(targetSeconds)
  if (audioRef.value) {
    audioRef.value.currentTime = targetSeconds
  }
}

function onVolumeChange(e) {
  const newVol = parseFloat(e.target.value)
  playerStore.setVolume(newVol)
  if (audioRef.value) {
    audioRef.value.volume = newVol
    audioRef.value.muted = false
  }
}

// --- Watchers to sync Store -> Audio element ---
watch(
  () => playerStore.isPlaying,
  (play) => {
    if (!audioRef.value) return
    if (play) {
      audioRef.value.play().catch((err) => {
        console.warn('Autoplay prevented:', err.message)
        playerStore.isPlaying = false
      })
    } else {
      audioRef.value.pause()
    }
  }
)

watch(
  () => playerStore.currentSong?.audioUrl,
  (newUrl) => {
    if (audioRef.value && newUrl) {
      audioRef.value.src = newUrl
      audioRef.value.load()
      if (playerStore.isPlaying) {
        audioRef.value.play().catch(() => {})
      }
    }
  }
)

watch(
  () => playerStore.volume,
  (newVol) => {
    if (audioRef.value) {
      audioRef.value.volume = newVol
    }
  }
)

watch(
  () => playerStore.isMuted,
  (muted) => {
    if (audioRef.value) {
      audioRef.value.muted = muted
    }
  }
)

watch(
  () => playerStore.seekTarget,
  (targetTime) => {
    if (targetTime !== null && audioRef.value) {
      audioRef.value.currentTime = targetTime
      playerStore.seekTarget = null
    }
  }
)

onMounted(() => {
  if (audioRef.value) {
    audioRef.value.volume = playerStore.volume
    audioRef.value.muted = playerStore.isMuted
  }
})
</script>

