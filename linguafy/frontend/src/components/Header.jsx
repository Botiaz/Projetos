import { Link, NavLink, useNavigate } from 'react-router-dom'
import Button from './Button'
import { useAuth } from '../context/AuthContext'

function Header() {
  const { isAuthenticated, user, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/')
  }

  return (
    <header className="site-header">
      <div className="header-inner">
        <Link to="/" className="brand">
          Linguafy
        </Link>

        <nav className="header-nav">
          {isAuthenticated ? (
            <>
              <NavLink to="/dashboard">Dashboard</NavLink>
              <NavLink to="/treino">Treino</NavLink>
            </>
          ) : null}
        </nav>

        <div className="header-actions">
          {isAuthenticated ? (
            <>
              <span className="user-badge">Olá, {user?.name}</span>
              <Button variant="ghost" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Link to="/login">
                <Button variant="ghost">Login</Button>
              </Link>
              <Link to="/cadastro">
                <Button>Cadastro</Button>
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}

export default Header
