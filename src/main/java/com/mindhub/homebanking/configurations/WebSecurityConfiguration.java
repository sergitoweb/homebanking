package com.mindhub.homebanking.configurations;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.NotificationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpSession;
import java.time.LocalTime;


@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {


    @Autowired
    ClientRepository clientRepository;

    @Autowired
    HttpSession session;

    @Autowired
    NotificationServiceImp notificationService;


    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName -> {
            Client client = clientRepository.findByEmail(inputName).orElse(null);

            if(client != null) {
                session.setAttribute("client", client);

                //asigna admin a todos los email que contengan @admin
                if (client.getEmail().contains("@admin")) {

                    if(client.isHasTelegram()){
                        notificationService.sendNotification(client.getEmail(),
                                "Hola " + client.getFirstName() + " han iniciado sesion a las " +
                                        LocalTime.now());
                    }

                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN"));
                } else {
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
            }else{
                throw new UsernameNotFoundException("Usuario no encontrado:" + inputName);
            }

        });
    }




}
