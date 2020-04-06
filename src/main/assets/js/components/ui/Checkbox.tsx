import * as React from 'react'
import { Feedback } from './Feedback'

type ChangeType = (event: React.ChangeEvent<HTMLInputElement>) => void

type Props = {
    id?: string|null
    label?: string|null
    disabled?: boolean|null
    required?: boolean|null
    checked?: boolean|null
    errorMessage?: string|null
    onChange?: ChangeType
}

export const Checkbox: React.FC<Props> = (props) => {

    const formControllClassName = (): string => {
        const classes = ['form-check-input']
        if (!!props.errorMessage) {
            classes.push('is-invalid')
        }
        return classes.join(' ')
    }

    return (
        <>
            <input
                id={props.id}
                type="Checkbox"
                onChange={props.onChange}
                disabled={props.disabled}
                required={props.required}
                className={formControllClassName()} />
            {!!props.label && (
                <label htmlFor={props.id} className="form-check-label">
                    {props.label}
                </label>
            )}
            {!!props.errorMessage && (
                <Feedback type="invalid" message={props.errorMessage} />
            )}
        </>
    )
}