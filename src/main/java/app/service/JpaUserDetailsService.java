package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import app.repository.UserRepository;

@Component
public class JpaUserDetailsService implements UserDetailsService {

	private UserRepository repository;

	@Autowired
	public JpaUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = this.repository.findByUsername(username);
		return new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoles()));
	}

}