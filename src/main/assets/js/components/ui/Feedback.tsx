import * as React from 'react'

type FeedbackType = 'valid'|'invalid'

type Props = {
    type: FeedbackType
    message: string|string[]
}

export const Feedback: React.FC<Props> = ({ type, message }) => {

    const printMessages = () => {
        if (typeof(message) === 'string') {
            return message
        }
        return message.join('. ')
    }

    return (
        <div className={`${type}-feedback`}>
            {printMessages()}
        </div>
    )
}
