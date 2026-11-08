<template>
  <div class="max-w-md mx-auto py-10 px-4 space-y-6">
    <!-- Brand header -->
    <div class="text-center space-y-2">
      <div class="w-12 h-12 mx-auto rounded-2xl bg-gradient-to-tr from-purple-600 to-indigo-600 flex items-center justify-center shadow-lg shadow-purple-500/20">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
        </svg>
      </div>
      <h1 class="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">Log in to MelodyHub</h1>
      <p class="text-xs text-melody-muted">Stream your favorite music anytime, anywhere</p>
    </div>

    <!-- Auth Card -->
    <div class="p-6 sm:p-8 rounded-3xl bg-melody-card border border-melody-border/60 shadow-xl space-y-5">
      <!-- Error Message Banner -->
      <div v-if="errorMessage" class="p-3 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs flex items-center gap-2">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>{{ errorMessage }}</span>
      </div>

      <!-- Credentials Form -->
      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-melody-muted mb-1.5">Email address</label>
          <input
            v-model="email"
            type="email"
            required
            placeholder="listener@melodyhub.io"
            class="w-full px-4 py-2.5 rounded-xl bg-melody-dark/80 border border-melody-border/80 text-white placeholder-melody-muted text-sm focus:outline-none focus:border-purple-500 focus:ring-1 focus:ring-purple-500/50 transition"
          />
        </div>

        <div>
          <div class="flex items-center justify-between mb-1.5">
            <label class="text-xs font-medium text-melody-muted">Password</label>
            <a href="#" class="text-[11px] text-purple-400 hover:underline">Forgot?</a>
          </div>
          <input
            v-model="password"
            type="password"
            required
            placeholder="••••••••"
            class="w-full px-4 py-2.5 rounded-xl bg-melody-dark/80 border border-melody-border/80 text-white placeholder-melody-muted text-sm focus:outline-none focus:border-purple-500 focus:ring-1 focus:ring-purple-500/50 transition"
          />
        </div>

        <button
          type="submit"
          :disabled="authStore.loading"
          class="w-full py-3 rounded-full bg-purple-600 hover:bg-purple-500 disabled:opacity-50 text-white font-semibold text-sm shadow-lg shadow-purple-600/30 transition hover:scale-[1.01] cursor-pointer mt-2 flex items-center justify-center gap-2"
        >
          <svg v-if="authStore.loading" class="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
          </svg>
          <span>{{ authStore.loading ? 'Signing In...' : 'Sign In' }}</span>
        </button>
      </form>

      <!-- Default Credentials Helper -->
      <div class="p-3 rounded-xl bg-purple-500/10 border border-purple-500/20 text-[11px] text-purple-300 space-y-1">
        <p class="font-semibold text-white">Default Demo Credentials:</p>
        <p>• Admin: <code class="text-purple-200">admin@melodyhub.io</code> / <code class="text-purple-200">admin123</code></p>
        <p>• User: <code class="text-purple-200">listener@melodyhub.io</code> / <code class="text-purple-200">password123</code></p>
      </div>

      <!-- Footer link -->
      <div class="text-center pt-2">
        <p class="text-xs text-melody-muted">
          Don't have an account?
          <router-link to="/register" class="text-purple-400 hover:underline font-semibold ml-1">
            Sign up for free
          </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('listener@melodyhub.io')
const password = ref('password123')
const errorMessage = ref('')

async function handleLogin() {
  errorMessage.value = ''
  const result = await authStore.login(email.value, password.value)
  if (result.success) {
    router.push('/')
  } else {
    errorMessage.value = result.error
  }
}
</script>
