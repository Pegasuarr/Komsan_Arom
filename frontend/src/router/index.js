import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SearchView from '@/views/SearchView.vue'
import LibraryView from '@/views/LibraryView.vue'
import PlaylistView from '@/views/PlaylistView.vue'
import ProfileView from '@/views/ProfileView.vue'
import AdminView from '@/views/AdminView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'

/**
 * Vue Router Configuration:
 * Maps URL paths to Vue view components without full-page reloads.
 */
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: 'Home – Komsan Arom' }
  },
  {
    path: '/search',
    name: 'search',
    component: SearchView,
    meta: { title: 'Search – Komsan Arom' }
  },
  {
    path: '/library',
    name: 'library',
    component: LibraryView,
    meta: { title: 'Your Library – Komsan Arom' }
  },
  {
    path: '/playlist/:id',
    name: 'playlist',
    component: PlaylistView,
    meta: { title: 'Playlist – Komsan Arom' }
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { title: 'Profile – Komsan Arom' }
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminView,
    meta: { title: 'Admin Dashboard – Komsan Arom' }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { title: 'Login – Komsan Arom' }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: { title: 'Register – Komsan Arom' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// Dynamic title update on route change
router.afterEach((to) => {
  document.title = to.meta.title || 'Komsan Arom – MelodyHub'
})

export default router
