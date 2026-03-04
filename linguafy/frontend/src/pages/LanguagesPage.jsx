import { useEffect, useState } from 'react'
import { getLanguages } from '../services/languageService'

function LanguagesPage() {
  const [languages, setLanguages] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadLanguages() {
      try {
        const data = await getLanguages()
        setLanguages(data)
      } catch {
        setError('Não foi possível carregar os idiomas. Verifique se o backend está ativo.')
      } finally {
        setLoading(false)
      }
    }

    loadLanguages()
  }, [])

  if (loading) {
    return <section className="card">Carregando idiomas...</section>
  }

  if (error) {
    return <section className="card error">{error}</section>
  }

  return (
    <section className="card">
      <h2>Idiomas</h2>

      {languages.length === 0 ? (
        <p>Nenhum idioma encontrado.</p>
      ) : (
        <ul className="list">
          {languages.map((language) => (
            <li key={language.id}>
              <strong>{language.name}</strong>
              {language.level && <span>Nível: {language.level}</span>}
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}

export default LanguagesPage