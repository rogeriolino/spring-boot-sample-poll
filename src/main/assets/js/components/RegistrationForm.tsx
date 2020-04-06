import * as React from 'react'
import { Input } from './ui/Input'
import { User } from '../models'
import { provider } from '../declarations'
import { Errors, emptyErrors } from '../util/errors'

type Props = {}

const initialValue: User = {
    username: '',
    password: '',
}

export const RegistrationForm: React.FC<Props> = ({}) => {
    const [ loading, setLoading ] = React.useState<boolean>(false)
    const [ user, setUser ] = React.useState<User>({ ...initialValue })
    const [ errors, setErrors ] = React.useState<Errors>(emptyErrors())

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault()
        setLoading(true)
        setErrors(emptyErrors())
        try {
            await provider.register(user)
            window.location.href = '/login'
        } catch (ex) {
            let errors: Errors = ex;
            setErrors(errors)
        }
        setLoading(false)
    }

    return (
        <form className="card text-white bg-dark registration-form" onSubmit={handleSubmit}>
            <div className="card-body">
                <h1 className="card-title">New account</h1>
                <hr />
                <div className="form-group">
                    <Input
                        placeholder="Username"
                        type="text"
                        value={user.username}
                        onChange={(e) => setUser({ ...user, username: e.target.value })}
                        errorMessage={errors.fields['username']} />
                </div>
                <div className="form-group">
                    <Input
                        placeholder="Password"
                        type="password"
                        value={user.password}
                        onChange={(e) => setUser({ ...user, password: e.target.value })}
                        errorMessage={errors.fields['password']} />
                </div>
                <hr />
                <div className="d-flex">
                    <button type="submit" className="btn btn-primary" disabled={loading}>
                        Register
                    </button>
                </div>
            </div>
        </form>
    )
}