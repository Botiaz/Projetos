import { useEffect, useState } from 'react'
import { getCategories } from '../services/categoryService'
import { createWord, deleteWord, getWords, updateWord } from '../services/wordService'

const INITIAL_FORM = {
  word: '',
  translation: '',
  pronunciation: '',
  audioUrl: '',
  categoryId: '',
}

function WordsPage() {
  const [words, setWords] = useState([])
  const [categories, setCategories] = useState([])
  const [form, setForm] = useState(INITIAL_FORM)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function loadData() {
    try {
      setError('')
      const [wordsData, categoriesData] = await Promise.all([getWords(), getCategories()])
      setWords(wordsData)
      setCategories(categoriesData)
    } catch {
      setError('Não foi possível carregar palavras/categorias.')
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

  function startEdit(word) {
    setEditingId(word.id)
    setForm({
      word: word.word ?? '',
      translation: word.translation ?? '',
      pronunciation: word.pronunciation ?? '',
      audioUrl: word.audioUrl ?? '',
      categoryId: String(word.categoryId ?? ''),
    })
  }

  function resetForm() {
    setEditingId(null)
    setForm(INITIAL_FORM)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const payload = {
      word: form.word,
      translation: form.translation,
      pronunciation: form.pronunciation,
      audioUrl: form.audioUrl,
      categoryId: Number(form.categoryId),
    }

    try {
      setError('')
      if (editingId) {
        await updateWord(editingId, payload)
      } else {
        await createWord(payload)
      }
      resetForm()
      await loadData()
    } catch {
      setError('Erro ao salvar palavra.')
    }
  }

  async function handleDelete(id) {
    try {
      setError('')
      await deleteWord(id)
      await loadData()
    } catch {
      setError('Não foi possível excluir a palavra.')
    }
  }

  function categoryName(id) {
    return categories.find((category) => category.id === id)?.name ?? `ID ${id}`
  }

  return (
    <section className="card">
      <h2>Palavras</h2>

      <form className="crud-form" onSubmit={handleSubmit}>
        <input name="word" placeholder="Palavra" value={form.word} onChange={handleChange} required />
        <input name="translation" placeholder="Tradução" value={form.translation} onChange={handleChange} required />
        <input name="pronunciation" placeholder="Pronúncia" value={form.pronunciation} onChange={handleChange} />
        <input name="audioUrl" placeholder="URL de áudio" value={form.audioUrl} onChange={handleChange} />
        <select name="categoryId" value={form.categoryId} onChange={handleChange} required>
          <option value="">Selecione uma categoria</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
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
        <p>Carregando palavras...</p>
      ) : (
        <ul className="list">
          {words.map((word) => (
            <li key={word.id}>
              <div className="item-info">
                <strong>{word.word}</strong>
                <span>Tradução: {word.translation}</span>
                {word.pronunciation && <span>Pronúncia: {word.pronunciation}</span>}
                <span>Categoria: {categoryName(word.categoryId)}</span>
              </div>
              <div className="actions-row">
                <button type="button" className="ghost" onClick={() => startEdit(word)}>
                  Editar
                </button>
                <button type="button" className="danger" onClick={() => handleDelete(word.id)}>
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

export default WordsPage