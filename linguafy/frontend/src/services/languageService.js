import { apiGet } from './api'

export function getLanguages() {
  return apiGet('/api/languages')
}