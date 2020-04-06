import * as React from 'react'
import { Context } from '../Context'
import { PollListCard } from './PollListCard'
import { Poll } from '../models'
import { Spinner } from './ui/Spinner'
import { provider } from '../declarations'

type Props = {}

export const MyPolls: React.FC<Props> = ({}) => {
    const authState = React.useContext(Context)
    const [ loading, setLoading ] = React.useState<boolean>(true)
    const [ polls, setPolls ] = React.useState<Poll[]>([])

    React.useEffect(() => {
        (async () => {
            setLoading(true)
            try {
                if (authState.authenticated) {
                    const pagination = await provider.getPolls({
                        owner: authState.user.id,
                        pageSize: 5,
                        sortBy: 'createdAt',
                        order: "DESC"
                    })
                    setPolls(pagination.items)
                }
            } catch (e) {
            }
            setLoading(false)
        })()
    }, [])

    return (
        <div>
            <h1>My Polls</h1>
            <hr/>
            <div>
                {loading && (
                    <Spinner label="Loading..." />
                )}
                {!loading && polls.length === 0 && (
                    <p className="text-muted">empty</p>
                )}
                {polls.map(poll => (
                    <PollListCard poll={poll} />
                ))}
            </div>
        </div>
    )
}
