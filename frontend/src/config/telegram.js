/**
 * Telegram Bot Configuration for MelodyHub
 * Users can customize their bot username in .env or change it here.
 */
export const TELEGRAM_BOT_USERNAME = import.meta.env.VITE_TELEGRAM_BOT_USERNAME || 'MelodyHubMusicBot'
export const TELEGRAM_BOT_URL = import.meta.env.VITE_TELEGRAM_BOT_URL || `https://t.me/${TELEGRAM_BOT_USERNAME}`

/**
 * Returns a Telegram deep link to open the bot.
 * If a songId is provided, passes a start parameter so the bot can immediately send/play that track.
 * Example: https://t.me/MelodyHubMusicBot?start=play_101
 */
export function getTelegramSongLink(songId) {
  if (!songId) return TELEGRAM_BOT_URL
  return `${TELEGRAM_BOT_URL}?start=play_${songId}`
}

