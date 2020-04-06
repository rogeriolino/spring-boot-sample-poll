import { Poll, User } from '../models'

export const canEditPoll = (poll: Poll, user: User): boolean => {
    if (poll) {
        if (!poll.owner) {
            return true
        }
        if (user) {
            return user.id === poll.owner.id
        }
    }
    return false
}

export const canClaimPoll = (poll: Poll, user: User): boolean => {
    return (
        !poll.owner && !!user && !!user.id
    )
}