import * as React from 'react'
import { Poll } from '../models'
import { Spinner } from './ui/Spinner'
import { PollListCard } from './PollListCard'
import { provider } from '../declarations'

type Props = {}

export const PollList: React.FC<Props> = ({}) => {
    const [ loading, setLoading ] = React.useState<boolean>(true)
    const [ mostVoted, setMostVoted ] = React.useState<Poll[]>([])
    const [ lastCreated, setLastCreated ] = React.useState<Poll[]>([])

    React.useEffect(() => {
        (async () => {
            setLoading(true)
            try {
                setMostVoted(await fetchPolls({
                    pageSize: 5,
                    sortBy: 'votes',
                    order: "DESC"
                }))
                setLastCreated(await fetchPolls({
                    pageSize: 5,
                    sortBy: 'createdAt',
                    order: "DESC"
                }))
            } catch (e) {
            }
            setLoading(false)
        })()
    }, [])

    const fetchPolls = async ({ pageSize, sortBy, order }) => {
        const pagination = await provider.getPolls({
            pageSize,
            sortBy,
            order
        })
        return pagination.items
    }

    return (
        <div>
            <h1>Polls</h1>
            <hr/>
            <div className="row">
                <div className="col-12 col-md-6">
                    <h2>Most voted</h2>
                    {loading && (
                        <Spinner label="Loading..." />
                    )}
                    {!loading && mostVoted.length === 0 && (
                        <p className="text-muted">empty</p>
                    )}
                    {mostVoted.map(poll => (
                        <PollListCard poll={poll} />
                    ))}
                </div>
                <hr/>
                <div className="col-12 col-md-6">
                    <h2>Last created</h2>
                    {loading && (
                        <Spinner label="Loading..." />
                    )}
                    {!loading && lastCreated.length === 0 && (
                        <p className="text-muted">empty</p>
                    )}
                    {lastCreated.map(poll => (
                        <PollListCard poll={poll} />
                    ))}
                </div>
            </div>
        </div>
    )
}
