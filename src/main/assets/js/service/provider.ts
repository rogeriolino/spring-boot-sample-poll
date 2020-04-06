import { Client } from './api'
import { Poll, User, Pagination, Vote, Option } from '../models'
import { emptyErrors, FieldError } from '../util/errors'

type SpringPage = {
    content: []
    pageable: {
        pageNumber: number
        pageSize: number
    }
    totalPages: number
    totalElements: number
}

type SpringPageArgs = {
    pageNo?: number
    pageSize?: number
    sortBy?: string
    order?: 'ASC'|'DESC'
}

type PollArgs = SpringPageArgs & {
    owner?: string
}

export class Provider {
    private api: Client

    constructor(baseUrl: string) {
        this.api = new Client(baseUrl)
    }

    private async try(fn: Function) {
        try {
            return await fn()
        } catch (e) {
            if (e?.response?.data?.errors) {
                // parse validation errors
                const errors = emptyErrors()
                errors.fields = e?.response?.data?.errors as FieldError[]
                throw errors
            }
            throw e
        }
    }

    async profile(): Promise<User> {
        return this.try(async () => {
            const data = await this.api.get('/profile')
            return data as User
        })
    }

    async register(user: User): Promise<User> {
        return this.try(async () => {
            const data = await this.api.post('/users', user)
            return data as User
        })
    }

    async getPollById(id: string): Promise<Poll> {
        return this.try(async () => {
            const data = await this.api.get(`/polls/${id}`)
            return data as Poll
        })
    }

    async getPollBySlug(slug: string): Promise<Poll> {
        return this.try(async () => {
            const data = await this.api.get(`/polls/${slug}/slug`)
            return data as Poll
        })
    }

    async getPolls(args?: PollArgs|null): Promise<Pagination<Poll>> {
        return this.try(async () => {
            const data = await this.api.get(`/polls`, args) as SpringPage
            const pagination: Pagination<Poll> = {
                items: data.content,
                page: data.pageable.pageNumber,
                pageSize: data.pageable.pageSize,
                pages: data.totalPages,
                total: data.totalElements,
            }
            return pagination
        })
    }

    async savePoll(poll: Poll): Promise<Poll> {
        return this.try(async () => {
            let data = { ...poll }
            if (data.id) {
                data = await this.api.put(`/polls/${poll.id}`, data)
            } else {
                data = await this.api.post('/polls', data)
            }
            return data as Poll
        })
    }

    async claimPoll(poll: Poll): Promise<Poll> {
        return this.try(async () => {
            const data = await this.api.post(`/polls/${poll.id}/claim`)
            return data as Poll
        })
    }

    async getUserVote(poll: Poll): Promise<Vote> {
        return this.try(async () => {
            const data = await this.api.get(`/polls/${poll.id}/vote`)
            return data as Vote
        })
    }

    async vote(poll: Poll, option: Option): Promise<Vote> {
        return this.try(async () => {
            const data = await this.api.post(`/polls/${poll.id}/vote/${option.id}`)
            return data as Vote
        })
    }
}