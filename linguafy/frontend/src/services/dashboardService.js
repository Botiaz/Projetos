import api from './api'

export async function getLanguages() {
  const response = await api.get('/api/languages')
  return response.data
}

export async function translateWord(payload) {
  const response = await api.post('/api/dashboard/translate', payload)
  return response.data
}

export async function saveWord(payload) {
  const response = await api.post('/api/dashboard/words', payload)
  return response.data
}

export async function getSavedWords() {
  const response = await api.get('/api/dashboard/words')
  return response.data
}
