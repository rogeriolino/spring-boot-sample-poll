import * as React from 'react'
import { Feedback } from './Feedback'

type InputType = 'text'|'password'
type ChangeType = (event: React.ChangeEvent<HTMLInputElement>) => void

type Props = {
    type: InputType
    value: string
    id?: string|null
    label?: string|null
    placeholder?: string|null
    disabled?: boolean|null
    readOnly?: boolean|null
    required?: boolean|null
    errorMessage?: string|null
    onChange?: ChangeType
    prependAddon?: React.ReactNode|null
    appendAddon?: React.ReactNode|null
}

export const Input: React.FC<Props> = (props) => {

    const formControllClassName = (): string => {
        const classes = ['form-control']
        if (!!props.errorMessage) {
            classes.push('is-invalid')
        }
        return classes.join(' ')
    }

    const hasAddon = () => {
        return (props.prependAddon || props.appendAddon)
    }

    const renderInput = () => {
        return (
            <input
                id={props.id}
                placeholder={props.placeholder}
                type={props.type}
                value={props.value}
                onChange={props.onChange}
                disabled={props.disabled}
                required={props.required}
                readOnly={props.readOnly}
                className={formControllClassName()} />
        )
    }

    return (
        <>
            {!!props.label && (
                <label>{props.label}</label>
            )}
            {hasAddon() ? (
                <div className="input-group">
                    {!!props.prependAddon && (
                        <div className="input-group-prepend">
                            {props.prependAddon}
                        </div>
                    )}
                    {renderInput()}
                    {!!props.appendAddon && (
                        <div className="input-group-append">
                            {props.appendAddon}
                        </div>
                    )}
                </div>
            ) : (
                renderInput()
            )}
            {!!props.errorMessage && (
                <Feedback type="invalid" message={props.errorMessage} />
            )}
        </>
    )
}