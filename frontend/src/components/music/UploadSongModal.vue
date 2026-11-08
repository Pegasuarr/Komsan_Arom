<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md transition-opacity"
    @click.self="close"
  >
    <div
      class="bg-melody-card border border-melody-border/80 rounded-2xl w-full max-w-lg overflow-hidden shadow-2xl shadow-purple-950/40 animate-in fade-in zoom-in-95 duration-200"
    >
      <!-- Modal Header -->
      <div class="px-6 py-5 border-b border-melody-border/50 flex items-center justify-between">
        <div>
          <h2 class="text-lg font-bold text-white tracking-tight">Add New Music Track</h2>
          <p class="text-xs text-melody-muted mt-0.5">Upload a local audio file or stream link to MelodyHub</p>
        </div>
        <button
          @click="close"
          class="p-1.5 rounded-lg text-melody-muted hover:text-white hover:bg-melody-dark/50 transition cursor-pointer"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Form Body -->
      <form @submit.prevent="handleSubmit(true)" class="p-6 space-y-5 max-h-[75vh] overflow-y-auto">
        <!-- 1. Track Title & Artist -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="space-y-1.5">
            <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Track Title *</label>
            <input
              v-model="form.title"
              type="text"
              required
              placeholder="e.g. Phnom Penh Sunset"
              class="w-full px-3.5 py-2.5 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-sm placeholder-melody-muted/50 focus:outline-none focus:border-purple-500 transition"
            />
          </div>

          <div class="space-y-1.5">
            <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Artist Name *</label>
            <input
              v-model="form.artist"
              type="text"
              required
              placeholder="e.g. VannDa"
              class="w-full px-3.5 py-2.5 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-sm placeholder-melody-muted/50 focus:outline-none focus:border-purple-500 transition"
            />
          </div>
        </div>

        <!-- 2. Audio Source Selection Tabs -->
        <div class="space-y-2">
          <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Audio Source *</label>
          <div class="flex items-center gap-2 p-1 bg-melody-darkest rounded-xl border border-melody-border/50 text-xs">
            <button
              type="button"
              @click="sourceType = 'file'"
              class="flex-1 py-1.5 rounded-lg font-medium transition cursor-pointer"
              :class="sourceType === 'file' ? 'bg-purple-600 text-white shadow' : 'text-melody-muted hover:text-white'"
            >
              Upload Audio File (.mp3)
            </button>
            <button
              type="button"
              @click="sourceType = 'url'"
              class="flex-1 py-1.5 rounded-lg font-medium transition cursor-pointer"
              :class="sourceType === 'url' ? 'bg-purple-600 text-white shadow' : 'text-melody-muted hover:text-white'"
            >
              Audio Link / Presets
            </button>
          </div>

          <!-- Option A: File Dropzone / Picker -->
          <div v-if="sourceType === 'file'" class="mt-2">
            <label
              class="border-2 border-dashed rounded-xl p-5 flex flex-col items-center justify-center text-center cursor-pointer transition"
              :class="audioFile ? 'border-purple-500/60 bg-purple-950/20' : 'border-melody-border/60 hover:border-purple-500/50 hover:bg-melody-dark/40'"
            >
              <input
                type="file"
                accept="audio/mp3,audio/wav,audio/ogg,audio/m4a,audio/*"
                class="hidden"
                @change="handleAudioFileSelect"
              />

              <div v-if="!audioFile" class="space-y-2">
                <div class="w-10 h-10 rounded-full bg-purple-600/20 text-purple-400 mx-auto flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                  </svg>
                </div>
                <p class="text-xs font-semibold text-white">Click or drag an MP3 / audio file here</p>
                <p class="text-[11px] text-melody-muted">Supports MP3, WAV, OGG, M4A up to 50MB</p>
              </div>

              <div v-else class="space-y-1.5">
                <div class="flex items-center gap-2 text-emerald-400 text-xs font-semibold">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                  </svg>
                  <span>{{ audioFile.name }}</span>
                </div>
                <p class="text-[11px] text-melody-muted font-mono">
                  {{ (audioFile.size / (1024 * 1024)).toFixed(2) }} MB {{ detectedDuration ? `• ${detectedDuration}` : '' }}
                </p>
                <span class="inline-block text-[10px] text-purple-400 underline hover:text-purple-300">Choose different file</span>
              </div>
            </label>
          </div>

          <!-- Option B: Direct Audio URL -->
          <div v-else class="space-y-2 mt-2">
            <input
              v-model="form.audioUrl"
              type="url"
              placeholder="https://example.com/audio/song.mp3"
              class="w-full px-3.5 py-2.5 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-xs placeholder-melody-muted/50 focus:outline-none focus:border-purple-500 transition font-mono"
            />
            <p class="text-[11px] text-melody-muted">Enter any direct stream URL (.mp3 / .wav) for this track</p>
          </div>
        </div>

        <!-- 3. Cover Artwork Selection -->
        <div class="space-y-2">
          <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Cover Image</label>
          
          <div class="flex items-center gap-3">
            <div class="w-14 h-14 rounded-xl bg-melody-darkest border border-melody-border/60 overflow-hidden shrink-0 flex items-center justify-center shadow-md">
              <img
                v-if="form.cover"
                :src="form.cover"
                alt="Track Cover Preview"
                class="w-full h-full object-cover"
              />
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-melody-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>

            <div class="flex-1 space-y-2">
              <input
                v-model="form.cover"
                type="url"
                placeholder="Image URL or pick an artwork below"
                class="w-full px-3 py-2 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-xs placeholder-melody-muted/50 focus:outline-none focus:border-purple-500 transition"
              />
              <!-- Preset Covers -->
              <div class="flex items-center gap-2">
                <button
                  type="button"
                  v-for="(img, idx) in coverPresets"
                  :key="idx"
                  @click="form.cover = img"
                  class="w-7 h-7 rounded-lg overflow-hidden border transition cursor-pointer hover:scale-105"
                  :class="form.cover === img ? 'border-purple-500 ring-2 ring-purple-500/40' : 'border-melody-border/60 opacity-70 hover:opacity-100'"
                >
                  <img :src="img" class="w-full h-full object-cover" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 4. Duration Display / Manual Adjustment -->
        <div class="grid grid-cols-2 gap-4">
          <div class="space-y-1.5">
            <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Duration</label>
            <input
              v-model="form.duration"
              type="text"
              placeholder="e.g. 3:45"
              class="w-full px-3.5 py-2 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-xs placeholder-melody-muted/50 focus:outline-none focus:border-purple-500 transition font-mono"
            />
          </div>

          <div class="space-y-1.5">
            <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Playlist / Category</label>
            <select
              v-model="form.genre"
              class="w-full px-3.5 py-2 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-xs focus:outline-none focus:border-purple-500 transition cursor-pointer"
            >
              <option value="Khmer">Khmer</option>
              <option value="English">English</option>
              <option value="Chinese">Chinese</option>
              <option value="Korea">Korea</option>
              <option value="Japan">Japan</option>
              <option value="France">France</option>
              <option value="Phonk">Phonk</option>
            </select>
          </div>
        </div>

        <!-- Error feedback if any -->
        <div v-if="errorMessage" class="p-3 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs">
          {{ errorMessage }}
        </div>

        <!-- Success notice if song just added -->
        <div v-if="successMessage" class="p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs flex items-center justify-between">
          <span>{{ successMessage }}</span>
          <button
            type="button"
            @click="playJustAdded"
            class="px-2.5 py-1 rounded-lg bg-emerald-600 hover:bg-emerald-500 text-white text-[11px] font-semibold transition cursor-pointer"
          >
            Play Now
          </button>
        </div>

        <!-- Modal Footer Actions -->
        <div class="pt-4 border-t border-melody-border/50 flex items-center justify-end gap-3">
          <button
            type="button"
            @click="close"
            class="px-4 py-2.5 rounded-xl text-xs font-semibold text-melody-muted hover:text-white hover:bg-melody-dark/50 transition cursor-pointer"
          >
            Cancel
          </button>

          <button
            type="button"
            @click="handleSubmit(false)"
            :disabled="isSubmitting"
            class="px-4 py-2.5 rounded-xl bg-melody-darkest hover:bg-melody-card border border-melody-border/80 text-white text-xs font-semibold transition cursor-pointer disabled:opacity-50"
          >
            Save to Catalog
          </button>

          <button
            type="submit"
            :disabled="isSubmitting"
            class="px-5 py-2.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white text-xs font-semibold shadow-lg shadow-purple-600/30 transition hover:scale-105 active:scale-95 cursor-pointer disabled:opacity-50 flex items-center gap-1.5"
          >
            <svg v-if="isSubmitting" class="animate-spin -ml-1 mr-1 h-3.5 w-3.5 text-white" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span>Add & Play Now</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'
