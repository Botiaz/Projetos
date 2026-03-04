import { useEffect, useState } from 'react'
import { createUser, deleteUser, getUsers, updateUser } from '../services/userService'

const INITIAL_FORM = {
  name: '',
  email: '',
  password: '',
  level: '',
}

function UsersPage() {
  const [users, setUsers] = useState([])
  const [form, setForm] = useState(INITIAL_FORM)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function loadUsers() {
    try {
      setError('')
      const data = await getUsers()
      setUsers(data)
    } catch {
      setError('Não foi possível carregar os usuários.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadUsers()
  }, [])

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  function startEdit(user) {
    setEditingId(user.id)
    setForm({
      name: user.name ?? '',
      email: user.email ?? '',
      password: '',
      level: user.level ?? '',
    })
  }

  function resetForm() {
    setEditingId(null)
    setForm(INITIAL_FORM)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    try {
      setError('')
      if (editingId) {
        await updateUser(editingId, form)
      } else {
        await createUser(form)
      }
      resetForm()
      await loadUsers()
    } catch {
      setError('Erro ao salvar usuário. Verifique os dados informados.')
    }
  }

  async function handleDelete(id) {
    try {
      setError('')
      await deleteUser(id)
      await loadUsers()
    } catch {
      setError('Não foi possível excluir o usuário.')
    }
  }

  return (
    <section className="card">
      <h2>Usuários</h2>

      <form className="crud-form" onSubmit={handleSubmit}>
        <input name="name" placeholder="Nome" value={form.name} onChange={handleChange} required />
        <input name="email" type="email" placeholder="E-mail" value={form.email} onChange={handleChange} required />
        <input
          name="password"
          type="password"
          placeholder={editingId ? 'Nova senha (obrigatória)' : 'Senha'}
          value={form.password}
          onChange={handleChange}
          required
        />
        <input name="level" placeholder="Nível" value={form.level} onChange={handleChange} required />
        <div className="actions-row">
          <button type="submit">{editingId ? 'Salvar edição' : 'Cadastrar'}</button>
          {editingId && (
            <button type="button" className="ghost" onClick={resetForm}>
              Cancelar
            </button>
          )}
        </div>
      </form>

      {error && <p className="error">{error}</p>}
      {loading ? (
        <p>Carregando usuários...</p>
      ) : (
        <ul className="list">
          {users.map((user) => (
            <li key={user.id}>
              <div className="item-info">
                <strong>{user.name}</strong>
                <span>{user.email}</span>
                <span>Nível: {user.level}</span>
              </div>
              <div className="actions-row">
                <button type="button" className="ghost" onClick={() => startEdit(user)}>
                  Editar
                </button>
                <button type="button" className="danger" onClick={() => handleDelete(user.id)}>
                  Excluir
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}

export default UsersPage