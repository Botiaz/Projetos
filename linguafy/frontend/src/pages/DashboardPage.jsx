import { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Button from '../components/Button'
import Card from '../components/Card'
import ProgressChart from '../components/ProgressChart'
import { getLanguages, getSavedWords, saveWord, translateWord } from '../services/dashboardService'
import { getTrainingProgress } from '../services/trainingService'

function DashboardPage() {
  const [languages, setLanguages] = useState([])
  const [selectedLanguage, setSelectedLanguage] = useState('')
  const [wordInput, setWordInput] = useState('')
  const [translation, setTranslation] = useState('')
  const [savedWords, setSavedWords] = useState([])
  const [progress, setProgress] = useState({ totalLearned: 0, totalReviewed: 0, progressPercent: 0, evolution: [0, 0, 0] })
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const loadDashboard = useCallback(async () => {
    try {
      const [languagesData, wordsData, progressData] = await Promise.all([
        getLanguages(),
        getSavedWords(),
        getTrainingProgress(),
      ])
      setLanguages(languagesData)
      setSavedWords(wordsData)
      setProgress(progressData)
      if (languagesData.length > 0) {
        setSelectedLanguage((prev) => prev || String(languagesData[0].id))
      }
    } catch {
      setError('Falha ao carregar dados do dashboard.')
    }
  }, [])

  useEffect(() => {
    loadDashboard()
  }, [loadDashboard])

  const canTranslate = useMemo(() => selectedLanguage && wordInput.trim(), [selectedLanguage, wordInput])

  async function handleTranslate() {
    if (!canTranslate) return
    setError('')
    try {
      const data = await translateWord({ languageId: Number(selectedLanguage), word: wordInput })
      setTranslation(data.translated)
    } catch {
      setError('Não foi possível traduzir a palavra.')
    }
  }

  async function handleSaveWord() {
    if (!translation || !selectedLanguage) return
    setError('')
    try {
      await saveWord({
        languageId: Number(selectedLanguage),
        word: wordInput,
        translation,
        pronunciation: '',
        audioUrl: '',
      })
      setWordInput('')
      setTranslation('')
      await loadDashboard()
    } catch {
      setError('Não foi possível salvar a palavra.')
    }
  }

  return (
    <div className="dashboard-grid">
      <Card>
        <h2>Dashboard</h2>
        <div className="form">
          <select value={selectedLanguage} onChange={(event) => setSelectedLanguage(event.target.value)}>
            <option value="">Selecione um idioma</option>
            {languages.map((language) => (
              <option key={language.id} value={language.id}>
                {language.name}
              </option>
            ))}
          </select>
          <input
            placeholder="Digite uma palavra"
            value={wordInput}
            onChange={(event) => setWordInput(event.target.value)}
          />
          <div className="actions">
            <Button type="button" onClick={handleTranslate} disabled={!canTranslate}>
              Traduzir
            </Button>
            <Button type="button" variant="ghost" onClick={handleSaveWord} disabled={!translation}>
              Salvar Palavra
            </Button>
            <Button type="button" variant="ghost" onClick={() => navigate('/treino')}>
              Treinar
            </Button>
          </div>
          {translation && (
            <p>
              <strong>Tradução:</strong> {translation}
            </p>
          )}
        </div>
        {error && <p className="error">{error}</p>}
      </Card>

      <Card>
        <h3>Suas palavras salvas</h3>
        <ul className="saved-list">
          {savedWords.map((word) => (
            <li key={word.id}>
              <strong>{word.word}</strong>
              <span>{word.translation}</span>
            </li>
          ))}
          {savedWords.length === 0 && <li>Nenhuma palavra salva ainda.</li>}
        </ul>
      </Card>

      <Card className="progress-card">
        <h3>Seu progresso</h3>
        <p>Total de palavras aprendidas: {progress.totalLearned}</p>
        <p>Total de palavras revisadas: {progress.totalReviewed}</p>
        <div className="progress-track">
          <div className="progress-fill" style={{ width: `${progress.progressPercent}%` }} />
        </div>
        <p>{progress.progressPercent}% concluído</p>
        <ProgressChart values={progress.evolution || [0, 0, 0]} />
      </Card>
    </div>
  )
}

export default DashboardPage
