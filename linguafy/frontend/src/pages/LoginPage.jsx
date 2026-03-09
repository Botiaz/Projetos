import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import Button from '../components/Button'
import Card from '../components/Card'
import { useAuth } from '../context/AuthContext'

function LoginPage() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      await login(form)
      navigate('/dashboard')
    } catch {
      setError('Não foi possível realizar login. Verifique suas credenciais.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card className="auth-card">
      <h2>Entrar</h2>
      <form className="form" onSubmit={handleSubmit}>
        <input type="email" name="email" placeholder="E-mail" value={form.email} onChange={handleChange} required />
        <input
          type="password"
          name="password"
          placeholder="Senha"
          value={form.password}
          onChange={handleChange}
          required
        />
        <Button type="submit" disabled={loading}>
          {loading ? 'Entrando...' : 'Login'}
        </Button>
      </form>
      {error && <p className="error">{error}</p>}
      <p>
        Não tem conta? <Link to="/cadastro">Criar conta</Link>
      </p>
    </Card>
  )
}

export default LoginPage
