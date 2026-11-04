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
      <!-- Google OAuth Button -->
      <button
        @click="handleGoogleSignIn"
        type="button"
        class="w-full py-3 px-4 rounded-full bg-white hover:bg-neutral-100 text-neutral-900 font-semibold text-xs sm:text-sm flex items-center justify-center gap-3 transition shadow cursor-pointer"
      >
        <svg class="w-4 h-4" viewBox="0 0 24 24">
          <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" />
          <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" />
          <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z" />
          <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z" />
        </svg>
        <span>Continue with Google</span>
      </button>

      <!-- Divider -->
      <div class="flex items-center gap-3">
        <div class="flex-1 h-px bg-melody-border/60"></div>
        <span class="text-[11px] uppercase tracking-wider text-melody-muted font-medium">or</span>
        <div class="flex-1 h-px bg-melody-border/60"></div>
      </div>

      <!-- Credentials Form -->
      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-melody-muted mb-1.5">Email address</label>
          <input
            v-model="email"
            type="email"
            required
            placeholder="you@example.com"
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
          class="w-full py-3 rounded-full bg-purple-600 hover:bg-purple-500 text-white font-semibold text-sm shadow-lg shadow-purple-600/30 transition hover:scale-[1.01] cursor-pointer mt-2"
        >
          Sign In
        </button>
      </form>

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

const email = ref('')
const password = ref('')

function handleLogin() {
  // Mock login setting authentication state
  authStore.setAuth(
    {
      username: email.value.split('@')[0] || 'Komsan Listener',
      email: email.value,
      role: 'ROLE_USER'
    },
    'mock-jwt-token'
  )
  router.push('/')
}

function handleGoogleSignIn() {
  // Prepared integration point for Spring Boot OAuth redirect
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>
