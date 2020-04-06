import * as React from 'react'
import { ToastProvider, useToasts } from 'react-toast-notifications'
import { BrowserRouter, Switch, Route, useLocation } from 'react-router-dom'
import { AuthContext } from './Context'
import { Index } from './components/Index'
import { Nav } from './components/Nav'
import { RegistrationForm } from './components/RegistrationForm'
import { PollList } from './components/PollList'
import { MyPolls } from './components/MyPolls'
import { PollForm } from './components/PollForm'
import { PollView } from './components/PollView'

const NoMatch = () => {
  let location = useLocation();

  return (
    <div>
      <h1>Not found</h1>
      <p>
        No match for <code>{location.pathname}</code>
      </p>
    </div>
  );
}

export default () => {
  return (
    <AuthContext>
      <BrowserRouter>
        <ToastProvider autoDismiss={true} placement="top-right">
          <Nav />
          <div role="main" className="container">
            <Switch>
              <Route path="/" component={Index} exact />
              <Route path="/registration" component={RegistrationForm} exact />
              <Route path="/my-polls" component={MyPolls} exact />
              <Route path="/polls/new" component={PollForm} exact />
              <Route path="/polls/:id/edit" component={PollForm} />
              <Route path="/polls/:slug" component={PollView} />
              <Route path="/polls/" component={PollList} />
              <Route path="*">
                <NoMatch />
              </Route>
            </Switch>
          </div>
        </ToastProvider>
      </BrowserRouter>
    </AuthContext>
  )
}