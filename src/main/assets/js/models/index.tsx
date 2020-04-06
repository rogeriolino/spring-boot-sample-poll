
export type Pagination<T> = {
    items: T[]
    page: number
    pages: number
    pageSize: number
    total: number
}

export type User = {
    id?: string|null
    username: string
    password?: string|null
}

export type Poll = {
    id?: string|null
    slug?: string|null
    title: string
    description: string
    restricted: boolean
    options: Option[]
    owner?: User|null
    votes?: number
    createdAt?: string
}

export type Option = {
    id?: string|null
    name: string
    votes?: number
}

export type Vote = {
    id?: string|null
    option: Option
}