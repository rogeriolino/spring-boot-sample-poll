import * as React from 'react'
import { Link } from 'react-router-dom'
import { Context } from '../Context'

export const Nav: React.FC<{}> = () => {
    const authState = React.useContext(Context)

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container">
                <Link to="/" className="navbar-brand">
                    Poll Mania
                </Link>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <Link to="/polls" className="nav-link">Polls</Link>
                        </li>
                        <li className="nav-item">
                            <Link to="/polls/new" className="nav-link">New poll</Link>
                        </li>
                        {authState.authenticated && (
                            <li className="nav-item">
                                <Link to="/my-polls" className="nav-link">My polls</Link>
                            </li>
                        )}
                    </ul>
                    <ul className="navbar-nav">
                        {authState.authenticated ? (
                            <li className="nav-item">
                                <a href="/logout" className="nav-link">Log out</a>
                            </li>
                        ) : (
                            <li className="nav-item ml-auto">
                                <a href="/login" className="nav-link">Log in</a>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    )
}
