import * as React from 'react'
import { Link } from 'react-router-dom'

type Props = {
}

export const Index: React.FC<Props> = ({}) => {
    return (
        <div className="jumbotron bg-dark">
            <h1 className="display-3">Poll mania</h1>
            <p className="lead">Yet another poll web aplication just for fun. Buit-in with Spring-Boot and React.</p>
            <hr/>
            <div className="text-center pt-3">
                <Link to="/polls/new" className="btn btn-outline-primary mr-2 text-white">Create your first poll</Link>
                <Link to="/polls" className="btn btn-outline-secondary mr-2 text-white">Discover</Link>
            </div>
        </div>
    )
}
