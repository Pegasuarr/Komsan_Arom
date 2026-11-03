/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        melody: {
          darkest: '#090a0f',   // Deepest background
          dark: '#11121a',      // Sidebar & Navbar background
          card: '#181924',      // Card & panel background
          cardHover: '#222434', // Hover state for cards
          border: '#2a2d3f',    // Subtle borders
          muted: '#8e92a8',     // Subtitle text
          accent: '#8b5cf6',    // Primary purple accent
          accentHover: '#a78bfa',
          blue: '#3b82f6',      // Secondary blue accent
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      }
    },
  },
  plugins: [],
}

