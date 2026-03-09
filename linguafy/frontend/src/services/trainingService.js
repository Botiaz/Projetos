import api from './api'

export async function getTrainingProgress() {
  const response = await api.get('/api/training/progress')
  return response.data
}

export async function submitReview(payload) {
  await api.post('/api/training/review', payload)
}
