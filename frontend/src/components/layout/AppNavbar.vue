<template>
  <header class="h-16 px-4 sm:px-8 border-b border-melody-border/40 bg-melody-dark/80 backdrop-blur-md flex items-center justify-between sticky top-0 z-30">
    <!-- Left: Mobile Menu Toggle & Navigation History -->
    <div class="flex items-center gap-3">
      <!-- Hamburger Button (Mobile only) -->
      <button
        @click="$emit('toggleSidebar')"
        class="lg:hidden p-2 rounded-xl text-melody-muted hover:text-white hover:bg-melody-card transition cursor-pointer"
        aria-label="Toggle navigation"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
      </button>

      <!-- History Navigation (< and >) -->
      <div class="hidden sm:flex items-center gap-1.5">
        <button
          @click="goBack"
          class="w-8 h-8 rounded-full bg-melody-card hover:bg-melody-cardHover border border-melody-border/50 text-melody-muted hover:text-white flex items-center justify-center transition cursor-pointer"
          title="Go back"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
        </button>
        <button
          @click="goForward"
          class="w-8 h-8 rounded-full bg-melody-card hover:bg-melody-cardHover border border-melody-border/50 text-melody-muted hover:text-white flex items-center justify-center transition cursor-pointer"
          title="Go forward"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>

      <!-- Quick Search Input Bar -->
      <div class="relative w-44 sm:w-64 md:w-80">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-melody-muted">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <input
          v-model="navSearch"
          @keyup.enter="handleSearch"
          type="text"
          placeholder="What do you want to play?"
          class="w-full pl-9 pr-4 py-1.5 rounded-full bg-melody-card border border-melody-border/60 text-xs sm:text-sm text-white placeholder-melody-muted focus:outline-none focus:border-purple-500/60 focus:ring-1 focus:ring-purple-500/40 transition"
        />
      </div>
    </div>

    <!-- Right: Authentication & Profile -->
    <div class="flex items-center gap-3">
      <!-- If NOT authenticated -->
      <template v-if="!authStore.isAuthenticated">
        <router-link
          to="/register"
          class="hidden sm:inline-block px-3.5 py-1.5 text-xs font-semibold text-melody-muted hover:text-white transition"
        >
          Sign up
        </router-link>
        <router-link
          to="/login"
          class="px-4 py-1.5 rounded-full bg-white hover:bg-neutral-200 text-black text-xs font-bold transition shadow-md"
        >
          Log in
        </router-link>
      </template>

      <!-- If authenticated -->
      <template v-else>
        <div class="relative flex items-center gap-3">
          <router-link
            to="/profile"
            class="flex items-center gap-2 p-1 pl-2 pr-3 rounded-full bg-melody-card hover:bg-melody-cardHover border border-melody-border/60 transition group"
          >
            <div class="w-7 h-7 rounded-full bg-gradient-to-tr from-purple-500 to-indigo-500 flex items-center justify-center text-xs font-bold text-white shadow">
              {{ userInitial }}
            </div>
            <span class="text-xs font-medium text-white max-w-[100px] truncate hidden sm:inline">
              {{ authStore.user?.username || 'User' }}
            </span>
          </router-link>

          <button
            @click="authStore.logout"
            class="p-2 rounded-full text-melody-muted hover:text-red-400 hover:bg-melody-card transition cursor-pointer"
            title="Log out"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
          </button>
        </div>
      </template>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

defineEmits(['toggleSidebar'])

const router = useRouter()
const authStore = useAuthStore()

const navSearch = ref('')

const userInitial = computed(() => {
  if (authStore.user?.username) {
    return authStore.user.username.charAt(0).toUpperCase()
  }
  return 'U'
})

function goBack() {
  router.back()
}

function goForward() {
  router.forward()
}

function handleSearch() {
  if (navSearch.value.trim()) {
    router.push({
      path: '/search',
      query: { q: navSearch.value.trim() }
    })
  }
}
</script>

