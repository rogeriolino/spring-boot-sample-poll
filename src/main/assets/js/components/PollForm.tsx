import * as React from 'react'
import { useToasts } from 'react-toast-notifications'
import { useParams, Link } from 'react-router-dom'
import { Context } from '../Context'
import { Input } from './ui/Input'
import { Textarea } from './ui/Textarea'
import { Checkbox } from './ui/Checkbox'
import { HelpText } from './ui/HelpText'
import { Poll, Option } from '../models'
import { canEditPoll } from '../util/security'
import { provider } from '../declarations'
import { Errors, emptyErrors } from '../util/errors'
import { Spinner } from './ui/Spinner'

type Props = {}

const initialValue: Poll = {
    title: '',
    description: '',
    restricted: false,
    options: [
        { name: 'Option 1' },
        { name: 'Option 2' },
    ]
}

export const PollForm: React.FC<Props> = ({}) => {
    let params = useParams();
    const authState = React.useContext(Context)
    const { addToast } = useToasts()
    const [ loading, setLoading ] = React.useState<boolean>(false)
    const [ poll, setPoll ] = React.useState<Poll>({ ...initialValue })
    const [ errors, setErrors ] = React.useState<Errors>(emptyErrors())
    const [ disabled, setDisabled ] = React.useState<boolean>(false)

    React.useEffect(() => {
        (async () => {
            let poll
            if (params.id) {
                poll = await provider.getPollById(params.id)
            } else {
                poll = { ...initialValue }
            }
            setDisabled(!canEditPoll(poll, authState.user))
            setPoll(poll)
        })()
    }, [params])

    const addOption = () => {
        const option: Option = {
            name: ''
        }
        poll.options.push(option)
        setPoll({ ...poll })
    }

    const removeOption = (option: Option) => {
        poll.options.splice(poll.options.indexOf(option), 1)
        setPoll({ ...poll })
    }

    const changeOption = (option: Option, name: string) => {
        option.name = name
        setPoll({ ...poll })
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault()
        setLoading(true)
        setErrors(emptyErrors())
        try {
            const isNew = !poll.id
            let data = await provider.savePoll(poll)
            addToast('Saved Successfully', { appearance: 'success' })
            if (isNew) {
                window.history.pushState(null, '', `/polls/${data.id}/edit`)
            }
            setPoll(data)
        } catch (ex) {
            let errors: Errors = ex;
            if ('fields' in errors) {
                setErrors(errors)
            } else {
                addToast('Unknown error', { appearance: 'error' })
            }
        }
        setLoading(false)
    }

    return (
        <>
            {disabled && (
                <div className="alert alert-danger mb-3">
                    {authState.authenticated ? (
                        'Owned poll. Only the owner can edit it.'
                    ) : (
                        'Owned poll. You must be logged in to edit it.'
                    )}
                </div>
            )}
            <form className="card text-white bg-dark poll-form" onSubmit={handleSubmit}>
                <div className="card-body">
                    <h1 className="card-title">Create Poll</h1>
                    <hr />
                    <div className="form-group">
                        <Input
                            placeholder="Poll title"
                            type="text"
                            value={poll.title}
                            onChange={(e) => setPoll({ ...poll, title: e.target.value })}
                            disabled={disabled}
                            errorMessage={errors.fields['title']} />
                    </div>
                    <div className="form-group">
                        <Textarea
                            placeholder="Poll description"
                            value={poll.description}
                            onChange={(e) => setPoll({ ...poll, description: e.target.value })}
                            disabled={disabled}
                            errorMessage={errors.fields['description']} />
                    </div>
                    <div className="form-group">
                        <div className="form-check">
                            <Checkbox
                                id="restricted"
                                label="Restricted to authenticated users"
                                checked={poll.restricted}
                                disabled={!authState.authenticated}
                                onChange={(e) => setPoll({ ...poll, restricted: e.target.checked })}  />
                            <HelpText message="To restrict this poll you must be authenticated" />
                        </div>
                    </div>
                    <fieldset>
                        <legend>Options</legend>
                        <div className="options">
                            {poll.options.map((option, index) => (
                                <div key={index} className="option">
                                    <Input
                                        type="text"
                                        placeholder="Option name"
                                        value={option.name}
                                        onChange={(e) => changeOption(option, e.target.value)}
                                        disabled={disabled}
                                        errorMessage={errors.fields['options[].name']}
                                        appendAddon={(
                                            <button
                                                type="button"
                                                disabled={disabled || poll.options.length <= 2}
                                                onClick={() => removeOption(option)} className="btn btn-secondary">
                                                x
                                            </button>
                                        )} />
                                </div>
                            ))}
                        </div>
                        <div className="text-right">
                            <button
                                type="button"
                                onClick={addOption} className="btn btn-secondary"
                                disabled={disabled}>
                                Add option
                            </button>
                        </div>
                    </fieldset>
                    <hr />
                    <div className="d-flex">
                        <button type="submit" className="btn btn-primary" disabled={disabled}>
                            Save
                        </button>
                        {loading && (
                            <Spinner label="Loading..." />
                        )}
                        {!!poll.id && (
                            <Link to={`/polls/${poll.slug}`} className="btn btn-secondary ml-auto">
                                View
                            </Link>
                        )}
                    </div>
                </div>
            </form>
        </>
    )
}