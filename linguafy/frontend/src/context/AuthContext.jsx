import { createContext, useContext, useMemo, useState } from 'react'
import { loginRequest, registerRequest } from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('linguafy_token'))
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('linguafy_user')
    return raw ? JSON.parse(raw) : null
  })

  async function login(payload) {
    const data = await loginRequest(payload)
    localStorage.setItem('linguafy_token', data.token)
    localStorage.setItem('linguafy_user', JSON.stringify({ id: data.userId, name: data.name, email: data.email }))
    setToken(data.token)
    setUser({ id: data.userId, name: data.name, email: data.email })
  }

  async function register(payload) {
    const data = await registerRequest(payload)
    localStorage.setItem('linguafy_token', data.token)
    localStorage.setItem('linguafy_user', JSON.stringify({ id: data.userId, name: data.name, email: data.email }))
    setToken(data.token)
    setUser({ id: data.userId, name: data.name, email: data.email })
  }

  function logout() {
    localStorage.removeItem('linguafy_token')
    localStorage.removeItem('linguafy_user')
    setToken(null)
    setUser(null)
  }

  const value = useMemo(
    () => ({ token, user, isAuthenticated: Boolean(token), login, register, logout }),
    [token, user],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth precisa estar dentro de AuthProvider')
  }
  return context
}
