import api from './api'

export const songService = {
  // Songs
  getAllSongs() {
    return api.get('/songs')
  },
  getTrending() {
    return api.get('/songs/trending')
  },
  getQuickPicks() {
    return api.get('/songs/quick-picks')
  },
  searchSongs(query) {
    return api.get('/songs/search', { params: { q: query } })
  },
  getSongById(id) {
    return api.get(`/songs/${id}`)
  },
  recordStream(id) {
    return api.post(`/songs/${id}/play`)
  },
  uploadSong(formData) {
    return api.post('/songs/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  createSong(data) {
    if (data instanceof FormData) {
      return this.uploadSong(data)
    }
    return api.post('/songs', data)
  },
  updateSong(id, data) {
    return api.put(`/songs/${id}`, data)
  },
  deleteSong(id) {
    return api.delete(`/songs/${id}`)
  },

  // Artists
  getAllArtists() {
    return api.get('/artists')
  },
  getArtistById(id) {
    return api.get(`/artists/${id}`)
  },

  // Playlists
  getFeaturedPlaylists() {
    return api.get('/playlists')
  },
  getMyPlaylists() {
    return api.get('/playlists/my')
  },
  getPlaylistById(id) {
    return api.get(`/playlists/${id}`)
  },
  createPlaylist(data) {
    return api.post('/playlists', data)
  },
  addSongToPlaylist(playlistId, songId) {
    return api.post(`/playlists/${playlistId}/songs/${songId}`)
  },
  removeSongFromPlaylist(playlistId, songId) {
    return api.delete(`/playlists/${playlistId}/songs/${songId}`)
  },

  // Favorites
  getFavorites() {
    return api.get('/users/favorites')
  },
  addFavorite(songId) {
    return api.post(`/users/favorites/${songId}`)
  },
  removeFavorite(songId) {
    return api.delete(`/users/favorites/${songId}`)
  }
}

export default songService

