import axios from 'axios'

type Method = 'GET'|'POST'|'PUT'|'PATCH'|'DELETE';

export class Client {
    constructor(public baseUrl: string) {
    }

    public async request(method: Method, path: string, data?: object|null, params?: object|null, headers={}) {
        const response = await axios.request({
            method,
            url: `${this.baseUrl}${path}`,
            data,
            params,
            headers,
        })
        return response.data
    }

    public async get(path: string, params?: object|null) {
        return await this.request('GET', path, null, params)
    }

    public async post(path: string, data?: object|null) {
        return await this.request('POST', path, data)
    }

    public async patch(path: string, data?: object|null) {
        return await this.request('PATCH', path, data)
    }

    public async put(path: string, data?: object|null) {
        return await this.request('PUT', path, data)
    }

    public async delete(path: string, data?: object|null) {
        return await this.request('DELETE', path, data)
    }
}