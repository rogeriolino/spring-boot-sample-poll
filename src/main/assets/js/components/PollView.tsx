import * as React from 'react'
import { useToasts } from 'react-toast-notifications'
import { useParams, Link } from 'react-router-dom'
import { Context } from '../Context'
import { Poll, Vote, Option } from '../models'
import { ProgressBar } from './ProgressBar'
import { Spinner } from './ui/Spinner'
import { canEditPoll, canClaimPoll } from '../util/security'
import { provider} from '../declarations'
import { Input } from './ui/Input'

type Props = {}

type ViewType = 'vote'|'result'

export const PollView: React.FC<Props> = ({}) => {
    let { slug } = useParams();
    const { addToast } = useToasts()
    const authState = React.useContext(Context)
    const [ loading, setLoading ] = React.useState<boolean>(true)
    const [ view, setView ] = React.useState<ViewType>('vote')
    const [ poll, setPoll ] = React.useState<Poll|null>(null)
    const [ vote, setVote ] = React.useState<Vote>({ option: null })
    const [ shareLink, setShareLink ] = React.useState<string>('')
    const [ disabled, setDisabled ] = React.useState<boolean>(false)

    React.useEffect(() => {
        if (slug) {
            fetchData()
        }
    }, [slug])

    const fetchData = async () => {
        setLoading(true)
        const poll = await provider.getPollBySlug(slug)
        setPoll(poll)
        setDisabled(poll.restricted && !authState.authenticated)
        setShareLink(`${window.location}`)
        try {
            const vote = await provider.getUserVote(poll)
            setVote(vote)
            setView('result')
        } catch (e) {
        }
        setLoading(false)
    }

    const copyToClipboard = () => {
        const el = document.getElementById('share-link') as HTMLInputElement
        el.select()
        document.execCommand('copy')
        addToast('Share link copied to clipboard', { appearance: 'success' })
    }

    const toggleView = () => {
        if (view === 'result') {
            setView('vote')
        } else {
            setView('result')
        }
    }

    const canEdit = (): boolean => {
        return canEditPoll(poll, authState.user)
    }

    const canClaim = (): boolean => {
        return canClaimPoll(poll, authState.user)
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault()
        if (poll && vote.option) {
            setLoading(true)
            try {
                await provider.vote(poll, vote.option)
                addToast('Voted Successfully', { appearance: 'success', autoDismiss: true })
                fetchData()
            } catch (e) {
            }
            setLoading(false)
        }
    }

    const claimPoll = async () => {
        if (poll && poll.id) {
            setLoading(true)
            try {
                await provider.claimPoll(poll)
                fetchData()
            } catch (e) {
            }
            setLoading(false)
        }
    }

    return (
        <>
            {disabled && (
                <div className="alert alert-danger mb-3">
                    Restricted poll. Only the authenticated users can vote.
                </div>
            )}
            <form className="card text-white bg-dark" onSubmit={handleSubmit}>
                <div className="card-body">
                {!!poll && <>
                    <div className="d-flex">
                        <h1 className="card-title">{poll.title}</h1>
                        <div className="ml-auto text-right">
                            <small className="m-1">Votes {poll.votes}</small>
                            {!!poll.owner ? (
                                <small className="m-1">(Owner <b>{poll.owner.username}</b>)</small>
                            ) : (
                                <small className="m-1">(<em>no owner</em>)</small>
                            )}
                            <br/>
                            <Input
                                id="share-link"
                                type="text"
                                readOnly={true}
                                value={shareLink}
                                appendAddon={(
                                    <button
                                        type="button"
                                        className="btn btn-secondary"
                                        onClick={copyToClipboard}>
                                        Share
                                    </button>
                                )}
                                />
                        </div>
                    </div>
                    <p>{poll.description}</p>
                    <hr />
                    <div>
                        {poll.options.map((option, index) => (
                            <div key={index} className={`poll-option ${view==='vote' && option.id===vote.option?.id && 'border border-primary'}`}>
                                {view === 'vote' ? (
                                    <div>
                                        <input
                                            id={`option-${index}`}
                                            type="radio"
                                            name="option"
                                            value={option.id}
                                            checked={option.id===vote.option?.id}
                                            onChange={(e) => setVote({ ...vote, option: option })}
                                            disabled={disabled} />
                                        <label className="pl-2" htmlFor={`option-${index}`}>
                                            {option.name}
                                        </label>
                                    </div>
                                ) :
                                (
                                    <div>
                                        {option.name}
                                        <ProgressBar min={0} max={poll.votes} value={option.votes} />
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                    <hr />
                    <div className="d-flex">
                        {view === 'vote' && (
                            <button type="submit" className="btn btn-primary mr-2" disabled={disabled || !vote.option}>
                                Vote
                            </button>
                        )}
                        <button type="button" className="btn btn-secondary mr-2" onClick={toggleView}>
                            {view === 'vote' ? 'Result' : 'Vote'}
                        </button>
                        {loading && (
                            <Spinner label="Loading..." />
                        )}
                        <div className="ml-auto">
                            {canClaim() && (
                                <button className="btn btn-danger mr-2" onClick={claimPoll} title="Claim this poll to you">
                                    Claim
                                </button>
                            )}
                            {canEdit() && (
                                <Link to={`/polls/${poll.id}/edit`} className="btn btn-secondary">
                                    Edit
                                </Link>
                            )}
                        </div>
                    </div>
                </>}
                </div>
            </form>
        </>
    )
}