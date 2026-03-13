import { useEffect, useState } from 'react'
import Button from '../components/Button'
import Card from '../components/Card'
import { getSavedWords } from '../services/dashboardService'
import { submitReview } from '../services/trainingService'

function normalize(text) {
  return (text || '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim()
    .toLowerCase()
}

function TrainingPage() {
  const [words, setWords] = useState([])
  const [index, setIndex] = useState(0)
  const [answer, setAnswer] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')

  async function loadWords() {
    try {
      const data = await getSavedWords()
      setWords(data)
      setIndex(0)
      setAnswer('')
      setResult(null)
    } catch {
      setError('Não foi possível carregar palavras para treino.')
    }
  }

  useEffect(() => {
    loadWords()
  }, [])

  async function handleCheckAnswer() {
    const current = words[index]
    if (!current || !answer.trim()) return

    try {
      const isCorrect = normalize(answer) === normalize(current.translation)
      await submitReview({ wordId: current.id, correct: isCorrect })
      setResult({
        isCorrect,
        expected: current.translation,
      })
    } catch {
      setError('Não foi possível registrar revisão.')
    }
  }

  function handleNextWord() {
    setAnswer('')
    setResult(null)
    setIndex((prev) => prev + 1)
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

      {result ? (
        <>
          <p className="flash-translation">Tradução correta: {result.expected}</p>
          <p>{result.isCorrect ? 'Acertou!' : 'Errou, mas continue treinando.'}</p>
          <Button type="button" onClick={handleNextWord}>
            Proxima palavra
          </Button>
        </>
      ) : (
        <>
          <div className="form">
            <input
              placeholder="Digite a tradução"
              value={answer}
              onChange={(event) => setAnswer(event.target.value)}
            />
          </div>
          <Button type="button" onClick={handleCheckAnswer} disabled={!answer.trim()}>
            Verificar resposta
          </Button>
        </>
      )}

      {error && <p className="error">{error}</p>}
    </Card>
  )
}

export default TrainingPage
