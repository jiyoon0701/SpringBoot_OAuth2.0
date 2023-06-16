package com.example.authServerPassword.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.util.DefaultJdbcListFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

@Service
@Primary
@Slf4j
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {


    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("ClientDetailsServiceImpl.loadClientByClientId :::: {}", clientId);
        log.info("loadClientByCheck :::::::::::::: {}", super.loadClientByClientId(clientId).getAdditionalInformation());

       // OAuth2Authentication
        return super.loadClientByClientId(clientId);
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        log.info("ClientDetailsServiceImpl.addClientDetails :::: {}", clientDetails.toString());
        super.addClientDetails(clientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.updateClientDetails :::: {}", clientDetails.toString());
        super.updateClientDetails(clientDetails);
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.updateClientSecret :::: {}, {}", clientId, secret);
        super.updateClientSecret(clientId, secret);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.removeClientDetails :::: {}", clientId);
        super.removeClientDetails(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<ClientDetails> list = super.listClientDetails();
        log.info("ClientDetailsServiceImpl.listClientDetails :::: count = {}", list.size());
        return list;
    }
}
