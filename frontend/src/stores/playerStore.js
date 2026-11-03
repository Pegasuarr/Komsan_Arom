import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * Player Store: Manages global audio playback state.
 * Using Pinia's "Setup Store" syntax (similar to Vue 3 <script setup>).
 */
export const usePlayerStore = defineStore('player', () => {
  // --- Reactive State ---
  const currentSong = ref(null)
  const isPlaying = ref(false)
  const currentTime = ref(0)
  const duration = ref(0)
  const volume = ref(0.8) // 0.0 to 1.0
  const isMuted = ref(false)
  const isShuffle = ref(false)
  const isRepeat = ref(false)
  const queue = ref([])
  const queueIndex = ref(0)

  // --- Computed (Getters) ---
  const hasCurrentSong = computed(() => currentSong.value !== null)
  const progressPercentage = computed(() => {
    if (!duration.value || duration.value === 0) return 0
    return (currentTime.value / duration.value) * 100
  })

  // --- Actions ---
  function playSong(song) {
    currentSong.value = song
    isPlaying.value = true
  }

  function togglePlay() {
    if (!currentSong.value) return
    isPlaying.value = !isPlaying.value
  }

  function setVolume(newVolume) {
    volume.value = Math.max(0, Math.min(1, newVolume))
  }

  return {
    // State
    currentSong,
    isPlaying,
    currentTime,
    duration,
    volume,
    isMuted,
    isShuffle,
    isRepeat,
    queue,
    queueIndex,
    // Computed
    hasCurrentSong,
    progressPercentage,
    // Actions
    playSong,
    togglePlay,
    setVolume
  }
})

