package com.ecomapplication.Security_Service;

import com.vich.sdbmsapi.entity.Student;
import com.vich.sdbmsapi.entity.StudentPrincipal;
import com.vich.sdbmsapi.exception.StudentNotFound;
import com.vich.sdbmsapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private StudentRepository repository ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = repository.findByName(username);

        if (student == null)  throw  new StudentNotFound("Unable to find the Student") ;

        return new StudentPrincipal(student) ;
    }
}
