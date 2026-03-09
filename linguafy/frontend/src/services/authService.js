import api from './api'

export async function loginRequest(payload) {
  const response = await api.post('/api/auth/login', payload)
  return response.data
}

export async function registerRequest(payload) {
  const response = await api.post('/api/auth/register', payload)
  return response.data
}
