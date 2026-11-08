import axios from 'axios'

/**
 * Centralized Axios instance for communicating with the backend.
 *
 * Backend target: Spring Boot REST API
 * Default URL points to standard Spring Boot port 8080 (or environment variable).
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8085/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// Request Interceptor: automatically attaches JWT Token (if user is authenticated)
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response Interceptor: centralized error handling (e.g., 401 Unauthorized redirect)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Future: auto-logout or redirect to login when JWT token expires
      console.warn('Unauthorized request. Token may be expired.')
    }
    return Promise.reject(error)
  }
)

export default api

