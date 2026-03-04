import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import Button from '../components/Button'
import Card from '../components/Card'
import { useAuth } from '../context/AuthContext'

function RegisterPage() {
  const [form, setForm] = useState({ name: '', email: '', password: '', level: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
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
      await register(form)
      navigate('/dashboard')
    } catch {
      setError('Não foi possível concluir o cadastro.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card className="auth-card">
      <h2>Criar Conta</h2>
      <form className="form" onSubmit={handleSubmit}>
        <input name="name" placeholder="Nome" value={form.name} onChange={handleChange} required />
        <input type="email" name="email" placeholder="E-mail" value={form.email} onChange={handleChange} required />
        <input
          type="password"
          name="password"
          placeholder="Senha"
          value={form.password}
          onChange={handleChange}
          required
        />
        <input name="level" placeholder="Nível atual" value={form.level} onChange={handleChange} required />
        <Button type="submit" disabled={loading}>
          {loading ? 'Cadastrando...' : 'Cadastrar'}
        </Button>
      </form>
      {error && <p className="error">{error}</p>}
      <p>
        Já possui conta? <Link to="/login">Entrar</Link>
      </p>
    </Card>
  )
}

export default RegisterPage
