import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './style.css'

// 1. Initialize Vue Application
const app = createApp(App)

// 2. Register Global State Store (Pinia)
const pinia = createPinia()
app.use(pinia)

// 3. Register Client-side Router
app.use(router)

// 4. Mount Application into <div id="app"></div> in index.html
app.mount('#app')

