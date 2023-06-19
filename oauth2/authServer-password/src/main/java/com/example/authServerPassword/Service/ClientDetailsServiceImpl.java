package com.example.authServerPassword.Service;

import com.example.authServerPassword.Domain.CustomClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.DefaultJdbcListFactory;
import org.springframework.security.oauth2.common.util.JdbcListFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Primary
@Slf4j
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {
    private static final String DEFAULT_UPDATE_STATEMENT = "update oauth2_client_details set " + "scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove".replaceAll(", ", "=?, ") + "=? where client_id = ?";

    private String updateClientDetailsSql;
    private String updateClientSecretSql;
    private String insertClientDetailsSql;
    private String selectClientDetailsSql;
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private ClientDetailsServiceImpl.JsonMapper mapper = createJsonMapper();
    private JdbcListFactory listFactory;

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
        this.updateClientDetailsSql = DEFAULT_UPDATE_STATEMENT;
        this.updateClientSecretSql = "update oauth2_client_details set client_secret = ? where client_id = ?";
        this.insertClientDetailsSql = "insert into oauth2_client_details (client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, client_id) values (?,?,?,?,?,?,?,?,?)";
        this.selectClientDetailsSql = "select client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth2_client_details where client_id = ?";
        this.passwordEncoder = NoOpPasswordEncoder.getInstance();
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.listFactory = new DefaultJdbcListFactory(new NamedParameterJdbcTemplate(this.jdbcTemplate));
    }

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("ClientDetailsServiceImpl.loadClientByClientId :::: {}", clientId);

        try {
            ClientDetails details = this.jdbcTemplate.queryForObject(this.selectClientDetailsSql, new ClientDetailsRowMapper(), new Object[]{clientId});
            return details;
        } catch (EmptyResultDataAccessException var4) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        log.info("ClientDetailsServiceImpl.addClientDetails :::: {}", clientDetails.toString());

        try {
            this.jdbcTemplate.update(this.insertClientDetailsSql, this.getFields(clientDetails));
        } catch (DuplicateKeyException var3) {
            throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), var3);
        }
        //super.addClientDetails(clientDetails);
    }

   // @Override
    public void updateClientDetails(CustomClient clientDetails) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.updateClientDetails :::: {}", clientDetails.toString());
       // super.updateClientDetails(clientDetails);
    }

  //  @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.updateClientSecret :::: {}, {}", clientId, secret);
        super.updateClientSecret(clientId, secret);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        log.info("ClientDetailsServiceImpl.removeClientDetails :::: {}", clientId);
        super.removeClientDetails(clientId);
    }

//    @Override
//    public List<ClientDetails> listClientDetails() {
//     //   List<ClientDetails> list = super.listClientDetails();
//        log.info("ClientDetailsServiceImpl.listClientDetails :::: count = {}", list.size());
//        return list;
//    }

    private Object[] getFields(ClientDetails clientDetails) {
        Object[] fieldsForUpdate = this.getFieldsForUpdate(clientDetails);
        Object[] fields = new Object[fieldsForUpdate.length + 1];
        System.arraycopy(fieldsForUpdate, 0, fields, 1, fieldsForUpdate.length);
        fields[0] = clientDetails.getClientSecret() != null ? this.passwordEncoder.encode(clientDetails.getClientSecret()) : null;
        return fields;
    }

    private Object[] getFieldsForUpdate(ClientDetails clientDetails) {
        String json = null;

        try {
            json = this.mapper.write(clientDetails.getAdditionalInformation());
        } catch (Exception var4) {
            //   logger.warn("Could not serialize additional information: " + clientDetails, var4);
        }

        return new Object[]{clientDetails.getScope() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()) : null, clientDetails.getAuthorizedGrantTypes() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()) : null, clientDetails.getAuthorities() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()) : null, clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(), json, this.getAutoApproveScopes(clientDetails), clientDetails.getClientId()};
    }

    private static ClientDetailsServiceImpl.JsonMapper createJsonMapper() {
        if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", (ClassLoader)null)) {
            return new JacksonMapper();
        } else {
            return (ClientDetailsServiceImpl.JsonMapper)(ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", (ClassLoader)null) ? new Jackson2Mapper() : new NotSupportedJsonMapper());
        }
    }

    private String getAutoApproveScopes(ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return "true";
        } else {
            Set<String> scopes = new HashSet();
            Iterator var3 = clientDetails.getScope().iterator();

            while(var3.hasNext()) {
                String scope = (String)var3.next();
                if (clientDetails.isAutoApprove(scope)) {
                    scopes.add(scope);
                }
            }

            return StringUtils.collectionToCommaDelimitedString(scopes);
        }
    }

    private static class NotSupportedJsonMapper implements ClientDetailsServiceImpl.JsonMapper {
        private NotSupportedJsonMapper() {
        }

        public String write(Object input) throws Exception {
            throw new UnsupportedOperationException("Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            throw new UnsupportedOperationException("Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }
    }

    private static class Jackson2Mapper implements ClientDetailsServiceImpl.JsonMapper {
        private ObjectMapper mapper;

        private Jackson2Mapper() {
            this.mapper = new ObjectMapper();
        }

        public String write(Object input) throws Exception {
            return this.mapper.writeValueAsString(input);
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            return this.mapper.readValue(input, type);
        }
    }

    private static class JacksonMapper implements ClientDetailsServiceImpl.JsonMapper {
        private org.codehaus.jackson.map.ObjectMapper mapper;

        private JacksonMapper() {
            this.mapper = new org.codehaus.jackson.map.ObjectMapper();
        }

        public String write(Object input) throws Exception {
            return this.mapper.writeValueAsString(input);
        }

        public <T> T read(String input, Class<T> type) throws Exception {
            return this.mapper.readValue(input, type);
        }
    }

    interface JsonMapper {
        String write(Object var1) throws Exception;

        <T> T read(String var1, Class<T> var2) throws Exception;
    }

    private static class ClientDetailsRowMapper implements RowMapper<CustomClient> {
        private ClientDetailsServiceImpl.JsonMapper mapper;

        private ClientDetailsRowMapper() {
            this.mapper = ClientDetailsServiceImpl.createJsonMapper();
        }

        public CustomClient mapRow(ResultSet rs, int rowNum) throws SQLException {

            CustomClient details = new CustomClient(rs.getString(1), rs.getString(3),rs.getString(4), rs.getString(5));
            details.setClientSecret(rs.getString(2));
            if (rs.getObject(6) != null) {
                details.setAccessTokenValiditySeconds(rs.getInt(6));
            }

            if (rs.getObject(7) != null) {
                details.setRefreshTokenValiditySeconds(rs.getInt(7));
            }

            String json = rs.getString(8);
            if (json != null) {
                try {
                    Map<String, Object> additionalInformation = (Map)this.mapper.read(json, Map.class);
                    details.setAdditionalInformation(additionalInformation);
                } catch (Exception var6) {
                   // JdbcClientDetailsService.logger.warn("Could not decode JSON for additional information: " + details, var6);
                }
            }

            String scopes = rs.getString(9);
            if (scopes != null) {
                details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
            }

            return details;
        }
    }




}
