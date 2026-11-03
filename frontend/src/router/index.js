import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'

/**
 * Vue Router Configuration
 * Maps URL paths to Vue view components without full-page reloads.
 */
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: 'Home – Komsan Arom' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    // Always scroll to top when changing routes
    return { top: 0 }
  }
})

// Optional route guard to update document title
router.afterEach((to) => {
  document.title = to.meta.title || 'Komsan Arom – MelodyHub'
})

export default router

