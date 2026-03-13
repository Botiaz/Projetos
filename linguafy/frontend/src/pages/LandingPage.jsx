import { Link } from 'react-router-dom'
import Button from '../components/Button'
import Card from '../components/Card'
import { useAuth } from '../context/AuthContext'

function LandingPage() {
  const { isAuthenticated } = useAuth()

  return (
    <div className="landing">
      <section className="hero">
        <h1>Aprenda Idiomas de Forma Inteligente e Eficiente</h1>
        <p>
          Traduza, salve e revise palavras com um sistema inteligente feito para acelerar seu aprendizado
          de forma prática e personalizada.
        </p>
        <div className="hero-actions">
          {isAuthenticated ? (
            <>
              <Link to="/dashboard">
                <Button>Ir para o Dashboard</Button>
              </Link>
              <Link to="/treino">
                <Button variant="ghost">Continuar Treino</Button>
              </Link>
            </>
          ) : (
            <>
              <Link to="/cadastro">
                <Button>Criar Conta</Button>
              </Link>
              <Link to="/login">
                <Button variant="ghost">Ja Tenho Conta</Button>
              </Link>
            </>
          )}
        </div>
      </section>

      <section className="steps">
        <h2>Como funciona</h2>
        <div className="grid-3">
          <Card>
            <h3>1. Escolha seu idioma</h3>
            <p>Selecione o idioma de estudo e personalize sua jornada.</p>
          </Card>
          <Card>
            <h3>2. Traduza palavras automaticamente</h3>
            <p>Digite palavras e obtenha traduções para criar sua base de vocabulário.</p>
          </Card>
          <Card>
            <h3>3. Treine com revisões inteligentes</h3>
            <p>Use flashcards e marque dificuldades para evoluir com consistência.</p>
          </Card>
        </div>
      </section>

      <section className="benefits">
        <h2>Benefícios</h2>
        <div className="grid-2">
          <Card>Tradução com integração à API.</Card>
          <Card>Sistema de treino estilo flashcards.</Card>
          <Card>Acompanhamento de progresso.</Card>
          <Card>Aprendizado personalizado.</Card>
        </div>
      </section>
    </div>
  )
}

export default LandingPage
