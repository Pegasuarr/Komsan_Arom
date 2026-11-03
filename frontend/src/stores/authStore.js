import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * Auth Store: Manages authenticated user state and session.
 */
export const useAuthStore = defineStore('auth', () => {
  // --- Reactive State ---
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)

  // --- Computed (Getters) ---
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')

  // --- Actions ---
  function setAuth(userData, authToken) {
    user.value = userData
    token.value = authToken
    localStorage.setItem('token', authToken)
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  return {
    user,
    token,
    isAuthenticated,
    isAdmin,
    setAuth,
    logout
  }
})

