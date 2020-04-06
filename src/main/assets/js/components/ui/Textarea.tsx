import * as React from 'react'
import { Feedback } from './Feedback'

type ChangeType = (event: React.ChangeEvent<HTMLTextAreaElement>) => void

type Props = {
    value: string
    label?: string|null
    id?: string|null
    placeholder?: string|null
    disabled?: boolean|null
    required?: boolean|null
    errorMessage?: string|null
    onChange?: ChangeType
}

export const Textarea: React.FC<Props> = (props) => {

    const formControllClassName = (): string => {
        const classes = ['form-control']
        if (!!props.errorMessage) {
            classes.push('is-invalid')
        }
        return classes.join(' ')
    }

    return (
        <>
            {!!props.label && (
                <label>{props.label}</label>
            )}
            <textarea
                id={props.id}
                placeholder={props.placeholder}
                value={props.value}
                onChange={props.onChange}
                disabled={props.disabled}
                required={props.required}
                className={formControllClassName()} />
            {!!props.errorMessage && (
                <Feedback type="invalid" message={props.errorMessage} />
            )}
        </>
    )
}
