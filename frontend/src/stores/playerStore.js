import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import songService from '@/services/songService'

/**
 * Player Store: Manages global audio playback, queue, and catalog state.
 * Implements full reactive playback controls for the MelodyHub audio engine.
 */
export const usePlayerStore = defineStore('player', () => {
  // --- Reactive State ---
  const currentSong = ref(null)
  const isPlaying = ref(false)
  const currentTime = ref(0)
  const duration = ref(0)
  const volume = ref(0.8) // Range 0.0 to 1.0
  const isMuted = ref(false)
  const isShuffle = ref(false)
  const isRepeat = ref(false)
  const queue = ref([])
  const queueIndex = ref(0)
  const seekTarget = ref(null) // Signal to seek the HTML5 audio element

  // Catalog contains only user-added / real database songs
  const catalog = ref([])

  // --- Computed Properties ---
  const hasCurrentSong = computed(() => currentSong.value !== null)

  const progressPercentage = computed(() => {
    if (!duration.value || duration.value === 0) return 0
    return Math.min(100, Math.max(0, (currentTime.value / duration.value) * 100))
  })

  // --- Formatting Helper ---
  function formatTime(seconds) {
    if (isNaN(seconds) || seconds < 0) return '0:00'
    const mins = Math.floor(seconds / 60)
    const secs = Math.floor(seconds % 60)
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`
  }

  // --- Actions ---
  async function loadCatalogFromBackend() {
    try {
      const res = await songService.getAllSongs()
      if (res.data) {
        const songList = Array.isArray(res.data) ? res.data : (res.data.content || [])
        const formatted = songList.map(s => ({
          id: s.id,
          title: s.title,
          artist: s.artist,
          genre: s.genre || 'Khmer',
          duration: s.duration || '3:30',
          audioUrl: s.audioUrl ? (s.audioUrl.startsWith('http') ? s.audioUrl : `http://localhost:8085${s.audioUrl}`) : '',
          cover: s.cover ? (s.cover.startsWith('http') ? s.cover : `http://localhost:8085${s.cover}`) : 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80',
          streams: s.playCount || 0
        }))
        catalog.value = formatted
        if (!currentSong.value && formatted.length > 0) {
          currentSong.value = formatted[0]
        }
      }
    } catch (err) {
      console.warn('Could not load songs from backend:', err.message)
    }
  }

  function playSong(song, customQueue = null) {
    if (!song) return
    currentSong.value = { ...song }
    isPlaying.value = true

    if (customQueue && Array.isArray(customQueue) && customQueue.length > 0) {
      queue.value = [...customQueue]
      queueIndex.value = queue.value.findIndex(item => item.id === song.id)
      if (queueIndex.value === -1) queueIndex.value = 0
    } else if (queue.value.length === 0) {
      queue.value = [...catalog.value]
      queueIndex.value = queue.value.findIndex(item => item.id === song.id)
      if (queueIndex.value === -1) queueIndex.value = 0
    } else {
      const idx = queue.value.findIndex(item => item.id === song.id)
      if (idx !== -1) {
        queueIndex.value = idx
      } else {
        queue.value.push(song)
        queueIndex.value = queue.value.length - 1
      }
    }
  }

  function togglePlay() {
    if (!currentSong.value && catalog.value.length > 0) {
      playSong(catalog.value[0])
      return
    }
    isPlaying.value = !isPlaying.value
  }

  function nextSong() {
    if (queue.value.length === 0) return

    if (isShuffle.value) {
      const randomIndex = Math.floor(Math.random() * queue.value.length)
      queueIndex.value = randomIndex
    } else {
      queueIndex.value = (queueIndex.value + 1) % queue.value.length
    }

    const nextTrack = queue.value[queueIndex.value]
    if (nextTrack) {
      currentSong.value = { ...nextTrack }
      isPlaying.value = true
    }
  }

  function prevSong() {
    if (queue.value.length === 0) return

    if (currentTime.value > 3) {
      seekTo(0)
      return
    }

    queueIndex.value = (queueIndex.value - 1 + queue.value.length) % queue.value.length
    const prevTrack = queue.value[queueIndex.value]
    if (prevTrack) {
      currentSong.value = { ...prevTrack }
      isPlaying.value = true
    }
  }

  function seekTo(seconds) {
    currentTime.value = seconds
    seekTarget.value = seconds
  }

  function setVolume(newVolume) {
    volume.value = Math.max(0, Math.min(1, newVolume))
    if (volume.value > 0) {
      isMuted.value = false
    }
  }

  function toggleMute() {
    isMuted.value = !isMuted.value
  }

  function toggleShuffle() {
    isShuffle.value = !isShuffle.value
  }

  function toggleRepeat() {
    isRepeat.value = !isRepeat.value
  }

  /**
   * Adds a newly uploaded song to the catalog and plays it immediately.
   */
  function addSong(newSong, playImmediately = true) {
    const songEntry = {
      id: newSong.id || Date.now(),
      title: newSong.title || 'Untitled Track',
      artist: newSong.artist || 'Independent Artist',
      cover: newSong.cover || 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80',
      audioUrl: newSong.audioUrl,
      duration: newSong.duration || '3:30',
      genre: newSong.genre || 'Khmer',
      streams: '1'
    }

    catalog.value.unshift(songEntry)
    queue.value.unshift(songEntry)
    queueIndex.value = 0

    if (playImmediately) {
      playSong(songEntry)
    }

    return songEntry
  }

  function updateSong(id, updatedFields) {
    const idx = catalog.value.findIndex(s => s.id === id)
    if (idx !== -1) {
      catalog.value[idx] = { ...catalog.value[idx], ...updatedFields }
    }
    const qIdx = queue.value.findIndex(s => s.id === id)
    if (qIdx !== -1) {
      queue.value[qIdx] = { ...queue.value[qIdx], ...updatedFields }
    }
    if (currentSong.value && currentSong.value.id === id) {
      currentSong.value = { ...currentSong.value, ...updatedFields }
    }
  }

  function removeSong(id) {
    catalog.value = catalog.value.filter(s => s.id !== id)
    queue.value = queue.value.filter(s => s.id !== id)
    if (currentSong.value && currentSong.value.id === id) {
      currentSong.value = catalog.value.length > 0 ? catalog.value[0] : null
      isPlaying.value = false
    }
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
    seekTarget,
    catalog,
    // Computed
    hasCurrentSong,
    progressPercentage,
    // Actions
    playSong,
    togglePlay,
    nextSong,
    prevSong,
    seekTo,
    setVolume,
    toggleMute,
    toggleShuffle,
    toggleRepeat,
    addSong,
    updateSong,
    removeSong,
    loadCatalogFromBackend,
    formatTime
  }
})
