import { NavLink, Outlet } from 'react-router-dom'

function MainLayout() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Linguafy</h1>
        <nav>
          <NavLink to="/" end>
            Início
          </NavLink>
          <NavLink to="/idiomas">Idiomas</NavLink>
          <NavLink to="/usuarios">Usuários</NavLink>
          <NavLink to="/categorias">Categorias</NavLink>
          <NavLink to="/palavras">Palavras</NavLink>
        </nav>
      </header>

      <main className="app-content">
        <Outlet />
      </main>
    </div>
  )
}

export default MainLayout