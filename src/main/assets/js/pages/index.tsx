import * as React from 'react'
import * as ReactDOM from 'react-dom'
import '../main'
import App from '../App'

document.addEventListener("DOMContentLoaded", () => {
    const element = document.getElementById('app') as HTMLDivElement
    ReactDOM.render(<App/>, element)
})