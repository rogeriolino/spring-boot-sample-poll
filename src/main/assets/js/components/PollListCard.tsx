import * as React from 'react'
import * as dayjs from 'dayjs'
import * as localizedFormat from 'dayjs/plugin/localizedFormat'
import { Poll } from '../models'
import { Link } from 'react-router-dom'

type Props = {
    poll: Poll
}

export const PollListCard: React.FC<Props> = ({ poll }) => {

    const formatDate = (dt) => {
        dayjs.extend(localizedFormat)
        return dayjs(dt).format('L LT')
    }

    return (
        <div
            key={poll.id}
            className="card text-white bg-dark mb-3">
            <div className="card-body">
                <h3 className="card-title">
                    <Link to={`/polls/${poll.slug}`}>{poll.title}</Link>
                </h3>
                <p>{poll.description}</p>
                <small className="text-muted p-0 m-0">
                    created at {formatDate(poll.createdAt)}, votes {poll.votes}
                </small>
            </div>
        </div>
    )
}