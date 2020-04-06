import * as React from 'react'

type Props = {
    label: string
}

export const Spinner: React.FC<Props> = ({ label }) => {
    return (
        <div className="spinner-border" role="status">
            <span className="sr-only">{label}</span>
        </div>
    )
}