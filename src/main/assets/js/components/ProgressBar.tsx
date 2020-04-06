import * as React from 'react'

type Props = {
    value: number
    min: number
    max: number
}

export const ProgressBar: React.FC<Props> = ({ value, min, max }) => {
    const pct = value * 100 / max

    return (
        <div className="progress">
            <div
                className="progress-bar"
                style={{width: pct + '%'}}
                role="progressbar"
                aria-valuenow={value}
                aria-valuemin={min}
                aria-valuemax={max}>
                {value} vote(s)
            </div>
        </div>
    )
}