import songService from '@/services/songService'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'song-added'])

const playerStore = usePlayerStore()

const sourceType = ref('file')
const audioFile = ref(null)
const detectedDuration = ref('')
const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const lastAddedSong = ref(null)

const coverPresets = [
  'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=300&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&auto=format&fit=crop&q=80'
]

const form = reactive({
  title: '',
  artist: '',
  audioUrl: '',
  cover: coverPresets[0],
  duration: '3:30',
  genre: 'Khmer'
})

function handleAudioFileSelect(e) {
  const file = e.target.files[0]
  if (!file) return

  audioFile.value = file

  // Auto-fill track title from file name if empty
  if (!form.title) {
    const cleanName = file.name.replace(/\.[^/.]+$/, '').replace(/[-_]/g, ' ')
    form.title = cleanName.charAt(0).toUpperCase() + cleanName.slice(1)
  }

  // Create local object URL for instant browser playback
  const objectUrl = URL.createObjectURL(file)
  form.audioUrl = objectUrl

  // Calculate audio duration from file
  const tempAudio = new Audio(objectUrl)
  tempAudio.addEventListener('loadedmetadata', () => {
    if (tempAudio.duration && !isNaN(tempAudio.duration)) {
      const minutes = Math.floor(tempAudio.duration / 60)
      const seconds = Math.floor(tempAudio.duration % 60)
      const formatted = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`
      form.duration = formatted
      detectedDuration.value = formatted
    }
  })
}

async function handleSubmit(playImmediately = true) {
  errorMessage.value = ''
  successMessage.value = ''

  if (!form.title.trim()) {
    errorMessage.value = 'Please provide a track title.'
    return
  }
  if (!form.artist.trim()) {
    errorMessage.value = 'Please provide an artist name.'
    return
  }
  if (!form.audioUrl) {
    errorMessage.value = 'Please select an audio file or enter an audio URL.'
    return
  }

  isSubmitting.value = true

  try {
    const newSongData = {
      id: Date.now(),
      title: form.title.trim(),
      artist: form.artist.trim(),
      audioUrl: form.audioUrl,
      cover: form.cover || coverPresets[0],
      duration: form.duration || '3:30',
      genre: form.genre || 'Music'
    }

    // Attempt backend upload if audio file exists
    if (audioFile.value) {
      const formData = new FormData()
      formData.append('title', form.title)
      formData.append('artist', form.artist)
      formData.append('audio', audioFile.value)
      if (form.duration) formData.append('duration', form.duration)
      if (form.genre) formData.append('genre', form.genre)

      // Send to backend in background if server is running
      songService.createSong(formData).catch((err) => {
        console.warn('Backend not reachable or offline; song loaded locally:', err.message)
      })
    }

    // Add to Pinia store so it plays immediately!
    const addedSong = playerStore.addSong(newSongData, playImmediately)
    lastAddedSong.value = addedSong

    emit('song-added', addedSong)

    if (playImmediately) {
      close()
    } else {
      successMessage.value = `"${form.title}" added to catalog!`
    }
  } catch (err) {
    errorMessage.value = err.message || 'Failed to add song.'
  } finally {
    isSubmitting.value = false
  }
}

function playJustAdded() {
  if (lastAddedSong.value) {
    playerStore.playSong(lastAddedSong.value)
    close()
  }
}

function close() {
  emit('close')
  // Reset fields after closing
  setTimeout(() => {
    form.title = ''
    form.artist = ''
    form.audioUrl = ''
    audioFile.value = null
    detectedDuration.value = ''
    errorMessage.value = ''
    successMessage.value = ''
  }, 300)
}
</script>

