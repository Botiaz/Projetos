import api from './api'

function mapRequestError(error, fallbackMessage) {
  const message = error?.response?.data?.message
  if (typeof message === 'string' && message.trim()) {
    return new Error(message)
  }
  return new Error(fallbackMessage)
}

export async function loginRequest(payload) {
  try {
    const response = await api.post('/api/auth/login', payload)
    return response.data
  } catch (error) {
    throw mapRequestError(error, 'Nao foi possivel realizar login. Verifique suas credenciais.')
  }
}

export async function registerRequest(payload) {
  try {
    const response = await api.post('/api/auth/register', payload)
    return response.data
  } catch (error) {
    throw mapRequestError(error, 'Nao foi possivel concluir o cadastro.')
  }
}
