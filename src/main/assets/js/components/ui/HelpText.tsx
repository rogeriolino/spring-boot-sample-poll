import * as React from 'react'

type Props = {
    message: string
}

export const HelpText: React.FC<Props> = ({ message }) => {

    return (
        <small className="form-text text-muted">
            {message}
        </small>
    )
}
