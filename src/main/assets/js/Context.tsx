import * as React from 'react'
import { User } from './models'
import { provider } from './declarations'

type AuthState = {
    user: User|null
    authenticated: boolean
    loading: boolean
}

const initialValue = {
    user: null,
    authenticated: false,
    loading: true,
}

export const Context = React.createContext<AuthState>(initialValue)

export const AuthContext: React.FC<{}> = ({ children }) => {
    const [ state, setState ] = React.useState<AuthState>(initialValue)

    React.useEffect(() => {
        (async () => {
            state.loading = true
            try {
                const user = await provider.profile()
                if (user.id) {
                    state.user = user
                    state.authenticated = true
                }
            } catch (e) {
            }
            state.loading = false
            setState({ ...state })
        })()
    }, [])

    return (
        <Context.Provider value={state}>
            {!state.loading && children}
        </Context.Provider>
    )
}