import { apiDelete, apiGet, apiPost, apiPut } from './api'

export function getWords() {
  return apiGet('/api/words')
}

export function createWord(payload) {
  return apiPost('/api/words', payload)
}

export function updateWord(id, payload) {
  return apiPut(`/api/words/${id}`, payload)
}

export function deleteWord(id) {
  return apiDelete(`/api/words/${id}`)
}