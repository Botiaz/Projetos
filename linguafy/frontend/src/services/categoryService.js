import { apiDelete, apiGet, apiPost, apiPut } from './api'

export function getCategories() {
  return apiGet('/api/categories')
}

export function createCategory(payload) {
  return apiPost('/api/categories', payload)
}

export function updateCategory(id, payload) {
  return apiPut(`/api/categories/${id}`, payload)
}

export function deleteCategory(id) {
  return apiDelete(`/api/categories/${id}`)
}