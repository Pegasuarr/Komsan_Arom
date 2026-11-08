<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md transition-opacity"
    @click.self="close"
  >
    <div
      class="bg-melody-card border border-melody-border/80 rounded-2xl w-full max-w-md overflow-hidden shadow-2xl shadow-purple-950/40 animate-in fade-in zoom-in-95 duration-200"
    >
      <!-- Modal Header -->
      <div class="px-6 py-5 border-b border-melody-border/50 flex items-center justify-between">
        <div>
          <h2 class="text-lg font-bold text-white tracking-tight">Edit Song Details</h2>
          <p class="text-xs text-melody-muted mt-0.5">Rename song title, artist, or playlist category</p>
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
      <form @submit.prevent="handleSave" class="p-6 space-y-4">
        <!-- Track Title -->
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

        <!-- Artist Name -->
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

        <!-- Playlist Category -->
        <div class="space-y-1.5">
          <label class="text-xs font-semibold text-melody-muted uppercase tracking-wider">Playlist Category</label>
          <select
            v-model="form.genre"
            class="w-full px-3.5 py-2.5 rounded-xl bg-melody-darkest border border-melody-border/70 text-white text-sm focus:outline-none focus:border-purple-500 transition cursor-pointer"
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

        <!-- Error Feedback -->
        <div v-if="errorMessage" class="p-3 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs">
          {{ errorMessage }}
        </div>

        <!-- Modal Footer Actions -->
        <div class="pt-4 border-t border-melody-border/50 flex items-center justify-between">
          <!-- Delete button -->
          <button
            type="button"
            @click="handleDelete"
            :disabled="isSubmitting"
            class="px-3 py-2 rounded-xl text-xs font-semibold text-red-400 hover:text-red-300 hover:bg-red-500/10 transition cursor-pointer flex items-center gap-1.5"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
            <span>Delete</span>
          </button>

          <div class="flex items-center gap-2">
            <button
              type="button"
              @click="close"
              class="px-4 py-2 rounded-xl text-xs font-semibold text-melody-muted hover:text-white hover:bg-melody-dark/50 transition cursor-pointer"
            >
              Cancel
            </button>

            <button
              type="submit"
              :disabled="isSubmitting"
              class="px-5 py-2 rounded-xl bg-purple-600 hover:bg-purple-500 text-white text-xs font-semibold shadow-lg shadow-purple-600/30 transition hover:scale-105 active:scale-95 cursor-pointer disabled:opacity-50 flex items-center gap-1.5"
            >
              <svg v-if="isSubmitting" class="animate-spin -ml-1 mr-1 h-3.5 w-3.5 text-white" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>Save Changes</span>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { usePlayerStore } from '@/stores/playerStore'
import songService from '@/services/songService'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  song: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'updated', 'deleted'])
const playerStore = usePlayerStore()

const isSubmitting = ref(false)
const errorMessage = ref('')

const form = reactive({
  id: null,
  title: '',
  artist: '',
  genre: 'Khmer'
})

watch(
  () => props.song,
  (newSong) => {
    if (newSong) {
      form.id = newSong.id
      form.title = newSong.title || ''
      form.artist = newSong.artist || ''
      form.genre = newSong.genre || 'Khmer'
    }
  },
  { immediate: true }
)

async function handleSave() {
  if (!form.title.trim()) {
    errorMessage.value = 'Please provide a track title.'
    return
  }
  if (!form.artist.trim()) {
    errorMessage.value = 'Please provide an artist name.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''

  try {
    const updatedData = {
      title: form.title.trim(),
      artist: form.artist.trim(),
      genre: form.genre
    }

    // Call backend API if song has valid ID
    if (form.id) {
      await songService.updateSong(form.id, updatedData)
    }

    // Update global player state
    playerStore.updateSong(form.id, updatedData)

    emit('updated', { id: form.id, ...updatedData })
    close()
  } catch (err) {
    errorMessage.value = err.response?.data?.message || err.message || 'Failed to update song.'
  } finally {
    isSubmitting.value = false
  }
}

async function handleDelete() {
  if (!confirm(`Are you sure you want to delete "${form.title}"?`)) {
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''

  try {
    if (form.id) {
      await songService.deleteSong(form.id)
    }
    playerStore.removeSong(form.id)
    emit('deleted', form.id)
    close()
  } catch (err) {
    errorMessage.value = err.response?.data?.message || err.message || 'Failed to delete song.'
  } finally {
    isSubmitting.value = false
  }
}

function close() {
  emit('close')
}
</script>

