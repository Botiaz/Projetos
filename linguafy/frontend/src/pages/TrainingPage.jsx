import { useEffect, useState } from 'react'
import Button from '../components/Button'
import Card from '../components/Card'
import { getSavedWords } from '../services/dashboardService'
import { submitReview } from '../services/trainingService'

function TrainingPage() {
  const [words, setWords] = useState([])
  const [index, setIndex] = useState(0)
  const [showTranslation, setShowTranslation] = useState(false)
  const [error, setError] = useState('')

  async function loadWords() {
    try {
      const data = await getSavedWords()
      setWords(data)
      setIndex(0)
      setShowTranslation(false)
    } catch {
      setError('Não foi possível carregar palavras para treino.')
    }
  }

  useEffect(() => {
    loadWords()
  }, [])

  async function handleDifficulty(difficulty) {
    const current = words[index]
    if (!current) return

    try {
      await submitReview({ wordId: current.id, difficulty })
      setShowTranslation(false)
      setIndex((prev) => prev + 1)
    } catch {
      setError('Não foi possível registrar revisão.')
    }
  }

  const currentWord = words[index]

  if (!currentWord) {
    return (
      <Card>
        <h2>Treino</h2>
        <p>{words.length === 0 ? 'Adicione palavras no dashboard para começar.' : 'Treino concluído por enquanto.'}</p>
      </Card>
    )
  }

  return (
    <Card className="training-card">
      <h2>Treino com Flashcards</h2>
      <p className="flash-word">{currentWord.word}</p>

      {showTranslation ? (
        <>
          <p className="flash-translation">{currentWord.translation}</p>
          <div className="actions">
            <Button type="button" onClick={() => handleDifficulty('Fácil')}>
              Fácil
            </Button>
            <Button type="button" variant="ghost" onClick={() => handleDifficulty('Médio')}>
              Médio
            </Button>
            <Button type="button" variant="ghost" onClick={() => handleDifficulty('Difícil')}>
              Difícil
            </Button>
          </div>
        </>
      ) : (
        <Button type="button" onClick={() => setShowTranslation(true)}>
          Mostrar tradução
        </Button>
      )}

      {error && <p className="error">{error}</p>}
    </Card>
  )
}

export default TrainingPage
