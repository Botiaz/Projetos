import { useEffect, useState } from 'react'
import { createCategory, deleteCategory, getCategories, updateCategory } from '../services/categoryService'
import { getLanguages } from '../services/languageService'

const INITIAL_FORM = {
  name: '',
  languageId: '',
}

function CategoriesPage() {
  const [categories, setCategories] = useState([])
  const [languages, setLanguages] = useState([])
  const [form, setForm] = useState(INITIAL_FORM)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function loadData() {
    try {
      setError('')
      const [categoriesData, languagesData] = await Promise.all([getCategories(), getLanguages()])
      setCategories(categoriesData)
      setLanguages(languagesData)
    } catch {
      setError('Não foi possível carregar categorias/idiomas.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  function startEdit(category) {
    setEditingId(category.id)
    setForm({
      name: category.name ?? '',
      languageId: String(category.languageId ?? ''),
    })
  }

  function resetForm() {
    setEditingId(null)
    setForm(INITIAL_FORM)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const payload = {
      name: form.name,
      languageId: Number(form.languageId),
    }

    try {
      setError('')
      if (editingId) {
        await updateCategory(editingId, payload)
      } else {
        await createCategory(payload)
      }
      resetForm()
      await loadData()
    } catch {
      setError('Erro ao salvar categoria.')
    }
  }

  async function handleDelete(id) {
    try {
      setError('')
      await deleteCategory(id)
      await loadData()
    } catch {
      setError('Não foi possível excluir a categoria.')
    }
  }

  function languageName(id) {
    return languages.find((language) => language.id === id)?.name ?? `ID ${id}`
  }

  return (
    <section className="card">
      <h2>Categorias</h2>

      <form className="crud-form" onSubmit={handleSubmit}>
        <input name="name" placeholder="Nome da categoria" value={form.name} onChange={handleChange} required />
        <select name="languageId" value={form.languageId} onChange={handleChange} required>
          <option value="">Selecione um idioma</option>
          {languages.map((language) => (
            <option key={language.id} value={language.id}>
              {language.name}
            </option>
          ))}
        </select>
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
        <p>Carregando categorias...</p>
      ) : (
        <ul className="list">
          {categories.map((category) => (
            <li key={category.id}>
              <div className="item-info">
                <strong>{category.name}</strong>
                <span>Idioma: {languageName(category.languageId)}</span>
              </div>
              <div className="actions-row">
                <button type="button" className="ghost" onClick={() => startEdit(category)}>
                  Editar
                </button>
                <button type="button" className="danger" onClick={() => handleDelete(category.id)}>
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

export default CategoriesPage