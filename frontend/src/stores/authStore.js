import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'

/**
 * Auth Store: Manages authenticated user state and session with Spring Boot backend.
 */
export const useAuthStore = defineStore('auth', () => {
  // --- Reactive State ---
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const loading = ref(false)
  const error = ref(null)

  // --- Computed (Getters) ---
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')

  // --- Actions ---
  async function login(email, password) {
    loading.value = true
    error.value = null
    try {
      const response = await api.post('/auth/login', { email, password })
      const data = response.data

      token.value = data.token
      user.value = {
        id: data.id,
        username: data.username,
        email: data.email,
        role: data.role
      }

      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(user.value))
      return { success: true }
    } catch (err) {
      error.value = err.response?.data?.message || 'Login failed. Please check your credentials.'
      return { success: false, error: error.value }
    } finally {
      loading.value = false
    }
  }

  async function register(username, email, password) {
    loading.value = true
    error.value = null
    try {
      const response = await api.post('/auth/register', { username, email, password })
      const data = response.data

      token.value = data.token
      user.value = {
        id: data.id,
        username: data.username,
        email: data.email,
        role: data.role
      }

      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(user.value))
      return { success: true }
    } catch (err) {
      error.value = err.response?.data?.message || 'Registration failed.'
      return { success: false, error: error.value }
    } finally {
      loading.value = false
    }
  }

  async function fetchProfile() {
    if (!token.value) return
    try {
      const response = await api.get('/users/me')
      user.value = response.data
      localStorage.setItem('user', JSON.stringify(response.data))
    } catch (err) {
      if (err.response?.status === 401) {
        logout()
      }
    }
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return {
    user,
    token,
    loading,
    error,
    isAuthenticated,
    isAdmin,
    login,
    register,
    fetchProfile,
    logout
  }
})
