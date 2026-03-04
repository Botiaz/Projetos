import { apiDelete, apiGet, apiPost, apiPut } from './api'

export function getUsers() {
  return apiGet('/api/users')
}

export function createUser(payload) {
  return apiPost('/api/users', payload)
}

export function updateUser(id, payload) {
  return apiPut(`/api/users/${id}`, payload)
}

export function deleteUser(id) {
  return apiDelete(`/api/users/${id}`)
}