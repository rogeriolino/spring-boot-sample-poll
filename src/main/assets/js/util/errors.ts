export type FieldError = {
    [field: string]: string[]
}

export type Errors = {
    message?: string|null
    fields: FieldError[]
}

export function emptyErrors(): Errors {
    return {
        message: '',
        fields: []
    }
}

export function parseError(error): Errors {
    if (error.response) {
        return parseResponse(error.response)
    }
    const errors: Errors = emptyErrors()
    errors.message = error.message
    return errors
}

export function parseResponse(response): Errors {
    const errors: Errors = emptyErrors()
    const fields = response?.data?.errors
    if (fields) {
        errors.fields = fields
    } else {
        const message = response.data?.message
        if (message) {
            errors.message = message.data || message
        }
    }
    return errors
}